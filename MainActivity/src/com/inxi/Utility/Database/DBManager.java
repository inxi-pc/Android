package com.inxi.Utility.Database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager 
{
	
	private static String DATABASE_NAME="test.db";
	private static int  DATABASE_VARSION=1;
	private DBHelper helper;
	private SQLiteDatabase db;
	
	public DBManager(Context context)
	{ 
		helper = new DBHelper(context,DATABASE_NAME,null,DATABASE_VARSION);
		db=helper.getWritableDatabase();
	}
	
	public void OpenDB()
	{
		db = helper.getWritableDatabase();
	}
	
	public List<Record> query()
	{
		if(!db.isOpen())
		{	
			OpenDB();
		}
		
		ArrayList<Record> listMessage=new ArrayList<Record>();
		Cursor c=getDatabaseCursor();
		
		while(c.moveToNext()) 
		{
			Record tRecord=new Record();
			tRecord.messageTitle = c.getString(c.getColumnIndex("messageTitle"));
			tRecord.messageTime = c.getString(c.getColumnIndex("messageTime"));
			tRecord.messageContent = c.getString(c.getColumnIndex("messageContent"));
			listMessage.add(tRecord);
		}
		return listMessage;
	}
	
	public List<Record> query(String sql,String[] args)
	{
		if(!db.isOpen())
		{	
			OpenDB();
		}
		
		ArrayList<Record> listMessage=new ArrayList<Record>();
		Cursor c=getDatabaseCursor(sql,args);
		
		while(c.moveToNext())
		{
			Record tRecord=new Record();
			tRecord.messageTitle = c.getString(c.getColumnIndex("messageTitle"));
			tRecord.messageTime = c.getString(c.getColumnIndex("messageTime"));
			tRecord.messageContent = c.getString(c.getColumnIndex("messageContent"));
			listMessage.add(tRecord);
		}
		return listMessage;
	}
	
	private Cursor getDatabaseCursor()
	{
		Cursor c=db.rawQuery("select * from tMessage",null);
		return c;
	}
	
	private Cursor getDatabaseCursor(String sql,String[] args)
	{
		Cursor c = db.rawQuery(sql, args);
		return c;
	}
	
	public synchronized void insert(List<Record> listMessage)
	{
		for(Record tMessage : listMessage)
		{
			db.beginTransaction();
			db.execSQL("insert into tMessage values(?,?,?,?)",new Object[]{null,tMessage.messageTitle,tMessage.messageContent,
					tMessage.messageTime});
			db.setTransactionSuccessful();
		}
		db.endTransaction();
	}
	
	public void update()
	{
		
	}
	
	public void delete()
	{
		
	}

	public void closeDB()
	{
		db.close();
	}

}
