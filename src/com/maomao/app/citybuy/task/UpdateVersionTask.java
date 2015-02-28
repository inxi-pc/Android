package com.maomao.app.citybuy.task;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.maomao.app.citybuy.entity.UpdateInfo;
import com.maomao.app.citybuy.util.Constants;
import com.maomao.app.citybuy.util.HttpHelper;

/**
 * 版本更新异步任务
 * 
 * 2014-09-22
 * 
 * @author peng
 * 
 */
public class UpdateVersionTask extends BaseTask {

	public static final int RESPONSE_OK = 3;

	public UpdateVersionTask(Context context, Handler handler) {
		super(context, handler);
	}

	@Override
	protected String doHttp() throws Exception {
		return new HttpHelper().doGet(Constants.Http.UPDATE_VERSION, null);
	}

	@Override
	protected Object parseResponseResult(JSONObject jsonObject)
			throws Exception {
		JSONObject dataJsonObject = jsonObject.getJSONArray("data")
				.getJSONObject(0);
		if (isNewVersion(dataJsonObject.getString("cur_version"))) {
			UpdateInfo updateInfo = new UpdateInfo();
			updateInfo.setDownloadUrl(dataJsonObject.getString("href"));
			updateInfo.setUpdateInfo(dataJsonObject.getString("description"));
			updateInfo.setVersionName(dataJsonObject.getString("cur_version"));
			return updateInfo;
		}
		return null;
	}

	@Override
	protected int getResponseOk() {
		return RESPONSE_OK;
	}

	private Boolean isNewVersion(final String versionName) throws Exception {
		String currentVersionName = getPackageVersionName();
		if (Integer.parseInt(currentVersionName.replaceAll("\\.", "")) < Integer
				.parseInt(versionName.replaceAll("\\.", "")))
			return true;
		return false;
	}

	private String getPackageVersionName() throws Exception {
		return context.getPackageManager().getPackageInfo(
				context.getPackageName(), 0).versionName;
	}
}
