package com.maomao.app.citybuy.util;

import java.io.File;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.telephony.TelephonyManager;

import com.maomao.app.citybuy.entity.City;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class DecorateApplication extends Application {

	private static DecorateApplication decorateApplication;
	private static ImageLoader imageLoader;
	private static DisplayImageOptions displayImageOptions;

	private List<City> cityLists;
	private String locationCity;

	@Override
	public void onCreate() {
		super.onCreate();
		decorateApplication = this;
		initImageLoader();
	}

	public static DecorateApplication getInstance() {
		return decorateApplication;
	}

	public static DisplayImageOptions getDisplayImageOptions() {
		return displayImageOptions;
	}

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	private void initImageLoader() {
		imageLoader = ImageLoader.getInstance();
		File cacheDir = StorageUtils.getCacheDirectory(this);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().discCache(new UnlimitedDiscCache(cacheDir))
				.build();
		imageLoader.init(config);
		displayImageOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	public String getLocationCity() {
		return locationCity;
	}

	public void setCityLists(List<City> cityLists) {
		this.cityLists = cityLists;
	}

	public List<City> getCityLists() {
		return cityLists;
	}

	public String getDeviceId() {
		return ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
				.getDeviceId();
	}

}
