package com.inxi.Utility.Database;

public class Record 
{
	public String messageTitle;
	public String messageTime;
	public String messageContent;
	
	public Record()
	{
		messageTitle = null;
		messageContent = null;
		messageTime = null;
	}
	
	public Record(String[] message)
	{
		this.messageTitle = message[0];
		this.messageTime = message[1];
		this.messageContent = message[2];
	}
	
	public String[] conventToArray()
	{
		String[] str = new String[3];
		str[0] = this.messageTitle;
		str[1] = this.messageTime;
		str[2] = this.messageContent;
		return str;
	}
}
