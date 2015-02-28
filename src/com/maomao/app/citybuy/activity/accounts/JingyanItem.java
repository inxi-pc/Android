package com.maomao.app.citybuy.activity.accounts;

import java.io.Serializable;

public class JingyanItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String itemid;//:文章id
	private String itemtitle;//:文章标题
	private String itemurl;//:文章网址
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public String getItemtitle() {
		return itemtitle;
	}
	public void setItemtitle(String itemtitle) {
		this.itemtitle = itemtitle;
	}
	public String getItemurl() {
		return itemurl;
	}
	public void setItemurl(String itemurl) {
		this.itemurl = itemurl;
	}
	
	
}
