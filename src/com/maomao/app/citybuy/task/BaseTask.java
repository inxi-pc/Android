package com.maomao.app.citybuy.task;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.maomao.app.citybuy.R;
import com.maomao.app.citybuy.util.LogUtil;

/**
 * 2014-09-18
 * 
 * @author peng
 * 
 */
public abstract class BaseTask extends AsyncTask<Void, Void, Void> {

	public static final int RESPONSE_ERROR = 0;
	public static final int RESPONSE_OK = 1;
	private static final int HTTP_RESPONSE_OK = 0;

	protected Context context;
	protected Handler handler;

	public BaseTask(final Context context, final Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	@Override
	protected Void doInBackground(Void... params) {
		Message message = handler.obtainMessage();
		try {
			String responseResult = doHttp();
			LogUtil.D("-----" + getClass().getSimpleName()
					+ " responseResult-----" + responseResult);
			JSONObject jsonObject = new JSONObject(responseResult);
			if (jsonObject.getInt("errorNo") == HTTP_RESPONSE_OK) {
				message.what = getResponseOk();
				message.obj = parseResponseResult(jsonObject);
			} else {
				message.what = RESPONSE_ERROR;
				message.obj = jsonObject.getString("errorMsg");
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.what = RESPONSE_ERROR;
			message.obj = context.getString(R.string.common_network_exception);
		}
		handler.sendMessage(message);
		return null;
	}

	protected abstract String doHttp() throws Exception;

	protected abstract Object parseResponseResult(final JSONObject jsonObject)
			throws Exception;

	protected int getResponseOk() {
		return RESPONSE_OK;
	}

}
