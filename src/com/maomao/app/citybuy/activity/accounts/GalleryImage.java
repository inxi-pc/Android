package com.maomao.app.citybuy.activity.accounts;

import java.io.Serializable;

public class GalleryImage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;//:图片id
	private String smallurl;//:图片小图地址
	private String largeurl;//:图片大图地址
	private int likes;//:图片收藏数
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSmallurl() {
		return smallurl;
	}
	public void setSmallurl(String smallurl) {
		this.smallurl = smallurl;
	}
	public String getLargeurl() {
		return largeurl;
	}
	public void setLargeurl(String largeurl) {
		this.largeurl = largeurl;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	
	
}
