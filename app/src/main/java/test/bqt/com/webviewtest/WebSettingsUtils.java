package test.bqt.com.webviewtest;

import android.annotation.SuppressLint;
import android.webkit.WebSettings;

public class WebSettingsUtils {

	@SuppressLint("SetJavaScriptEnabled")
	public static void setWebSettings(WebSettings webSettings) {
		//必选的JavaScript
		webSettings.setJavaScriptEnabled(true);//允许js交互
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//让JavaScript自动打开窗口

		//布局相关
		webSettings.setBuiltInZoomControls(true); //使用内置的缩放机制，设为false时setDisplayZoomControls无效
		webSettings.setDisplayZoomControls(false);//使用内置的缩放机制时是否展示缩放控件
		webSettings.setUseWideViewPort(true);//重要！布局的宽度总是与WebView控件上的设备无关像素宽度一致
		webSettings.setLoadWithOverviewMode(true);//允许缩小内容以适应屏幕宽度
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//设置布局类型，会引起WebView重新布局
		//默认值NARROW_COLUMNS（适应内容大小），SINGLE_COLUMN（适应屏幕，内容将自动缩放）

		//缓存
		webSettings.setAppCacheEnabled(true);//应用缓存API可用
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//使用缓存的方式，默认值LOAD_DEFAULT，LOAD_NO_CACHE

		//其他
		webSettings.setDatabaseEnabled(true);//数据库存储API可用
		webSettings.setDomStorageEnabled(true);//DOM存储API可用
		webSettings.setSupportMultipleWindows(true);//支持多窗口。如果设置为true，主程序要实现onCreateWindow
	}

	@SuppressLint("SetJavaScriptEnabled")
	public static void setWebSettings2(WebSettings webSettings) {
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setJavaScriptEnabled(true);// 允许js交互
		webSettings.setGeolocationEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setDisplayZoomControls(false);
		webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
		webSettings.setSupportZoom(true); // 支持缩放
		webSettings.setLoadWithOverviewMode(true);
	}
}