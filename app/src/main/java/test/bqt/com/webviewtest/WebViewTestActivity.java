package test.bqt.com.webviewtest;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class WebViewTestActivity extends ListActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] array = {"参数传为null，这时使用的是WebViewModel.Builder中的默认设置",
				"搜索白乾涛（是否显示ProgressBar）",
				"使用GitHub搜索指定的项目",
				"设置WebSettings的部分参数",
				"",
				"",};
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
				WebViewActivity.start(this, WebViewModel.newBuilder()
						.title("是否显示ProgressBar")
						.showProgressBar(b)
						.url("白乾涛").build());
				break;
			case 2:
				WebViewActivity.start(this, WebViewModel.newBuilder()
						.title("使用GitHub搜索指定的项目")
						.searchType("github")
						.url("MyViews").build());
				break;
			case 3:

				break;
			case 4:

				break;
		}
	}
}