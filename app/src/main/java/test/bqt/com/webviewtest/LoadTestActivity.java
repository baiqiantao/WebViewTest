package test.bqt.com.webviewtest;

import android.app.ListActivity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class LoadTestActivity extends ListActivity {
	private WebView webView;
	private boolean b;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] array = {"loadData时中文乱码问题，错误解决方案",
				"loadData时中文乱码问题，正确解决方案",
				"loadDataWithBaseURL中baseUrl的作用",

				"loadUrl：打开assets和raw中的HTML文件",
				"loadUrl：打开SD卡中的HTML文件(失败)",
				"加载res下的drawable资源\n貌似放在drawable-**下的资源访问不了",
				"loadUrl：貌似res下其他类型的资源访问不了",

				"loadUrl与evaluateJavascript",};
		setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(Arrays.asList(array))));
		webView = new WebView(this);
		getListView().addFooterView(webView);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String time = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(new Date());
		String data = "<html><body>你好：<b>包青天</b> <p/>请登录</body></html>" + "<p/>" + time;
		String data2 = "这里两个图片的地址是相对路径<img src='/2015/74/33.jpg' /><p/><img src='/2015/74/35.jpg' />" + "<p/>" + time;
		String data3 = "res下的资源<p/><img src='drawable/ic_launcher.png' />" + "<p/>" + time;

		b = !b;
		switch (position) {
			case 0:
				if (b) webView.loadData(data, "text/html", "UTF8");//encoding参数的值，设置为null或其他任何值都是没有影响的。
				else webView.loadData(data, "text/html", "base64");//如果不是采用的base64编码，那么设置为【base64】会有影响
				break;
			case 1:
				if (b) webView.loadData(data, "text/html; charset=UTF-8", "同样可以设置为除base64外的任何东西");
				else webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
				break;
			case 2:
				if (b) webView.loadDataWithBaseURL(null, data2, "text/html", "utf-8", null);//这两张图片将显示不出来
				else webView.loadDataWithBaseURL("http://img.mmjpg.com", data2, "text/html", "utf-8", null);
				break;
			case 3:
				String uriStr = "android.resource://" + getPackageName() + "/" + R.raw.test;
				if (b) webView.loadUrl("file:///android_asset" + "/h5/test.html");
				else webView.loadUrl(Uri.parse(uriStr).getPath());//失败
				break;
			case 4:
				//访问失败，提示【net::ERR_ACCESS_DENIED】
				if (b) webView.loadUrl("file:///" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/h5/test.html");
				else webView.loadUrl("file:///sdcard" + "/h5/test.html");
				break;
			case 5:
				if (b) webView.loadUrl("file:///android_res/" + "drawable/icon.jpg");//貌似放在drawable-**下的资源访问不了
				else webView.loadDataWithBaseURL("file:///android_res/", data3, "text/html", "utf-8", null);
				break;
			case 6:
				//访问失败，提示【net::ERR_FILE_NOT_FOUND】
				if (b) webView.loadUrl("file:///android_res/" + "values/colors.xml");//貌似res下其他类型的资源访问不了
				else webView.loadUrl("file:///android_res/" + "layout/activity_webview.xml");
				break;
			case 7:
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					webView.evaluateJavascript("在", value -> Log.i("bqt", "【onReceiveValue】" + value));
				} else webView.loadUrl("https://www.baidu.com/");
				break;
		}
	}
}