package com.bqt.test.wv.load;

import android.os.Environment;

/**
 * 里面的内容基本都来自 {@link android.webkit.URLUtil}
 */
public class URLUtils {
	// to refer to bar.png under your package's asset/foo/ directory, use "file:///android_asset/foo/bar.png".
	public static final String ASSET_BASE = "file:///android_asset/";
	// to refer to bar.png under your package's res/drawable/ directory, use "file:///android_res/drawable/bar.png".
	// Use "drawable" to refer to"drawable-hdpi" directory as well.
	public static final String RESOURCE_BASE = "file:///android_res/";
	public static final String FILE_BASE = "file://";
	public static final String FILE_BASE_SIMPLE = "file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/";//file:///sdcard/
	public static final String FILE_BASE_SIMPLE2 = "file:///storage/emulated/0/";
	public static final String PROXY_BASE = "file:///cookieless_proxy/";
	public static final String CONTENT_BASE = "content:";
}
