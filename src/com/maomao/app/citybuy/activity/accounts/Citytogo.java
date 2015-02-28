package com.maomao.app.citybuy.activity.accounts;

import java.io.Serializable;

public class Citytogo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String commentid;//:团购id
	private String id;//:团购id
	private String smallurl;//:图片小图地址
	private String largeurl;//:图片大图地址
	private String title;//：标题
	private String longtitle;//: 详情页面标题
	private String subtitle;//：简介
	private String simpledate;//：日期，格式是：3月2日
	private int type;//: 1:建材,2:家具 3:家电 4:专场,5:联盟 ， 建材排第一个 由服务端控制
	private String time;//：团购时间
	private String address;//：团购地点
	private String lat;//：纬度
	private String lng;//：经度
	private String description;//：团购详情
	private String processimageurl;//:团购流程图片网址
	private String tgUrl;
	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLongtitle() {
		return longtitle;
	}
	public void setLongtitle(String longtitle) {
		this.longtitle = longtitle;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getSimpledate() {
		return simpledate;
	}
	public void setSimpledate(String simpledate) {
		this.simpledate = simpledate;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProcessimageurl() {
		return processimageurl;
	}
	public void setProcessimageurl(String processimageurl) {
		this.processimageurl = processimageurl;
	}
	public String getCommentid() {
		return commentid;
	}
	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}
	public String getTgUrl() {
		return tgUrl;
	}
	public void setTgUrl(String tgUrl) {
		this.tgUrl = tgUrl;
	}
	
	
}
