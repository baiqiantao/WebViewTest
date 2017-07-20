package test.bqt.com.webviewtest;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MyWebViewClient extends WebViewClient {
	private ProgressBar mProgressBar;
	private WebViewModel model;

	public MyWebViewClient(ProgressBar mProgressBar, WebViewModel model) {
		super();
		this.mProgressBar = mProgressBar;
		this.model = (model != null ? model : WebViewModel.newBuilder().build());
	}

	@Override
	//打开网页时不调用系统浏览器， 而是在本WebView中显示
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		mProgressBar.setVisibility(model.showProgressBar ? View.VISIBLE : View.GONE);
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		mProgressBar.setVisibility(View.GONE);
		super.onPageFinished(view, url);
	}
}