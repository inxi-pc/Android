package com.maomao.app.citybuy.entity;

import java.io.Serializable;

/**
 * 团购城市实体
 * 
 * 2014-09-18
 * 
 * @author peng
 * 
 */
public class City implements Serializable {

	private String name;
	private String pinyin;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

}
