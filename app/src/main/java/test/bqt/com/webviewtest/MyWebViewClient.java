package test.bqt.com.webviewtest;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
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
		this.model = (model != null ? model : WebSettingsModel.newBuilder().build());
	}

	@Override

	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Log.i("bqt", "【shouldOverrideUrlLoading】" + url);
		//FIXME:某些链接在goBack时会一直跳转回到最后的连接，暂时用这个判断解决
		/*WebHistoryItem item = view.copyBackForwardList().getCurrentItem();
		if (item == null || !url.equals(item.getUrl())) view.loadUrl(url);//不去调用系统浏览器， 而是在本WebView中跳转
		else ((Activity) mProgressBar.getContext()).finish();*/

		view.loadUrl(url);//不去调用系统浏览器， 而是在本WebView中跳转
		return true;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		Log.i("bqt", "【onPageStarted】" + url);

		mProgressBar.setVisibility(model.showProgressBar ? View.VISIBLE : View.GONE);
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		Log.i("bqt", "【onPageFinished】" + url);
		mProgressBar.setVisibility(View.GONE);
		super.onPageFinished(view, url);
	}
}