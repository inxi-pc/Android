package com.maomao.app.citybuy.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.maomao.app.citybuy.entity.Consult;
import com.maomao.app.citybuy.util.Constants;
import com.maomao.app.citybuy.util.DecorateApplication;
import com.maomao.app.citybuy.util.HttpHelper;

/**
 * 咨询历史记录异步任务
 * 
 * 2014-09-18
 * 
 * @author peng
 * 
 */
public class ConsultHistoryTask extends BaseTask {

	public ConsultHistoryTask(Context context, Handler handler) {
		super(context, handler);
	}

	@Override
	protected String doHttp() throws Exception {
		Map<String, String> args = new HashMap<String, String>();
		args.put("uID", DecorateApplication.getInstance().getDeviceId());
		return new HttpHelper().doPost(Constants.Http.CONSULT_HISTORY, args);
	}

	@Override
	protected Object parseResponseResult(JSONObject jsonObject)
			throws Exception {
		List<Consult> consults = new ArrayList<Consult>();
		JSONArray jsonArray = jsonObject.getJSONArray("data");
		for (int i = 0, j = jsonArray.length(); i < j; i++) {
			JSONObject itemJsonObject = jsonArray.getJSONObject(i);
			Consult consult = new Consult();
			consult.setContent(itemJsonObject.getString("content"));
			consult.setReceiver(itemJsonObject.getString("receiver"));
			consult.setSender(itemJsonObject.getString("sender"));
			consult.setTime(itemJsonObject.getString("chat_time"));
			consults.add(consult);
		}
		return consults;
	}

}
