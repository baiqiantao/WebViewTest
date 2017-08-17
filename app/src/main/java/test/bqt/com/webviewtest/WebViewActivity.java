package test.bqt.com.webviewtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import test.bqt.com.webviewtest.client.MyWebChromeClient;
import test.bqt.com.webviewtest.client.MyWebViewClient;
import test.bqt.com.webviewtest.websetting.WebSettingsModel;
import test.bqt.com.webviewtest.websetting.WebSettingsUtils;

public class WebViewActivity extends Activity implements View.OnClickListener {
	public static final String WEB_MODEL = "model";
	public static final String JS_INTERFACE = "Android";//JS调用类名
	private GestureDetector mGestureDetector;//双击事件手势识别器
	private WebSettingsModel model;
	private WebView webview;
	private ProgressBar progress_bar;
	private TextView tv_title;
	private TextView tv_back;
	private ImageView iv_icon;
	private FrameLayout fl_content;
	private MyWebChromeClient mWebChromeClient;
	
	public static void start(Activity ctx, WebSettingsModel model) {
		Intent intent = new Intent(ctx, WebViewActivity.class);
		intent.putExtra(WEB_MODEL, model);
		ctx.startActivity(intent);
	}
	
	//*****************************************************生命周期方法*********************************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_webview);
		initIntent();
		findViews();
		initViews();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (webview != null) {
			if (webview.getParent() != null) ((ViewGroup) webview.getParent()).removeView(webview);
			webview.removeAllViews();
			webview.loadUrl("about:blank");
			webview.stopLoading();
			webview.setWebChromeClient(null);
			webview.setWebViewClient(null);
			webview.destroy();
			webview = null;
		}
	}
	
	//*****************************************************重写的方法*********************************************************
	@Override
	//设置当点击后退按钮时不是退出Activity，而是让WebView后退一页。也可以通过webview.setOnKeyListener设置
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebChromeClient != null && mWebChromeClient.isOnShowCustomView()) {
				mWebChromeClient.onHideCustomView();
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				return true;
			} else if (webview.canGoBack()) {//返回网页上一页
				webview.goBack(); //后退，goForward() 前进
				return true;
			} else {//退出网页
				webview.loadUrl("about:blank");
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_back:
				finish();
				break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.i("bqt", "【onActivityResult】 requestCode=" + requestCode + "  resultCode=" + resultCode);
		//上传图片之后的回调
		if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE) {
			mWebChromeClient.mUploadMessage(intent, resultCode);
		} else if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
			mWebChromeClient.mUploadMessageForAndroid5(intent, resultCode);
		}
	}
	
	//*****************************************************初始化方法*********************************************************
	private void initIntent() {
		Intent intent = getIntent();
		if (intent != null) {
			if (intent.getParcelableExtra(WEB_MODEL) != null) model = intent.getParcelableExtra(WEB_MODEL);
			else model = WebSettingsModel.newBuilder().build();
			
			if (intent.getDataString() != null) model.url = initUrl(intent.getDataString(), model.searchType);//优先使用此URL
			else model.url = initUrl(model.url, model.searchType);
		} else model = WebSettingsModel.newBuilder().build();
		Log.i("bqt", "【参数】" + model.toString());
	}
	
	private String initUrl(String url, String searchType) {
		if (url == null || url.equals("")) return "http://www.cnblogs.com/baiqiantao";
		else if (url.startsWith("file://")) return url;
		else if (!url.startsWith("http://") && !url.startsWith("https://")) {//不以http开头
			if (url.startsWith("www.")
					|| url.endsWith(".com") || url.endsWith(".cn") || url.endsWith(".org") || url.endsWith(".net")
					|| url.endsWith(".co") || url.endsWith(".cc") || url.endsWith(".xyz") || url.endsWith(".so")) {
				url = "http://" + url; //添加"http://"前缀
			} else {
				//使用百度等搜索引擎搜索输入的内容
				if (searchType == null || searchType.equalsIgnoreCase("baidu")) url = WebSettingsModel.SEARCH_TYPE_BAIDU + url;
				else if (searchType.equalsIgnoreCase("google")) url = WebSettingsModel.SEARCH_TYPE_GOOGLE + url;
				else if (searchType.equalsIgnoreCase("github")) url = WebSettingsModel.SEARCH_TYPE_GITHUB + url;
				else url = WebSettingsModel.SEARCH_TYPE_BAIDU + url;//使用百度搜索输入的内容
			}
		}
		return url;
	}
	
	private void findViews() {
		if (model.showHorizontalPB) {
			progress_bar = (ProgressBar) findViewById(R.id.progress_bar_horizontal);
			progress_bar.setIndeterminate(false);
			findViewById(R.id.progress_bar_center).setVisibility(View.GONE);
		} else if (model.showCenterPB) {
			progress_bar = (ProgressBar) findViewById(R.id.progress_bar_center);
			progress_bar.setIndeterminate(true);//自动在最小到最大值之间来回移动，不明确具体的值
			findViewById(R.id.progress_bar_horizontal).setVisibility(View.GONE);
		} else {
			findViewById(R.id.progress_bar_center).setVisibility(View.GONE);
			findViewById(R.id.progress_bar_horizontal).setVisibility(View.GONE);
		}
		
		webview = (WebView) findViewById(R.id.webview);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_back = (TextView) findViewById(R.id.tv_back);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		fl_content = (FrameLayout) findViewById(R.id.fl_content);
	}
	
	@SuppressLint("JavascriptInterface")
	private void initViews() {
		tv_back.setOnClickListener(this);
		tv_title.setText(model.title);
		mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				webview.scrollTo(0, 0);
				return super.onDoubleTap(e);
			}
		});
		tv_title.setOnTouchListener((v, event) -> {
			mGestureDetector.onTouchEvent(event);
			return true;
		});
		
		WebSettingsUtils.setWebSettings(webview.getSettings(), model);
		webview.setWebViewClient(new MyWebViewClient(progress_bar));
		webview.setWebChromeClient(mWebChromeClient = new MyWebChromeClient(this));
		webview.addJavascriptInterface(new WebAppinterface(this), JS_INTERFACE);// 注册后可以在JS中调用此接口中定义的方法
		//在6.0之前，WebView没有提供setOnScrollChangeListener方法，需要我们自定义WebView，并重写其onScrollChanged方法
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//23
			webview.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
				//这里OnScrollChangeListener中回调过来的数据完全是基类View中onScrollChanged方法中的数据
				Log.i("bqt", "【onScrollChange】" + scrollX + "  " + scrollY + "  " + oldScrollX + "  " + oldScrollY);
			});
		}
		webview.loadUrl(model.url);
	}
	
	public WebView getWebview() {
		return webview;
	}
	
	public ProgressBar getProgress_bar() {
		return progress_bar;
	}
	
	public TextView getTv_title() {
		return tv_title;
	}
	
	public TextView getTv_back() {
		return tv_back;
	}
	
	public ImageView getIv_icon() {
		return iv_icon;
	}
	
	public FrameLayout getFl_content() {
		return fl_content;
	}
}