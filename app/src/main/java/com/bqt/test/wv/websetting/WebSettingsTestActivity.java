package com.bqt.test.wv.websetting;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.bqt.test.wv.WebViewActivity;

/**
 * 演示通过WebSettingsModel设置参数启动WebViewActivity
 */
public class WebSettingsTestActivity extends ListActivity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] array = {"参数传为null，这时使用的是WebViewModel.Builder中的默认设置",
				"是否显示ProgressBar：showCenterPB、showHorizontalPB",
				
				"设置WebSettings的是否支持缩放：setBuiltInZoomControls 和 setDisplayZoomControls",
				"设置WebSettings的显示模式：setUseWideViewPort 和 setLoadWithOverviewMode",
				
				"设置WebSettings的字体缩放：setTextZoom",
				"设置WebSettings的最小字体：setMinimumFontSize",};
		setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(Arrays.asList(array))));
	}
	
	private boolean b;
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		WebSettingsModel model;
		b = !b;
		switch (position) {
			case 1:
				model = WebSettingsModel.newBuilder().title("百度").showCenterPB(b).showHorizontalPB(!b)
						.url("https://www.baidu.com/").build();
				break;
			case 2:
				model = WebSettingsModel.newBuilder().title("电影天堂").setBuiltInZoomControls(b).url("http://www.dy2018.com/")
						.setDisplayZoomControls(false).build();//是否展示缩放控件。这个控件非常丑，不要显示！
				break;
			case 3:
				model = WebSettingsModel.newBuilder().title("电影天堂").setUseWideViewPort(true).url("http://www.dy2018.com/")
						.setLoadWithOverviewMode(b).build();//setUseWideViewPort为true时才有效
				break;
			case 4:
				int textSize = 50 + 200 * new Random().nextInt(3);
				model = WebSettingsModel.newBuilder().title("微信开放平台").setUseWideViewPort(true).setLoadWithOverviewMode(true)
						.setTextZoom(textSize).url("https://open.weixin.qq.com/cgi-bin/frame?t=news/protocol_developer_tmpl").build();
				break;
			case 5:
				int miniFontSize = 10 + 10 * new Random().nextInt(3);
				model = WebSettingsModel.newBuilder().title("安卓开发者").setUseWideViewPort(true).setLoadWithOverviewMode(true)
						.setMinimumFontSize(miniFontSize).url("https://developer.android.google.cn/guide/index.html").build();
				break;
			default:
				model = WebSettingsModel.newBuilder().build();
				break;
		}
		WebViewActivity.start(this, model);
	}
}