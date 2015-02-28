package com.maomao.app.citybuy.entity;

import java.io.Serializable;

/**
 * 版本更新信息实体
 * 
 * 2014-09-22
 * 
 * @author peng
 * 
 */
public class UpdateInfo implements Serializable {

	private String versionName;
	private String downloadUrl;
	private String updateInfo;

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}

}
