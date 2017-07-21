package test.bqt.com.webviewtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import test.bqt.com.webviewtest.websetting.WebSettingsModel;
import test.bqt.com.webviewtest.websetting.WebSettingsUtils;

public class WebViewActivity extends Activity implements View.OnClickListener {
	public static final String WEB_MODEL = "model";
	public static final String JS_INTERFACE = "Android";//JS调用类名
	private GestureDetector mGestureDetector;//双击事件手势识别器
	private WebSettingsModel model;
	private WebView webview;
	private final ThreadLocal<ProgressBar> progress_bar = new ThreadLocal<>();
	private final ThreadLocal<TextView> tv_title = new ThreadLocal<>();
	private final ThreadLocal<TextView> tv_back = new ThreadLocal<>();

	public static void start(Activity ctx, WebSettingsModel model) {
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
		progress_bar.set((ProgressBar) findViewById(R.id.progress_bar));
		webview = (WebView) findViewById(R.id.webview);
		tv_title.set((TextView) findViewById(R.id.tv_title));
		tv_back.set((TextView) findViewById(R.id.tv_back));

		tv_back.get().setOnClickListener(this);
		progress_bar.get().setIndeterminate(true);//自动在最小到最大值之间来回移动，不明确具体的值
		tv_title.get().setText(model.title);
		mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				webview.scrollTo(0, 0);
				return super.onDoubleTap(e);
			}
		});
		tv_title.get().setOnTouchListener((v, event) -> {
			mGestureDetector.onTouchEvent(event);
			return true;
		});

		WebSettingsUtils.setWebSettings(webview.getSettings(), model);
		webview.loadUrl(model.url);
		webview.setWebViewClient(new MyWebViewClient(progress_bar.get(), model));//在本WebView中显示网页内容。
		webview.addJavascriptInterface(new WebAppinterface(this), JS_INTERFACE);// 注册后可以在JS中调用此接口中定义的方法
	}
	
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
	
	public static String initUrl(String url, String searchType) {
		if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {//不以http开头
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

	@Override
	//点击后退按钮不退出Activity，而是让WebView后退一页。也可以通过webview.setOnKeyListener设置
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
			webview.goBack(); //后退，goForward() 前进
			return true;
		} else finish();
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
}