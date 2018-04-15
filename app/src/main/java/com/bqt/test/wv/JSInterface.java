package com.bqt.test.wv;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class JSInterface {
	public static final String JS_INTERFACE_NAME = "JSInterface";//JS调用类名
	private Context mContext;
	private WebView webView;
	
	public JSInterface(Context context, WebView webView) {
		this.mContext = context;
		this.webView = webView;
	}
	
	@JavascriptInterface
	public void hello(String content) {
		Log.i("bqt", "JS 调用原生时是否发生在主线程：" + (Looper.myLooper() == Looper.getMainLooper()));//false
		new Handler(Looper.getMainLooper()).post(() -> //WebView等UI操作必须在主线程中进行
				Toast.makeText(mContext, "原生的hello方法被调用了：" + content, Toast.LENGTH_SHORT).show());
		
		SystemClock.sleep(3000);//模拟耗时操作
		
		String call = "javascript:javacalljs(" + System.currentTimeMillis() + ")";//格式很重要，大部分错误都是由于格式问题导致的
		new Handler(Looper.getMainLooper()).post(() -> webView.loadUrl(call));//WebView等UI操作必须在主线程中进行
	}
	
	@JavascriptInterface
	public void hello2(String content) {
		new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show());
		
		SystemClock.sleep(3000);//模拟耗时操作
		
		String call = "javascript:javacalljs2(" + System.currentTimeMillis() + ")";//JS此方法的返回值会通过onReceiveValue回调到原生
		new Handler(Looper.getMainLooper()).post(() -> webView.evaluateJavascript(call, value -> {
			Log.i("bqt", "ValueCallback 是否发生在主线程：" + (Looper.myLooper() == Looper.getMainLooper()));//true
			Toast.makeText(mContext, "【onReceiveValue】" + value, Toast.LENGTH_SHORT).show();
		}));
	}
}