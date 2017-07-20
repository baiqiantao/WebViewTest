package test.bqt.com.webviewtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 在JS中可以调用此类中的方法
 */
public class WebAppinterface {
	private Activity mActivity;

	public WebAppinterface(Activity context) {
		this.mActivity = context;
	}

	public void recharge(int vipType) {
		Intent intent = new Intent(mActivity, Activity.class);
		Bundle mBundle = new Bundle();
		mBundle.putInt("item", vipType - 1);
		mActivity.startActivity(intent, mBundle);
	}
}