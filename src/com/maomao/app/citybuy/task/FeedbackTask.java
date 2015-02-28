package com.maomao.app.citybuy.task;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.maomao.app.citybuy.util.Constants;
import com.maomao.app.citybuy.util.HttpHelper;

/**
 * 用户反馈异步任务
 * 
 * 2014-09-19
 * 
 * @author peng
 * 
 */
public class FeedbackTask extends BaseTask {

	private String contact;
	private String content;

	public FeedbackTask(Context context, Handler handler, final String contact,
			final String content) {
		super(context, handler);
		this.contact = contact;
		this.content = content;
	}

	@Override
	protected String doHttp() throws Exception {
		Map<String, String> args = new HashMap<String, String>();
		args.put("uID", contact);
		args.put("content", content);
		return new HttpHelper().doPost(Constants.Http.FEEDBACK, args);
	}

	@Override
	protected Object parseResponseResult(JSONObject jsonObject)
			throws Exception {
		return null;
	}

}
