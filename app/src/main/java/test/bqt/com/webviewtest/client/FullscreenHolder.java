package test.bqt.com.webviewtest.client;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import test.bqt.com.webviewtest.R;

public class FullscreenHolder extends FrameLayout {
	
	public FullscreenHolder(Context ctx) {
		super(ctx);
		setBackgroundResource(R.drawable.icon);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}
}
