package com.maomao.app.citybuy.activity.accounts;

import java.io.Serializable;

public class DiaryInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String titledate;
	private String largeurl;
	private String smallurl;
	private String title; 
	private String subdate;
	private String diaryid;
	private int likes;
	private int comments;
	public String getTitledate() {
		return titledate;
	}
	public void setTitledate(String titledate) {
		this.titledate = titledate;
	}
	public String getLargeurl() {
		return largeurl;
	}
	public void setLargeurl(String largeurl) {
		this.largeurl = largeurl;
	}
	public String getSmallurl() {
		return smallurl;
	}
	public void setSmallurl(String smallurl) {
		this.smallurl = smallurl;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubdate() {
		return subdate;
	}
	public void setSubdate(String subdate) {
		this.subdate = subdate;
	}
	public String getDiaryid() {
		return diaryid;
	}
	public void setDiaryid(String diaryid) {
		this.diaryid = diaryid;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getComments() {
		return comments;
	}
	public void setComments(int comments) {
		this.comments = comments;
	}
	
	

}
