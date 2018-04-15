package com.bqt.test.wv.client;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bqt.test.wv.R;
import com.bqt.test.wv.WebViewActivity;

import java.util.Arrays;
import java.util.Random;

public class MyWebChromeClient extends WebChromeClient {
	private WebViewActivity activity;//控件的显示和隐藏应该都由WebViewClient控制
	
	public MyWebChromeClient(WebViewActivity activity) {
		super();
		this.activity = activity;
	}
	
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		Log.i("bqt", "【onProgressChanged】  " + newProgress);
		if (activity.getProgress_bar() != null) activity.getProgress_bar().setProgress(newProgress);
		super.onProgressChanged(view, newProgress);
	}
	
	@Override
	public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
		//If the client returns true, WebView will assume that the client will handle the dialog. Otherwise, it will continue execution执行.
		boolean b = new Random().nextBoolean();
		Log.i("bqt", "【onJsAlert】" + b + "  " + url + "  " + message);//message 是 alert 方法中参数的值，即弹窗显示的内容
		if (b) return super.onJsAlert(view, url, message, result);//默认为return false，此时有弹窗
		else {//拦截html中alert函数之后，我们可以在这里做任何自己的操作。我们还可以根据message来做不同的操作
			Toast.makeText(view.getContext(), "onJsAlert，此时将不会有弹窗", Toast.LENGTH_SHORT).show();
			result.confirm();//confirm()表示点击了弹出框的确定按钮，cancel()则表示点击了弹出框的取消按钮。两者必须调用一个
			return true;//如果return true，不弹出对话框了；否则，会继续弹出对话框
		}
	}
	
	@Override
	public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
		boolean b = new Random().nextBoolean();
		Log.i("bqt", "【onJsConfirm】" + b + "  " + url + "  " + message);
		Toast.makeText(view.getContext(), "onJsConfirm，此时将不会有弹窗", Toast.LENGTH_SHORT).show();
		result.confirm();
		return b;//如果return true，不弹出对话框了；否则，会继续弹出对话框。默认为return false，有弹窗
	}
	
	@Override
	public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
		boolean b = new Random().nextBoolean();
		Log.i("bqt", "【onJsPrompt】" + b + "  " + url + "  " + message + "  " + defaultValue);
		Toast.makeText(view.getContext(), "亲，请输入你的昵称", Toast.LENGTH_SHORT).show();
		return super.onJsPrompt(view, url, message, defaultValue, result);//默认为return false，有弹窗
	}
	
	@Override
	public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
		boolean b = new Random().nextBoolean();
		Log.i("bqt", "【onJsBeforeUnload】" + b + "  " + url + "  " + message);
		return super.onJsBeforeUnload(view, url, message, result);
	}
	
	@Override
	public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
		Log.i("bqt", "【onConsoleMessage】" + "\nmessage=" + consoleMessage.message()
				+ "\nlineNumber=" + consoleMessage.lineNumber()
				+ "\nmessageLevel=" + consoleMessage.messageLevel() + "\nsourceId=" + consoleMessage.sourceId());
		return super.onConsoleMessage(consoleMessage);
	}
	
	@Override
	public void onReceivedTitle(WebView view, String title) {
		Log.i("bqt", "【onReceivedTitle 标题】" + title);
		activity.getTv_title().setText(title);
		super.onReceivedTitle(view, title);
	}
	
	@Override
	public void onReceivedIcon(WebView view, Bitmap icon) {
		Log.i("bqt", "【onReceivedIcon 图标】");
		activity.getIv_icon().setVisibility(View.VISIBLE);
		activity.getIv_icon().setImageBitmap(icon);
		super.onReceivedIcon(view, icon);
	}
	
	@Override
	public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
		//【url】The icon url.   【precomposed】True if the url is for a precomposed touch icon.
		Log.i("bqt", "【onReceivedTouchIconUrl 苹果图标】" + precomposed + "  " + url);
		super.onReceivedTouchIconUrl(view, url, precomposed);
	}
	//获得所有访问历史项目的列表，用于链接着色。
	
	@Override
	public void getVisitedHistory(ValueCallback<String[]> callback) {
		Log.i("bqt", "【getVisitedHistory 不知道怎么用】" + callback.toString());
		super.getVisitedHistory(callback);
	}
	
@Override
	public boolean onCreateWindow(WebView webView, boolean isDialog, boolean isUserGesture, Message resultMsg) {
		Log.i("bqt", "【onCreateWindow】 " + isDialog + "  " + isUserGesture + "\n详细信息" + resultMsg.toString());
		return super.onCreateWindow(webView, isDialog, isUserGesture, resultMsg);//默认是returns false
	}
	
	@Override
	public void onCloseWindow(WebView window) {
		Log.i("bqt", "【onCloseWindow】");
		super.onCloseWindow(window);
	}
	
	@Override
	public void onPermissionRequest(PermissionRequest request) {
		Log.i("bqt", "【onPermissionRequest】");
		//The host application must invoke grant(String[]) or deny(). If this method isn't overridden, the permission is denied拒绝.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Log.i("bqt", "Origin=" + request.getOrigin().toString() + "   Resources=" + Arrays.toString(request.getResources()));
		}
		super.onPermissionRequest(request);
	}
	
	@Override
	public void onPermissionRequestCanceled(PermissionRequest request) {
		Log.i("bqt", "【onPermissionRequestCanceled】");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Log.i("bqt", "Origin=" + request.getOrigin().toString() + "   Resources=" + Arrays.toString(request.getResources()));
		}
		super.onPermissionRequestCanceled(request);
	}
	
	@Override
	public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
		Log.i("bqt", "【onGeolocationPermissionsShowPrompt】" + origin);
		//callback只有一个方法【public void invoke(String origin, boolean allow, boolean retain)】
		super.onGeolocationPermissionsShowPrompt(origin, callback);
	}
	
	@Override
	public void onGeolocationPermissionsHidePrompt() {
		Log.i("bqt", "【onGeolocationPermissionsHidePrompt】");
		super.onGeolocationPermissionsHidePrompt();
	}
	
	@Override
	public void onRequestFocus(WebView view) {
		Log.i("bqt", "【onRequestFocus】" + (view == activity.getWebview()));
		super.onRequestFocus(view);
	}
	
	@Override
	public Bitmap getDefaultVideoPoster() {
		Log.i("bqt", "【getDefaultVideoPoster】");
		return BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher);
		//return super.getDefaultVideoPoster(); //return null;
	}
	
	private ImageView loadingView;
	
	// 视频加载时进程loading
	@Override
	public View getVideoLoadingProgressView() {
		Log.i("bqt", "【getVideoLoadingProgressView】");
		if (loadingView != null) return loadingView;
		else {
			loadingView = new ImageView(activity);
			loadingView.setImageResource(R.drawable.icon);
			return loadingView;
		}
		//else return super.getVideoLoadingProgressView();//return null;
	}
	
	//*****************                ↓↓↓↓↓↓↓↓↓↓↓↓↓↓      以下为全屏播放视频相关代码      ↓↓↓↓↓↓↓↓↓↓↓↓↓             *******************
	private View mCustomView;//onShowCustomView传过来的view，其实就是我们构造的View
	private CustomViewCallback mCustomViewCallback;//onShowCustomView传过来的callback
	private FullscreenHolder videoFullView;//全屏播放视频时的根布局
	
	// 播放网络视频时全屏会被调用的方法
	@Override
	public void onShowCustomView(View view, CustomViewCallback callback) {
		Log.i("bqt", "【onShowCustomView】" + view.getClass().getSimpleName());//FrameLayout
		if (view instanceof ViewGroup) {
			ViewGroup vp = (ViewGroup) view;
			Log.i("bqt", "【onShowCustomView】count=" + vp.getChildCount()
					+ "  type=" + vp.getChildAt(0).getClass().getSimpleName());//count=1  type=FullScreenView
		}
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		if (mCustomView == null) {
			mCustomView = view;
			mCustomViewCallback = callback;
			videoFullView = new FullscreenHolder(activity);
			videoFullView.addView(mCustomView);
			((FrameLayout) activity.getWindow().getDecorView()).addView(videoFullView);
		} else callback.onCustomViewHidden();
	}
	
	// 视频播放退出全屏会被调用的
	@Override
	public void onHideCustomView() {
		Log.i("bqt", "【onHideCustomView】");
		// 是全屏播放状态
		if (videoFullView != null && mCustomView != null && mCustomViewCallback != null) {
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			videoFullView.removeView(mCustomView);
			videoFullView.setVisibility(View.GONE);//这个不能省
			mCustomView = null;
			videoFullView = null;
			mCustomViewCallback.onCustomViewHidden();
		}
	}
	
	public boolean isOnShowCustomView() {
		return mCustomView != null;
	}
	
	//*****************                ↑↑↑↑↑↑↑↑↑↑↑↑↑↑      以上为全屏播放视频相关代码      ↑↑↑↑↑↑↑↑↑↑↑↑↑             *******************
	
	//********************************************以下为5.0以上，上传文件相关代码*******************************************
	private ValueCallback<Uri[]> mUploadMessageForAndroid5;
	public static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
	
	@Override
	public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams params) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Log.i("bqt", "【onShowFileChooser】 5.0+" + "   " + params.getMode() + "  " + params.getTitle() + "  "//Mode为0
					+ params.isCaptureEnabled() + "  " + params.getFilenameHint() + "  " + Arrays.toString(params.getAcceptTypes()));
		}
		mUploadMessageForAndroid5 = uploadMsg;
		Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
		contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
		contentSelectionIntent.setType("image/*");//文件类型
		
		Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
		chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
		chooserIntent.putExtra(Intent.EXTRA_TITLE, "图片选择");
		
		activity.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
		return true;
	}
	
	//5.0以上，上传图片成功后的回调
	public void mUploadMessageForAndroid5(Intent intent, int resultCode) {
		Log.i("bqt", "【上传图片成功后的回调】 5.0+");
		if (mUploadMessageForAndroid5 != null) {
			Uri result = (intent == null || resultCode != Activity.RESULT_OK) ? null : intent.getData();
			if (result != null) mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
			else mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
			mUploadMessageForAndroid5 = null;
		}
	}
	
	//*******************************************以下为5.0以下，上传文件相关代码***********************************************
	private ValueCallback<Uri> mUploadMessage;
	public static int FILECHOOSER_RESULTCODE = 1;
	
	//The undocumented magic method override Eclipse will swear at you if you try to put @Override here
	//undocumented：无正式文件的，无事实证明的；与…不协调，咒骂，发誓
	// For Android 3.0-
	@SuppressWarnings("unused")
	public void openFileChooser(ValueCallback<Uri> uploadMsg) {
		openFileChooserImpl(uploadMsg);
	}
	
	// For Android 3.0+
	@SuppressWarnings("unused")
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
		openFileChooserImpl(uploadMsg);
	}
	
	//For Android 4.1
	@SuppressWarnings("unused")
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
		openFileChooserImpl(uploadMsg);
	}
	
	//低版本上传文件代码
	private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
		Log.i("bqt", "【openFileChooser】");
		mUploadMessage = uploadMsg;
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType("image/*");
		activity.startActivityForResult(Intent.createChooser(i, "文件选择"), FILECHOOSER_RESULTCODE);
	}
	
	//5.0以下，上传图片成功后的回调
	public void mUploadMessage(Intent intent, int resultCode) {
		Log.i("bqt", "【上传图片成功后的回调】 5.0-");
		if (mUploadMessage != null) {
			if (intent != null && resultCode == Activity.RESULT_OK) mUploadMessage.onReceiveValue(intent.getData());
			mUploadMessage = null;
		}
	}
}