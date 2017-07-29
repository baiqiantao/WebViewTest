package test.bqt.com.webviewtest;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import test.bqt.com.webviewtest.websetting.WebSettingsModel;

public class MyWebViewClient extends WebViewClient {
	private ProgressBar mProgressBar;
	private WebSettingsModel model;
	
	public MyWebViewClient(ProgressBar mProgressBar, WebSettingsModel model) {
		super();
		this.mProgressBar = mProgressBar;
		this.model = (model != null ? model : WebSettingsModel.newBuilder()
				.build());
	}
	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		//favicon(网站图标)：如果这个favicon已经存储在本地数据库中，则会返回这个网页的favicon，否则返回为null
		Log.i("bqt", "【onPageStarted】" + url);
		if (mProgressBar != null) mProgressBar.setVisibility(View.VISIBLE);//在开始加载时显示进度条
		super.onPageStarted(view, url, favicon);
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
		Log.i("bqt", "【onPageFinished】" + url);
		if (mProgressBar != null) mProgressBar.setVisibility(View.GONE);//在结束加载时隐藏进度条
		super.onPageFinished(view, url);
	}
	
	@Override
	public void onLoadResource(WebView view, String url) {
		Log.i("bqt", "【onLoadResource】" + url);//每一个资源（比如图片）的加载都会调用一次
		super.onLoadResource(view, url);
	}
	
	@TargetApi(Build.VERSION_CODES.M)
	@Override
	public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
		//访问指定的网址发生错误时回调，我们可以在这里做错误处理，比如再请求加载一次，或者提示404的错误页面
		Log.i("bqt", "【onReceivedError】" + request.getUrl().toString() + "  " + error.getErrorCode() + "  " + error.getDescription());
		super.onReceivedError(view, request, error);
	}
	
	@TargetApi(Build.VERSION_CODES.M)
	@Override
	public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
		//HTTP错误具有> = 400的状态码。请注意，errorResponse参数中可能不提供服务器响应的内容。
		Log.i("bqt", "【onReceivedHttpError】" + request.getUrl().toString() + "  " + errorResponse.getStatusCode()
				+ "  " + errorResponse.getEncoding() + "  " + errorResponse.getMimeType());//502  utf-8  text/html
		super.onReceivedHttpError(view, request, errorResponse);
	}
	
	@Override
	public void onScaleChanged(WebView view, float oldScale, float newScale) {
		//应用程序可以处理改事件，比如调整适配屏幕
		Log.i("bqt", "【onScaleChanged】" + "oldScale=" + oldScale + "  newScale=" + newScale);
		super.onScaleChanged(view, oldScale, newScale);
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
		//每一次请求资源时都会回调。如果我们需要改变网页的背景，可以在这里处理。
		//如果返回值为null，则WebView会照常继续加载资源。 否则，将使用返回的响应和数据。
		Log.i("bqt", "【shouldInterceptRequest】" + request.getUrl().toString() + "  " + request.getMethod());
		return super.shouldInterceptRequest(view, request);
	}
	
	@Override
	public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
		//给主机应用程序一次同步处理键事件的机会。如果应用程序想要处理该事件则返回true，否则返回false。
		Log.i("bqt", "【shouldOverrideKeyEvent】" + event.getAction() + "  " + event.getKeyCode());
		return super.shouldOverrideKeyEvent(view, event);
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
		Log.i("bqt", "【shouldOverrideUrlLoading】" + request.getUrl().toString() + "  " + request.getMethod());
		view.loadUrl(request.getUrl().toString());//不去调用系统浏览器， 而是在本WebView中跳转
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Log.i("bqt", "【shouldOverrideUrlLoading废弃方法】" + url);
		return super.shouldOverrideUrlLoading(view, url);
	}
}