package test.bqt.com.webviewtest.websetting;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class JSBridgeTestActivity extends ListActivity {
	private WebView webView;
	private boolean b;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] array = {"",
				"",
				"",
				"",
				"",
				"",};
		setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(Arrays.asList(array))));
		webView = new WebView(this);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		getListView().addFooterView(webView);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String time = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss SSS", Locale.getDefault()).format(new Date());
		switch (position) {
			case 0:

				break;
			case 1:

				break;
			case 2:

				break;
			case 3:

				break;
			case 4:

				break;
		}
	}
}