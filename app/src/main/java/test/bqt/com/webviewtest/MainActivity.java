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

public class MainActivity extends ListActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] array = {"通过WebViewModel设置启动WebViewActivity时的参数",
				"",
				"",
				"",
				"",
				"",};
		setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(Arrays.asList(array))));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
			case 0:
				startActivity(new Intent(this, WebViewTestActivity.class));
				break;
			case 1:
				WebViewActivity.start(this, WebViewModel.newBuilder().title("包青天").url("百度搜索包青天").build());
				break;
			case 2:
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.cnblogs.com/baiqiantao/"));
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addCategory(Intent.CATEGORY_BROWSABLE);
				intent.putExtra(WebViewActivity.WEB_MODEL, WebViewModel.newBuilder().build());
				startActivity(intent);
				printIntentInfo(intent);
				break;
			case 3:

				break;
			case 4:

				break;
		}
	}

	private void printIntentInfo(Intent intent) {
		Log.i("bqt", "WEB_TITLE=" + intent.getParcelableExtra(WebViewActivity.WEB_MODEL)//
				+ "\nUri=" + intent.getData()//Uri=http://www.cnblogs.com/baiqiantao/
				+ "\nScheme=" + intent.getScheme()//Scheme=http
				+ "\nType=" + intent.getType()//Type=null
				+ "\nFlags=" + intent.getFlags()//Flags=0
				+ "\nPackage=" + intent.getPackage()//Package=null
				+ "\nAction=" + intent.getAction()//Action=android.intent.action.VIEW
				+ "\nCategory=" + intent.getCategories());//Category={android.intent.category.DEFAULT,//android.intent.category.BROWSABLE}
	}
}