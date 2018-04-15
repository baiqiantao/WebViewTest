package com.bqt.test.wv;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bqt.test.wv.load.WebViewLoadTestActivity;
import com.bqt.test.wv.websetting.WebSettingsModel;
import com.bqt.test.wv.websetting.WebSettingsTestActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends ListActivity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] array = {"演示WebSettings中的API及对WebSettings的封装",
				"演示通过隐式意图打开网页URL",
				"测试加载网页、加载资源的各种方法",
				"WebChromeClient重用功能演示",
				"",
				"",};
		setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>(Arrays.asList(array))));
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
			case 0:
				startActivity(new Intent(this, WebSettingsTestActivity.class));
				break;
			case 1:
				launchByImplicitIntent();
				break;
			case 2:
				startActivity(new Intent(this, WebViewLoadTestActivity.class));
				break;
			case 3:
				WebViewActivity.start(this, WebSettingsModel.newBuilder()
						.url("file:///android_asset/h5/JS交互演示.html").setJavaScriptEnabled(true).build());
				break;
			case 4:
				WebViewActivity.start(this, WebSettingsModel.newBuilder()
						.url("file:///android_asset/h5/WebChromeClient演示.html")
						.setJavaScriptEnabled(true)
						.setDomStorageEnabled(true)//这句话必须保留，否则无法播放优酷网页视频，其他的可以
						.setSupportMultipleWindows(true)
						.setGeolocationEnabled(true)
						.build());
				break;
			case 5:
				startActivity(new Intent(this, HandlerTestActivity.class));
				break;
		}
	}
	
	private void launchByImplicitIntent() {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dy2018.com/"));
		intent.addCategory(Intent.CATEGORY_DEFAULT);//可省略
		intent.addCategory(Intent.CATEGORY_BROWSABLE);//可省略
		startActivity(intent);
		Log.i("bqt", "Uri=" + intent.getData()//http://www.dy2018.com/
				+ "\nScheme=" + intent.getScheme()//Scheme=http
				+ "\nType=" + intent.getType()//Type=null
				+ "\nFlags=" + intent.getFlags()//Flags=0
				+ "\nPackage=" + intent.getPackage()//Package=null
				+ "\nAction=" + intent.getAction()//Action=android.intent.action.VIEW
				+ "\nCategory=" + intent.getCategories());//Category={android.intent.category.DEFAULT, android.intent.category.BROWSABLE}
	}
}