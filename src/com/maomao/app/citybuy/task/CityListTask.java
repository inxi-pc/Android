package com.maomao.app.citybuy.task;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.maomao.app.citybuy.entity.City;
import com.maomao.app.citybuy.util.Constants;
import com.maomao.app.citybuy.util.HttpHelper;

/**
 * 团购城市列表异步任务
 * 
 * 2014-09-18
 * 
 * @author peng
 * 
 */
public class CityListTask extends BaseTask {

	public CityListTask(Context context, Handler handler) {
		super(context, handler);
	}

	@Override
	protected String doHttp() throws Exception {
		return new HttpHelper().doGet(Constants.Http.CITY_LIST, null);
	}

	@Override
	protected Object parseResponseResult(JSONObject jsonObject)
			throws Exception {
		List<City> cityLists = new ArrayList<City>();
		JSONArray jsonArray = jsonObject.getJSONArray("data");
		for (int i = 0, j = jsonArray.length(); i < j; i++) {
			JSONObject itemJsonObject = jsonArray.getJSONObject(i);
			City city = new City();
			city.setName(itemJsonObject.getString("cname"));
			city.setPinyin(itemJsonObject.getString("cnickname"));
			cityLists.add(city);
		}
		return cityLists;
	}

}
