package com.maomao.app.citybuy.util;

import android.util.Log;

public class LogUtil {

	private static final Boolean isShowLog = false;
	public static final String TAG = "com.maomao.app.citybuy";

	public static void D(final String msg) {
		if (isShowLog)
			Log.d(TAG, msg);
	}

}
