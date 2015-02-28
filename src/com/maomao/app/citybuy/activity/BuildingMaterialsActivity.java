package com.maomao.app.citybuy.activity;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.maomao.app.citybuy.R;
import com.maomao.app.citybuy.entity.City;
import com.maomao.app.citybuy.task.BaseTask;
import com.maomao.app.citybuy.task.CityListTask;
import com.maomao.app.citybuy.util.Constants;
import com.maomao.app.citybuy.util.DecorateApplication;
import com.maomao.app.citybuy.util.LogUtil;

/**
 * 装修建材界面
 * 
 * 2014-08-27
 * 
 * @author peng
 * 
 */
public class BuildingMaterialsActivity extends BaseActivity {

	private LocationClient locationClient;
	private CityListTask cityListTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.building_materials);
		initTitleView(R.string.home_building_materiials, true, false);
		getCityListData();
	}

	@Override
	protected void onDestroy() {
		if (cityListTask != null && !cityListTask.isCancelled())
			cityListTask.cancel(true);
		super.onDestroy();
	}

	private void getCityListData() {
		if (DecorateApplication.getInstance().getCityLists() == null) {
			cityListTask = new CityListTask(this, handler);
			cityListTask.execute();
		} else {
			getLocationCity();
		}
	}

	private void getLocationCity() {
		if (TextUtils.isEmpty(DecorateApplication.getInstance()
				.getLocationCity())) {
			initLocationClient();
		} else {
			initWebView(getWebViewLoadUrl());
		}
	}

	private String getWebViewLoadUrl() {
		List<City> cityList = DecorateApplication.getInstance().getCityLists();
		String locationCity = DecorateApplication.getInstance()
				.getLocationCity();
		City city = null;
		if (locationCity != null) {
			for (City c : cityList) {
				if (locationCity.contains(c.getName())) {
					city = c;
					break;
				}
			}
		}
		if (city == null) {
			return Constants.Http.BUILDING_MATERIALS;
		} else {
			return "http://m." + city.getPinyin() + ".citytogo.com.cn/";
		}
	}

	private void initLocationClient() {
		locationClient = new LocationClient(getApplicationContext());
		locationClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation bdLocation) {
				if (bdLocation == null) {
					LogUtil.D("bdLocation is null");
					initWebView(Constants.Http.BUILDING_MATERIALS);
				} else {
					DecorateApplication.getInstance().setLocationCity(
							bdLocation.getAddrStr());
					initWebView(getWebViewLoadUrl());
				}
			}
		});
		LocationClientOption locationClientOption = new LocationClientOption();
		locationClientOption.setLocationMode(LocationMode.Battery_Saving);
		locationClientOption.setCoorType("bd09ll");
		locationClientOption.setIsNeedAddress(true);
		locationClientOption.setProdName("Rect365");
		locationClient.setLocOption(locationClientOption);
		locationClient.start();
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.requestLocation();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BaseTask.RESPONSE_ERROR:
				initWebView(Constants.Http.BUILDING_MATERIALS);
				break;
			case BaseTask.RESPONSE_OK:
				DecorateApplication.getInstance().setCityLists(
						(List<City>) msg.obj);
				getLocationCity();
				break;
			}
		}
	};

}
