package test.bqt.com.webviewtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import static test.bqt.com.webviewtest.WebSettingsUtils.setWebSettings2;

public class WebViewActivity extends Activity implements View.OnClickListener {
	public static final String WEB_MODEL = "model";
	private WebViewModel model;
	
	public static final String JS_INTERFACE = "Android";//JS调用类名
	private WebView webview;
	private ProgressBar progress_bar;
	private TextView tv_title;
	private TextView tv_back;

	public static void start(Activity ctx, WebViewModel model) {
		Intent intent = new Intent(ctx, WebViewActivity.class);
		intent.putExtra(WEB_MODEL, model);
		ctx.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_webview);
		initIntent();
		initViews();
	}
	
	@SuppressLint("JavascriptInterface")
	private void initViews() {
		progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
		webview = (WebView) findViewById(R.id.webview);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_back = (TextView) findViewById(R.id.tv_back);

		tv_back.setOnClickListener(this);
		progress_bar.setIndeterminate(true);//自动在最小到最大值之间来回移动，不明确具体的值
		tv_title.setText(model.title);

		setWebSettings(webview.getSettings());
		webview.loadUrl(model.url);
		webview.setWebViewClient(new MyWebViewClient(progress_bar, model));//在本WebView中显示网页内容。
		webview.addJavascriptInterface(new WebAppinterface(this), JS_INTERFACE);// 注册后可以在JS中调用此接口中定义的方法
	}
	
	private void initIntent() {
		Intent intent = getIntent();
		if (intent != null) {
			model = intent.getParcelableExtra(WEB_MODEL);
			if (model == null) model = WebViewModel.newBuilder().build();

			String url = intent.getDataString();
			if (url != null) model.url = initUrl(url, model.searchType);//优先使用此URL
			else model.url = initUrl(model.url, model.searchType);
		} else model = WebViewModel.newBuilder().build();
		Log.i("bqt", model.toString());
	}
	
	public static String initUrl(String url, String searchType) {
		if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {//不以http开头
			if (url.startsWith("www.")
					|| url.endsWith(".com") || url.endsWith(".cn") || url.endsWith(".org") || url.endsWith(".net")
					|| url.endsWith(".co") || url.endsWith(".cc") || url.endsWith(".xyz") || url.endsWith(".so")) {
				url = "http://" + url; //添加"http://"前缀
			} else {
				//使用百度等搜索引擎搜索输入的内容
				if (searchType == null || searchType.equalsIgnoreCase("baidu")) url = WebViewModel.SEARCH_TYPE_BAIDU + url;
				else if (searchType.equalsIgnoreCase("google")) url = WebViewModel.SEARCH_TYPE_GOOGLE + url;
				else if (searchType.equalsIgnoreCase("github")) url = WebViewModel.SEARCH_TYPE_GITHUB + url;
				else url = WebViewModel.SEARCH_TYPE_BAIDU + url;//使用百度搜索输入的内容
			}
		}
		return url;
	}
	
	@Override
	//点击后退按钮不退出Activity，而是让WebView后退一页。也可以通过webview.setOnKeyListener设置
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
			webview.goBack(); //后退，goForward() 前进
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (webview != null) {
			webview.loadUrl("about:blank");
			webview.destroy();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_back:
				finish();
				break;
		}
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void setWebSettings(WebSettings webSettings) {
		//必选的JavaScript
		webSettings.setJavaScriptEnabled(true);//允许js交互
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//让JavaScript自动打开窗口
		
		//布局相关
		webSettings.setBuiltInZoomControls(true); //使用内置的缩放机制，设为false时setDisplayZoomControls无效
		webSettings.setDisplayZoomControls(false);//使用内置的缩放机制时是否展示缩放控件
		webSettings.setUseWideViewPort(true);//重要！布局的宽度总是与WebView控件上的设备无关像素宽度一致
		webSettings.setLoadWithOverviewMode(true);//允许缩小内容以适应屏幕宽度
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//设置布局类型，会引起WebView重新布局
		//默认值NARROW_COLUMNS（适应内容大小），SINGLE_COLUMN（适应屏幕，内容将自动缩放）
		
		//缓存
		webSettings.setAppCacheEnabled(true);//应用缓存API可用
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//使用缓存的方式，默认值LOAD_DEFAULT，LOAD_NO_CACHE
		
		//其他
		webSettings.setDatabaseEnabled(true);//数据库存储API可用
		webSettings.setDomStorageEnabled(true);//DOM存储API可用
		webSettings.setSupportMultipleWindows(true);//支持多窗口。如果设置为true，主程序要实现onCreateWindow
	}
}