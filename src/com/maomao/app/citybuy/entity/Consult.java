package com.maomao.app.citybuy.entity;

import java.io.Serializable;

/**
 * 咨询实体
 * 
 * 2014-09-18
 * 
 * @author peng
 * 
 */
public class Consult implements Serializable {

	private String sender;
	private String receiver;
	private String content;
	private String time;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
