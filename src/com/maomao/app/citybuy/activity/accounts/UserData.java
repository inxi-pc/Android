package com.maomao.app.citybuy.activity.accounts;

import java.io.Serializable;

public class UserData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String logintype;//": "sina",
    private String sex;//": "acc111",
    private String avatarurl;//": "token111",
    private String nickname;//": "des111",
    private String accountid;//": "exp111",
    private String address;//": "sh111",
    private String userid;
    
	public String getLoginType() {
		return logintype;
	}
	public void setLoginType(String logintype) {
		this.logintype = logintype;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAvatarurl() {
		return avatarurl;
	}
	public void setAvatarurl(String avatarurl) {
		this.avatarurl = avatarurl;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAccountid() {
		return accountid;
	}
	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
}
