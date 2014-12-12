package com.inxi.Utility.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{

	public DBHelper(Context context, String name, CursorFactory factory,int version) 
	{
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("create table tMessage (messageId integer primary key autoincrement ," +
				"messageTitle varchar(200),messageContent varchar(5000),messageTime varchar(30))");  
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		// TODO Auto-generated method stub
		db.execSQL("ALTER TABLE tMessage ADD COLUMN other STRING");
	}
	
}
