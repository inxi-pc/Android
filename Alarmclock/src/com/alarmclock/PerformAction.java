package com.alarmclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
/**
 * 
 * @author linxi
 * 
 *	1.闹钟到达的时候执行的动作类,可以是activity,service,brodcast三类中的一种
 *	此处使用activity方便演示.
 *
 *	2.闹钟到达后的所用动作,比如播放音乐,界面设计,都在该类中完成.
 *
 *	3.比较重要的方法是setNextAlarm(int flag),该方法的逻辑是
 *	当闹钟第一次到达执行的时候,判断是否为循环闹钟,如果为循环闹钟
 *	那么下一次该闹钟的执行将是间隔一个星期,即AlarmManager.INTERVAL_DAY*7毫秒钟.
 *	
 */
public class PerformAction extends Activity {
	
	public static final boolean DEBUG=true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createDialog();
		Bundle b = this.getIntent().getExtras();
		int flag = b.getInt("flag");
		boolean finish = b.getBoolean("finish");
		boolean isRepeat = b.getBoolean("isRepeat");
		if(isRepeat) {
			if(finish == false ) {
				setRepeatAlarm(flag);
			}
		}
		//一次性闹钟执行后就删除
		else {
			clearAlarmInfo(flag);
		}
    }
	
	/**
	 * 删除一次性闹钟
	 * @param flag
	 */
    private void clearAlarmInfo(int flag)
    {
    	SharedPreferences alarmInfo = getSharedPreferences("alarmInfo",0);
    	Editor e = alarmInfo.edit();
    	e.remove(String.valueOf(flag));
    	e.commit();
    }
    
	/**
	 * 创建闹铃提示框
	 */
	private void createDialog() {
		AlertDialog b = new AlertDialog.Builder(this)		
        .setTitle("闹铃")
        .setIcon(android.R.drawable.ic_dialog_info)
        .setPositiveButton("确定",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
				onDestroy();
			}
        
        })
    	.create();
        b.show();
	}
	
	/**
	 * 循环闹钟的任务设定有两种方法,set()和setRepeat(),个人觉得set方法更耗资源.
	 * setRepeat()设定一个循环任务,设定一次变会按照固定规律执行任务,set()是设定
	 * 的一次性任务,对于循环闹钟,只要传递isRepeat标志变量为true,那么每执行一次
	 * 任务,都会重新set()一个新任务到下次执行.此处两种方法都提供.
	 * 
	 * @param flag
	 */

	private void setNextAlarm(int flag)
	{
		AlarmManager am =  (AlarmManager) this.getSystemService(ALARM_SERVICE);
		Intent i = new Intent(getApplicationContext(),PerformAction.class);
		i.setAction("AlarmService");
		i.putExtra("isRepeat", true);
		i.putExtra("finish", false);
		/**
		 * 2014-8-19修复BUG
		 * 之前忘了传递标志变量了,导致循环闹钟每次更新后都产生新的AlarmManager,继而删除闹钟后没效果
		 */
		i.putExtra("flag", flag);
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
				flag, i, PendingIntent.FLAG_UPDATE_CURRENT);
		if(DEBUG)
		{
			System.out.println("Create next alarm,the flag is : "+String.valueOf(flag));
			System.out.println("PendingIntent resource id is : "+pi);
			am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000, pi);
		}
		else
		{
			am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+AlarmManager.INTERVAL_DAY*7, pi);
		}
	}
	
	private void setRepeatAlarm(int flag)
	{
		AlarmManager am =  (AlarmManager) this.getSystemService(ALARM_SERVICE);
		Intent i = new Intent(getApplicationContext(),PerformAction.class);
		i.setAction("AlarmService");
		i.putExtra("isRepeat", true);
		i.putExtra("finish", true);
		i.putExtra("flag", flag);
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 
				flag, i, PendingIntent.FLAG_UPDATE_CURRENT);
		if(DEBUG)
		{
			System.out.println("Create repeat alarm,the flag is : "+String.valueOf(flag));
			System.out.println("PendingIntent resource id is : "+pi);
			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000, 10000, pi);
		}
		else
		{
			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+
					AlarmManager.INTERVAL_DAY*7, AlarmManager.INTERVAL_DAY*7, pi);
		}
		
	}
}
