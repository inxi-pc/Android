package com.maomao.app.citybuy.task;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.maomao.app.citybuy.entity.Banner;
import com.maomao.app.citybuy.util.Constants;
import com.maomao.app.citybuy.util.HttpHelper;

/**
 * 首页轮播图异步任务
 * 
 * 2014-09-18
 * 
 * @author peng
 * 
 */
public class HomeBannerTask extends BaseTask {

	public HomeBannerTask(Context context, Handler handler) {
		super(context, handler);
	}

	@Override
	protected String doHttp() throws Exception {
		return new HttpHelper().doGet(Constants.Http.HOME_BANNER, null);
	}

	@Override
	protected Object parseResponseResult(JSONObject jsonObject)
			throws Exception {
		List<Banner> banners = new ArrayList<Banner>();
		JSONArray jsonArray = jsonObject.getJSONArray("data");
		for (int i = 0, j = jsonArray.length(); i < j; i++) {
			JSONObject itemJsonObject = jsonArray.getJSONObject(i);
			Banner banner = new Banner();
			banner.setHref(itemJsonObject.getString("bhref"));
			banner.setImagePath(itemJsonObject.getString("ori_path"));
			banners.add(banner);
		}
		return banners;
	}

}
