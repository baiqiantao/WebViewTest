package test.bqt.com.webviewtest;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import test.bqt.com.webviewtest.websetting.WebSettingsModel;
import test.bqt.com.webviewtest.websetting.WebSettingsTestActivity;

public class MainActivity extends ListActivity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] array = {"通过WebSettingsModel设置启动WebViewActivity时的参数",
				"通过浏览器打开",
				"测试load方法",
				"",
				"",
				"",};
		setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>(Arrays.asList(array))));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
			case 0:
				startActivity(new Intent(this, WebSettingsTestActivity.class));
				break;
			case 1:
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dy2018.com/"));
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addCategory(Intent.CATEGORY_BROWSABLE);
				intent.putExtra(WebViewActivity.WEB_MODEL, WebSettingsModel.newBuilder()
						.title("无效参数")
						.build());//这个参数会丢失
				startActivity(intent);
				printIntentInfo(intent);
				break;
			case 2:
				startActivity(new Intent(this, LoadTestActivity.class));
				break;
			case 3:

				
				break;
			case 4:
				
				break;
		}
	}
	
	private void printIntentInfo(Intent intent) {
		Log.i("bqt", "传入的数据=" + intent.getParcelableExtra(WebViewActivity.WEB_MODEL)//
				+ "\nUri=" + intent.getData()//http://www.dy2018.com/
				+ "\nScheme=" + intent.getScheme()//Scheme=http
				+ "\nType=" + intent.getType()//Type=null
				+ "\nFlags=" + intent.getFlags()//Flags=0
				+ "\nPackage=" + intent.getPackage()//Package=null
				+ "\nAction=" + intent.getAction()//Action=android.intent.action.VIEW
				+ "\nCategory=" + intent.getCategories());//Category={android.intent.category.DEFAULT, android.intent.category.BROWSABLE}
	}
}