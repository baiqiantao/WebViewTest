package com.bqt.test.wv;

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

import com.bqt.test.wv.client.MyWebChromeClient;
import com.bqt.test.wv.client.MyWebViewClient;
import com.bqt.test.wv.websetting.WebSettingsModel;
import com.bqt.test.wv.websetting.WebSettingsUtils;

public class WebViewActivity extends Activity {
	public static final String WEB_SETTINGS_MODEL = "WebSettingsModel";
	private GestureDetector mGestureDetector;//双击事件手势识别器
	private WebSettingsModel model;
	private WebView webview;
	private ProgressBar progress_bar;
	private TextView tv_title;
	private TextView tv_back;
	private ImageView iv_icon;
	private FrameLayout fl_content;
	private MyWebChromeClient mWebChromeClient;
	
	public static void start(Activity ctx, WebSettingsModel webSettingsModel) {
		Intent intent = new Intent(ctx, WebViewActivity.class);
		intent.putExtra(WEB_SETTINGS_MODEL, webSettingsModel);
		ctx.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_webview);
		if (getIntent() != null && getIntent().getParcelableExtra(WEB_SETTINGS_MODEL) != null) {
			model = getIntent().getParcelableExtra(WEB_SETTINGS_MODEL);
		} else {
			model = WebSettingsModel.newBuilder().build();
		}
		Log.i("bqt", "【参数】" + model.toString());
		initViews();
		webview.loadUrl(model.url);
	}
	
	private void initViews() {
		webview = findViewById(R.id.webview);
		tv_title = findViewById(R.id.tv_title);
		tv_back = findViewById(R.id.tv_back);
		iv_icon = findViewById(R.id.iv_icon);
		fl_content = findViewById(R.id.fl_content);
		
		if (model.showHorizontalPB) {
			progress_bar = findViewById(R.id.progress_bar_horizontal);
			progress_bar.setIndeterminate(false);
			findViewById(R.id.progress_bar_center).setVisibility(View.GONE);
		} else if (model.showCenterPB) {
			progress_bar = findViewById(R.id.progress_bar_center);
			progress_bar.setIndeterminate(true);//自动在最小到最大值之间来回移动，不明确具体的值
			findViewById(R.id.progress_bar_horizontal).setVisibility(View.GONE);
		} else {
			findViewById(R.id.progress_bar_center).setVisibility(View.GONE);
			findViewById(R.id.progress_bar_horizontal).setVisibility(View.GONE);
		}
		
		tv_back.setOnClickListener(v -> finish());
		tv_title.setText(model.title);
		mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				webview.scrollTo(0, 0);
				return super.onDoubleTap(e);
			}
		});
		findViewById(R.id.rl_top_bar).setOnTouchListener((v, event) -> {
			mGestureDetector.onTouchEvent(event);
			return true;
		});
		
		mWebChromeClient = new MyWebChromeClient(this);
		WebSettingsUtils.setWebSettings(webview.getSettings(), model);
		webview.setWebViewClient(new MyWebViewClient(this));
		webview.setWebChromeClient(mWebChromeClient);
		webview.addJavascriptInterface(new JSInterface(this, webview), JSInterface.JS_INTERFACE_NAME);
		
		//在6.0之前，WebView没有提供setOnScrollChangeListener方法，需要我们自定义WebView，并重写其onScrollChanged方法
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//23
			webview.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
				//这里OnScrollChangeListener中回调过来的数据完全是基类View中onScrollChanged方法中的数据
				//Log.i("bqt", "【onScrollChange】" + scrollX + "  " + scrollY + "  " + oldScrollX + "  " + oldScrollY);
			});
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (webview != null) {
			if (webview.getParent() != null) {
				((ViewGroup) webview.getParent()).removeView(webview);
			}
			webview.removeAllViews();
			webview.stopLoading();
			webview.loadUrl("about:blank");
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
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.i("bqt", "【onActivityResult】 requestCode=" + requestCode + "  resultCode=" + resultCode);
		//上传图片之后的回调
		if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE) {
			mWebChromeClient.mUploadMessage(intent, resultCode);
		} else if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
			mWebChromeClient.mUploadMessageForAndroid5(intent, resultCode);
		}
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