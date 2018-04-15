package com.bqt.test.wv.load;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * 演示load不同资源的方法
 */
public class WebViewLoadTestActivity extends ListActivity {
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
				"7、加载网页http资源",};
		setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(Arrays.asList(array))));
		webView = new WebView(this);
		getListView().addFooterView(webView);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String time = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss SSS", Locale.getDefault()).format(new Date());
		String data = "<html><body>包青天<p/><a href=http://www.baidu.com>百度</a></body></html>" + "<p/>" + time;
		
		b = !b;
		Toast.makeText(this, "" + b, Toast.LENGTH_SHORT).show();
		switch (position) {
			case 0://loadData时中文乱码问题，错误解决方案
				String encoding = b ? "UTF8" : "base64";//encoding参数的值，设置为null或其他任何值(除了"base64")都是没有影响的
				webView.loadData(data, "text/html", encoding);//如果不是采用的base64编码，那么绝对不可以设置为base64
				break;
			case 1://loadData时中文乱码问题，正确解决方案
				if (b) webView.loadData(data, "text/html; charset=UTF-8", "encoding可以是base64之外的任何内容");
				else webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
				break;
			case 2://演示loadDataWithBaseURL方法中，参数baseUrl的作用
				String baseUrl = b ? null : "http://img.mmjpg.com";//不设置baseUrl时，data2里面的两张图片将显示不出来
				String data2 = "这里两个图片的地址是相对路径<img src='/2017/936/5.jpg' /><p/><img src='/2015/74/35.jpg' />";
				webView.loadDataWithBaseURL(baseUrl, data2, "text/html", "utf-8", null);
				break;
			case 3://加载assets下的资源
				data = "<html><body>包青天<p/><img src='icon.jpg' /></body></html>" + "<p/>" + time;
				if (b) webView.loadUrl(URLUtils.ASSET_BASE + "h5/test.html");
				else webView.loadDataWithBaseURL(URLUtils.ASSET_BASE, data, "text/html", "utf-8", null);
				break;
			case 4://加载res/drawable 或 res/mipmap下的资源。不会区分drawable与drawable-***，但会区分drawable和mipmap
				data = "<html><body>包青天<p/><img src='drawable/icon.不会校验文件后缀名' /></body></html>" + "<p/>" + time;
				if (b) webView.loadUrl(URLUtils.RESOURCE_BASE + "mipmap/ic_launcher");//建议直接省略文件后缀名
				else webView.loadDataWithBaseURL(URLUtils.RESOURCE_BASE, data, "text/html", "utf-8", null);
				break;
			case 5://加载res/raw下的资源，和res/drawable一样，都属于Resources资源文件
				data = "<html><body>包青天<p/><img src='raw/icon.jpg' /></body></html>" + "<p/>" + time;
				if (b) webView.loadUrl(URLUtils.RESOURCE_BASE + "raw/test");
				else webView.loadDataWithBaseURL(URLUtils.RESOURCE_BASE, data, "text/html", "utf-8", null);
				break;
			case 6://加载SD卡中的资源。注意，如果提示【net::ERR_ACCESS_DENIED】，是因为没有申请权限
				data = "<html><body>包青天<p/><img src='icon.png' /></body></html>" + "<p/>" + time;
				if (b) webView.loadUrl(URLUtils.FILE_BASE_SIMPLE + "test.html");
				else webView.loadDataWithBaseURL(URLUtils.FILE_BASE_SIMPLE, data, "text/html", "utf-8", null);
				break;
			case 7://加载网页http资源
				webView.loadUrl(b ? "http://www.meituba.com/" : "http://img.mmjpg.com/2017/936/5.jpg");
				break;
		}
	}
}