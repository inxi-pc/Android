package com.maomao.app.citybuy.task;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.maomao.app.citybuy.util.Constants;
import com.maomao.app.citybuy.util.DecorateApplication;
import com.maomao.app.citybuy.util.HttpHelper;

/**
 * 咨询异步任务
 * 
 * 2014-09-18
 * 
 * @author peng
 * 
 */
public class ConsultTask extends BaseTask {

	public static final int RESPONSE_OK = 3;

	private String recevier;
	private String content;

	public ConsultTask(Context context, Handler handler, String receiver,
			String content) {
		super(context, handler);
		this.recevier = receiver;
		this.content = content;
	}

	@Override
	protected String doHttp() throws Exception {
		Map<String, String> args = new HashMap<String, String>();
		args.put("sender", DecorateApplication.getInstance().getDeviceId());
		args.put("receiver", recevier);
		args.put("content", content);
		return new HttpHelper().doPost(Constants.Http.CONSULT, args);
	}

	@Override
	protected Object parseResponseResult(JSONObject jsonObject)
			throws Exception {
		return null;
	}

	@Override
	protected int getResponseOk() {
		return RESPONSE_OK;
	}

}
