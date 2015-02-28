package com.maomao.app.citybuy.entity;

import java.io.Serializable;

/**
 * 首页轮播图实体
 * 
 * 2014-09-18
 * 
 * @author peng
 * 
 */
public class Banner implements Serializable {

	private String imagePath;
	private String href;

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

}
