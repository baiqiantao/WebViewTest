package test.bqt.com.webviewtest;

import android.app.ListActivity;
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
		String[] array = {"0、loadData时中文乱码问题，错误解决方案",
				"1、loadData时中文乱码问题，正确解决方案",
				"2、演示loadDataWithBaseURL方法中，参数baseUrl的作用",
				"3、加载assets下的资源",
				"4、加载res/drawable下的资源",
				"5、加载res/raw下的资源",
				"6、加载SD卡中的资源",
				"7、loadUrl与evaluateJavascript",};
		setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(Arrays.asList(array))));
		webView = new WebView(this);
		getListView().addFooterView(webView);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String time = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss SSS", Locale.getDefault()).format(new Date());
		String data = "<html><body>你好：<b>包青天</b> <p/>请登录</body></html>" + "<p/>" + time;

		b = !b;
		switch (position) {
			case 0://loadData时中文乱码问题，错误解决方案
				String encoding = b ? "UTF8" : "base64";//encoding参数的值，设置为null或其他任何值（除了"base64"）都是没有影响的
				webView.loadData(data, "text/html", encoding);//如果不是采用的base64编码，那么绝不能设置为【base64】
				break;
			case 1://loadData时中文乱码问题，正确解决方案
				if (b) webView.loadData(data, "text/html; charset=UTF-8", "同样可以设置为除base64外的任何东西");
				else webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
				break;
			case 2://演示loadDataWithBaseURL方法中，参数baseUrl的作用
				String baseUrl = b ? null : "http://img.mmjpg.com";
				String data2 = "这里两个图片的地址是相对路径<img src='/2015/74/33.jpg' /><p/><img src='/2015/74/35.jpg' />";
				webView.loadDataWithBaseURL(baseUrl, data2, "text/html", "utf-8", null);//不设置baseUrl时，这两张图片将显示不出来
				break;
			case 3://加载assets下的资源
				// to refer to bar.png under your package's asset/foo/ directory, use"file:///android_asset/foo/bar.png".
				String ASSET_BASE = "file:///android_asset/";//参见URLUtil.ASSET_BASE
				if (b) webView.loadUrl(ASSET_BASE + "h5/test.html");
				else webView.loadDataWithBaseURL(ASSET_BASE, "<img src='icon.jpg' />", "text/html", "utf-8", null);
				break;
			case 4://加载res/drawable下的资源
				// to refer to bar.png under your package's res/drawable/ directory, use "file:///android_res/drawable/bar.png".
				// Use "drawable" to refer to "drawable-hdpi" directory as well.
				String RESOURCE_BASE = "file:///android_res/";//参见URLUtil.RESOURCE_BASE
				if (b) webView.loadUrl(RESOURCE_BASE + "mipmap/ic_launcher.任意后缀名都可以");//res资源不会检索文件后缀名
				else webView.loadDataWithBaseURL(RESOURCE_BASE, "<img src='drawable/icon.jpg' />", "text/html", "utf-8", null);
				break;
			case 5://加载res/raw下的资源
				if (b) webView.loadUrl("file:///android_res/" + "raw/test");//res资源不会检索文件后缀名
				else webView.loadDataWithBaseURL("file:///android_res/", "<img src='raw/icon.jpg' />", "text/html", "utf-8", null);
				break;
			case 6://加载SD卡中的资源
				String FILE_BASE = "file://";//参见URLUtil.FILE_BASE。如果提示【net::ERR_ACCESS_DENIED】，是没有申请权限
				String file = FILE_BASE + Environment.getExternalStorageDirectory().getAbsolutePath() + "/h5/test.html";
				if (b) webView.loadUrl(file);//【file:///storage/emulated/0/h5/test.html】或【file:///"sdcard/h5/test.html】
				else webView.loadDataWithBaseURL(FILE_BASE, "<img src='sdcard/Pictures/icon.jpg' />", "text/html", "utf-8", null);
				break;
			case 7:
				if (b) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
						webView.evaluateJavascript("",
								value -> Log.i("bqt", "【onReceiveValue】" + value));
					} else webView.loadUrl("https://www.baidu.com/");
				}
				break;
		}
	}
}