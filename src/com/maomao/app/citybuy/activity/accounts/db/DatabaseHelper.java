/**
 * 
 */
package com.maomao.app.citybuy.activity.accounts.db;

import java.util.ArrayList;
import java.util.List;

import com.maomao.app.citybuy.activity.accounts.AccountingData;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author DingMaolin
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	/* 数据库名称 */
	public static final String DB_NAME = "citybuy.db";
	/* 数据库版本 */
	public static final int DB_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		/* 数据库没有表时创建一个 */
		//创建下载表
		db.execSQL("CREATE TABLE IF NOT EXISTS accounting (id integer primary key, time TEXT, type TEXT, price TEXT, remark TEXT)");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	public void saveAccountingData(AccountingData accountingData){
		String sql = "INSERT INTO accounting(time, type, price, remark) VALUES (?, ?, ?, ?)";
		Object[] bingArgs = { accountingData.getTime(), accountingData.getType(), accountingData.getPrice(), accountingData.getRemark() };
		getWritableDatabase().execSQL(sql, bingArgs);
		close();
	}
	
	public void deleteAccountingData(int id){
		String sql = "DELETE FROM accounting WHERE id=?";
		Object[] bingArgs = { id };
		getWritableDatabase().execSQL(sql, bingArgs);
		close();
	}
	
	public void updateAccountingData(AccountingData accountingData){
//		deleteAccountingData(accountingData.getId());
//		saveAccountingData(accountingData);
		String sql = "update accounting set time=?, type=?, price=?, remark=? WHERE id=?";
		Object[] bingArgs = { accountingData.getTime(), accountingData.getType(), accountingData.getPrice(), accountingData.getRemark(), accountingData.getId() };
		getWritableDatabase().execSQL(sql, bingArgs);
		close();
	}
	
	/**
	 * desc降序, asc升序
	 * @param type
	 * @return
	 */
	public List<AccountingData> getAllAccountingDataByType(String type){
		List<AccountingData> list = new ArrayList<AccountingData>();
		String sql = null;
		if(type == null || type.length() < 1){
			sql = "select * from accounting order by time desc";// desc
		}else{
			sql = "select * from accounting where type="+type+" order by time desc";// desc
		}
		Cursor cursor = getReadableDatabase().rawQuery(sql, null);
		if (cursor == null || cursor.getCount() < 1) {
			cursor.close();
			close();
			return list;
		}
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			AccountingData accountingData = new AccountingData();
			accountingData.setId(cursor.getInt(0));
			accountingData.setTime(cursor.getString(1));
			accountingData.setType(cursor.getString(2));
			accountingData.setPrice(cursor.getString(3));
			accountingData.setRemark(cursor.getString(4));
			list.add(accountingData);
		}
		cursor.close();
		close();
		return list;
	}
	
	public List<AccountingData> getAllAccountingDataByTime(String time){
		List<AccountingData> list = new ArrayList<AccountingData>();
		String sql = null;
//		if(type == null || type.length() < 1){
//			sql = "select * from accounting order by time desc";// desc
//		}else{
//			sql = "select * from accounting where type="+type+" order by time desc";// desc
//		}
//		Cursor cursor = getReadableDatabase().rawQuery(sql, null);
//		if (cursor == null || cursor.getCount() < 1) {
//			cursor.close();
//			close();
//			return list;
//		}
//		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//			AccountingData accountingData = new AccountingData();
//			accountingData.setId(cursor.getInt(0));
//			accountingData.setTime(cursor.getString(1));
//			accountingData.setType(cursor.getString(2));
//			accountingData.setPrice(cursor.getString(3));
//			accountingData.setRemark(cursor.getString(4));
//			list.add(accountingData);
//		}
//		cursor.close();
//		close();
		return list;
	}
	
	public List<AccountingData> getTodayAccountingData(String time){
		List<AccountingData> list = new ArrayList<AccountingData>();
		String sql = "select * from accounting where time = '"+time+"' order by id desc";
		Cursor cursor = getReadableDatabase().rawQuery(sql, null);
		if (cursor == null || cursor.getCount() < 1) {
			cursor.close();
			close();
			return list;
		}
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			AccountingData accountingData = new AccountingData();
			accountingData.setId(cursor.getInt(0));
			accountingData.setTime(cursor.getString(1));
			accountingData.setType(cursor.getString(2));
			accountingData.setPrice(cursor.getString(3));
			accountingData.setRemark(cursor.getString(4));
			list.add(accountingData);
		}
		cursor.close();
		close();
		return list;
	}
	
	
}
