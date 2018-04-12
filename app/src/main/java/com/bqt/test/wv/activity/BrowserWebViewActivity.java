package com.bqt.test.wv.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * 用于通过隐式意图打开网页的Activity
 */
public class BrowserWebViewActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (getIntent() != null && getIntent().getDataString() != null) {
			WebView webview = new WebView(this);
			setContentView(webview);
			String url = getIntent().getDataString();
			Log.i("bqt", "【要加载的url】" + url);
			webview.loadUrl(url);
		} else {
			Toast.makeText(this, "没有要加载的网页", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
}