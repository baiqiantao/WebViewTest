package com.bqt.test.wv.client;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.bqt.test.wv.WebViewActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class MyWebViewClient extends WebViewClient {
	private ProgressBar mProgressBar;
	private WebViewActivity activity;
	
	public MyWebViewClient(WebViewActivity activity) {
		super();
		this.activity = activity;
		mProgressBar = activity.getProgress_bar();
	}
	
	@Override
	public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
		//通知主机应用程序更新其访问链接数据库（更新访问历史）。isReload：是否是正在被reload的url
		Log.i("bqt", "【doUpdateVisitedHistory】" + url + "   " + isReload);
		super.doUpdateVisitedHistory(view, url, isReload);
	}
	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		//favicon(网站图标)：如果这个favicon已经存储在本地数据库中，则会返回这个网页的favicon，否则返回为null
		Log.i("bqt", "【onPageStarted】" + url);
		if (mProgressBar != null) mProgressBar.setVisibility(View.VISIBLE);//在开始加载时显示进度条
		activity.getIv_icon().setVisibility(View.GONE);
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
		//如点击一个迅雷下载的资源时的错误信息为【ftp://***  -10  net::ERR_UNKNOWN_URL_SCHEME】，此时我们就可以加载指定的页面
		if (error.getErrorCode() == -10 && request.getUrl().toString().endsWith(".mp4")) view.loadUrl("file:///android_asset/h5/test.html");
		else super.onReceivedError(view, request, error);
	}
	
	@TargetApi(Build.VERSION_CODES.M)
	@Override
	public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
		//HTTP错误具有 >=400 的状态码。请注意，errorResponse参数中可能不提供服务器响应的内容。
		//如【502  utf-8  text/html】【http://www.dy2018.com/favicon.ico  404    text/html】
		Log.i("bqt", "【onReceivedHttpError】" + request.getUrl().toString() + "  " + errorResponse.getStatusCode()
				+ "  " + errorResponse.getEncoding() + "  " + errorResponse.getMimeType());
		super.onReceivedHttpError(view, request, errorResponse);
	}
	
	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
		//HTTP错误具有 >=400 的状态码。请注意，errorResponse参数中可能不提供服务器响应的内容。
		//如，点击12306中的购票时【https://kyfw.12306.cn/otn/  3  Issued to: CN=kyfw.12306.cn,***】
		Log.i("bqt", "【onReceivedSslError】" + error.getUrl() + "  " + error.getPrimaryError() + "  " + error.getCertificate().toString());
		if (new Random().nextBoolean()) super.onReceivedSslError(view, handler, error);//默认行为，取消加载
		else handler.proceed();//忽略错误继续加载
	}
	
	@Override
	public void onScaleChanged(WebView view, float oldScale, float newScale) {
		//应用程序可以处理该事件，比如调整适配屏幕
		Log.i("bqt", "【onScaleChanged】" + "oldScale=" + oldScale + "  newScale=" + newScale);
		super.onScaleChanged(view, oldScale, newScale);
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
		//每一次请求资源时都会回调。如果我们需要改变网页的背景，可以在这里处理。
		//如果返回值为null，则WebView会照常继续加载资源。 否则，将使用返回的响应和数据。
		Log.i("bqt", "【shouldInterceptRequest】" + request.getUrl().toString() + "  " + request.getMethod());
		if (new Random().nextBoolean()) return super.shouldInterceptRequest(view, request);
		else if (request.getUrl().toString().endsWith("你妹的.jpg")) {
			try {
				InputStream data = view.getContext().getAssets().open("icon.jpg");
				return new WebResourceResponse("text/html", "UTF-8", data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
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
		WebView.HitTestResult hitTestResult = view.getHitTestResult();
		int type = hitTestResult.getType();
		Log.i("bqt", "【shouldOverrideUrlLoading】" + request.getMethod() + "  " + type + "  " + request.getUrl().toString());
		switch (type) {
			case WebView.HitTestResult.UNKNOWN_TYPE: //0 未知，【异步重定向】
				break;
			case WebView.HitTestResult.PHONE_TYPE: //2 处理拨号
				break;
			case WebView.HitTestResult.GEO_TYPE: //3 地图类型
				break;
			case WebView.HitTestResult.EMAIL_TYPE: //4 处理Email
				break;
			case WebView.HitTestResult.IMAGE_TYPE: //5 处理长按图片的菜单项
			case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE: //8 带有链接的图片
				break;
			case WebView.HitTestResult.SRC_ANCHOR_TYPE: //7 超链接
				break;
			case WebView.HitTestResult.EDIT_TEXT_TYPE: //9 可编辑的区域
				break;
		}
		return super.shouldOverrideUrlLoading(view, request);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Log.i("bqt", "【shouldOverrideUrlLoading废弃方法】" + url);
		//识别电话、短信、邮件等
		if (url.startsWith(WebView.SCHEME_TEL) || url.startsWith("sms:") || url.startsWith(WebView.SCHEME_MAILTO)) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			view.getContext().startActivity(intent);
		} else {
			view.loadUrl(url);//不去调用系统浏览器， 而是在本WebView中跳转
		}
		return true;
	}
}