package com.maomao.app.citybuy.activity.accounts;

import java.io.Serializable;


public class DiaryInfoCaogao implements Serializable{

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String diaryid;
	  private String datetime;
	  private String text;
	  private byte[] image;
	public String getDiaryid() {
		return diaryid;
	}
	public void setDiaryid(String diaryid) {
		this.diaryid = diaryid;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	
}
