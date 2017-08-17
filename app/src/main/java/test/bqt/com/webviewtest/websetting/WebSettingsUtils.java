package test.bqt.com.webviewtest.websetting;

import android.annotation.SuppressLint;
import android.os.Build;
import android.webkit.WebSettings;

public class WebSettingsUtils {
	/**
	 * 设置WebSettings中的参数
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public static void setWebSettings(WebSettings webSettings, WebSettingsModel model) {
		//必选的JavaScript
		webSettings.setJavaScriptEnabled(model.setJavaScriptEnabled);//允许js交互
		webSettings.setJavaScriptCanOpenWindowsAutomatically(model.setJavaScriptCanOpenWindowsAutomatically);//让JavaScript自动打开窗口
		
		//缩放
		webSettings.setSupportZoom(model.setSupportZoom);//默认值
		webSettings.setBuiltInZoomControls(model.setBuiltInZoomControls); //使用内置的缩放机制，设为false时setDisplayZoomControls无效
		webSettings.setDisplayZoomControls(model.setDisplayZoomControls);//使用内置的缩放机制时是否展示缩放控件
		
		//文字
		webSettings.setTextZoom(model.setTextZoom);//设置页面上的文本缩放百分比，默认100
		webSettings.setMinimumFontSize(model.setMinimumFontSize);//设置最小的字号，默认为8。某些情况下设置这个比设置setTextZoom更友好
		
		//布局相关
		webSettings.setUseWideViewPort(model.setUseWideViewPort);//重要！布局的宽度总是与WebView控件上的设备无关像素宽度一致
		webSettings.setLoadWithOverviewMode(model.setLoadWithOverviewMode);//允许缩小内容以适应屏幕宽度
		webSettings.setLayoutAlgorithm(model.setLayoutAlgorithm);//设置布局类型，会引起WebView重新布局
		//默认值NARROW_COLUMNS（适应内容大小），SINGLE_COLUMN（适应屏幕，内容将自动缩放）
		
		//缓存
		webSettings.setAppCacheEnabled(model.setAppCacheEnabled);//应用缓存API可用
		webSettings.setCacheMode(model.setCacheMode);//使用缓存的方式，默认值LOAD_DEFAULT，LOAD_NO_CACHE
		
		//其他
		webSettings.setDatabaseEnabled(model.setDatabaseEnabled);//数据库存储API可用
		webSettings.setDomStorageEnabled(model.setDomStorageEnabled);//DOM存储API可用
		webSettings.setSupportMultipleWindows(model.setSupportMultipleWindows);//支持多窗口
		webSettings.setGeolocationEnabled(model.setGeolocationEnabled);//定位可用。需要有定位权限
		// webview从5.0开始默认不允许混合模式，https中不能加载http资源，需要设置开启。
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) webSettings.setMixedContentMode(model.setMixedContentMode);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public static void setWebSettings(WebSettings webSettings) {
		//必选的JavaScript
		webSettings.setJavaScriptEnabled(true);//允许js交互
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//让JavaScript自动打开窗口
		
		//缩放
		webSettings.setSupportZoom(true);//默认值
		webSettings.setBuiltInZoomControls(true); //使用内置的缩放机制，设为false时setDisplayZoomControls无效
		webSettings.setDisplayZoomControls(false);//使用内置的缩放机制时是否展示缩放控件
		webSettings.setTextZoom(100);//设置页面上的文本缩放百分比，默认100
		webSettings.setMinimumFontSize(8);//设置最小的字号，默认为8。某些情况下设置这个比设置setTextZoom更友好
		
		//布局相关
		webSettings.setUseWideViewPort(true);//重要！布局的宽度总是与WebView控件上的设备无关像素宽度一致
		webSettings.setLoadWithOverviewMode(true);//允许缩小内容以适应屏幕宽度
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//设置布局类型，会引起WebView重新布局
		//默认值NARROW_COLUMNS（适应内容大小），SINGLE_COLUMN（适应屏幕，内容将自动缩放）
		
		//缓存
		webSettings.setAppCacheEnabled(true);//应用缓存API可用
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//使用缓存的方式，默认值LOAD_DEFAULT，LOAD_NO_CACHE
		
		//其他
		webSettings.setGeolocationEnabled(true);//默认值
		webSettings.setDatabaseEnabled(true);//数据库存储API可用
		webSettings.setDomStorageEnabled(true);//DOM存储API可用
		webSettings.setSupportMultipleWindows(true);//支持多窗口。如果设置为true，主程序要实现onCreateWindow
	}
}