package test.bqt.com.webviewtest.websetting;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import test.bqt.com.webviewtest.WebViewActivity;

public class WebSettingsTestActivity extends ListActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] array = {"参数传为null，这时使用的是WebViewModel.Builder中的默认设置",
				"搜索白乾涛（是否显示ProgressBar：showCenterPB、showHorizontalPB）",
				"使用GitHub搜索指定的项目：searchType",

				"设置WebSettings的支持缩放：setBuiltInZoomControls 和 setDisplayZoomControls",
				"设置WebSettings的显示模式：setUseWideViewPort 和 setLoadWithOverviewMode",
				"设置WebSettings的字体缩放：setTextZoom",
				"设置WebSettings的最小字体：setMinimumFontSize",};
		setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(Arrays.asList(array))));
	}

	private boolean b;

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		b = !b;
		switch (position) {
			case 0:
				WebViewActivity.start(this, null);
				break;
			case 1:
				WebViewActivity.start(this, WebSettingsModel.newBuilder()
						.title("是否显示ProgressBar")
						.showCenterPB(b)
						.showHorizontalPB(!b)
						.url("白乾涛").build());
				break;
			case 2:
				WebViewActivity.start(this, WebSettingsModel.newBuilder()
						.title("使用GitHub搜索指定的项目")
						.searchType("github")
						.url("MyViews").build());
				break;
			case 3:
				WebViewActivity.start(this, WebSettingsModel.newBuilder()
						.title("电影天堂")
						.setBuiltInZoomControls(b)
						.setDisplayZoomControls(false)//是否展示缩放控件。这个控件非常丑，不要显示！
						.url("http://www.dy2018.com/").build());
				break;
			case 4:
				WebViewActivity.start(this, WebSettingsModel.newBuilder()
						.title("电影天堂")
						.setUseWideViewPort(true)
						.setLoadWithOverviewMode(b)//setUseWideViewPort为true时才有效
						.url("http://www.dy2018.com/").build());
				break;
			case 5:
				WebViewActivity.start(this, WebSettingsModel.newBuilder()
						.title("微信")
						.setUseWideViewPort(true)
						.setLoadWithOverviewMode(true)
						.setTextZoom(50 + (int) (new Random().nextFloat() * 4 * 100))//50-450
						.url("https://open.weixin.qq.com/cgi-bin/frame?t=news/protocol_developer_tmpl").build());
				break;
			case 6:
				WebViewActivity.start(this, WebSettingsModel.newBuilder()
						.title("微信")
						.setUseWideViewPort(true)
						.setLoadWithOverviewMode(true)
						.setMinimumFontSize(10 + new Random().nextInt(50))//10-60
						.url("https://open.weixin.qq.com/cgi-bin/frame?t=news/protocol_developer_tmpl").build());
				break;
		}
	}
}