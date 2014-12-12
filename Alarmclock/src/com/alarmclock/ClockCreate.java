package com.alarmclock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
/**
 * 
 * @author linxi
 *
 *	总结来说,我的闹钟创建逻辑就是计算出闹钟第一次触发的时间,循环闹钟触发后更新alarmmanger至一个固定规律.
 *	时间的计算封装在DateUtil里面.
 *
 *	闹钟创建的逻辑如下,页面存在一个时间选择timerpicker组件,和一个文本框.一个确定创建和一个取消按钮.
 *	1.在timerpicker上选择时间后,mTime.getCurrentHour() + ":" + mTime.getCurrentMinute()用来
 *	获得选择的时间.
 *
 *	2.点击文本view,调用createDialog()方法弹出闹钟重复日期选择.选择日期后会改变mChoosed数组对应值.
 *	mChoosed数组下标隐式包含着重复日期,未进行选择重复全为false,选择后的日期为true,下标+1对应这个日期.
 *
 *	3.点击确定按钮,如果是循环闹钟,那么多次执行performAction()创建多个时间任务.如果是一次性闹钟,
 *	只创建一个.
 *
 *	创建过程重点理解flag作用和概念.
 */
public class ClockCreate extends Activity {
	
	public static boolean DEBUG = true;
	public LinearLayout mLoop;
	public TimePicker mTime;
	public TextView mDate;
	public static boolean[] mChoosed = {false,false,false,false,false,false,false};
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_create);
		mLoop = (LinearLayout) findViewById(R.id.choose_date);
		mTime = (TimePicker) findViewById(R.id.timePicker1);
		mDate = (TextView) findViewById(R.id.text_date);
		mTime.setIs24HourView(true);
		
		mLoop.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				mLoop.setBackgroundResource(R.drawable.line);
				createDialog();
			}	
		});
		
		Button yes = (Button) findViewById(R.id.bt_yes);
		Button cancel = (Button) findViewById(R.id.bt_cancel);
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				for(int i=0;i<7;i++)
					mChoosed[i] = false;
				finish();
			}});
		//在确定事件里,其中包含创建闹钟的重要逻辑
		//获取闹钟设置信息->判断为何种闹钟->创建.两种闹钟的差别在flag的数量和时间计算方法
		//循环闹钟有多个flag,代表多个循环日期.一次性闹钟只有一个flag,为了统一传递变量
		//都用ArrayList储存
		yes.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String triggerTime = mTime.getCurrentHour() + ":" + mTime.getCurrentMinute();
				//1.获取对话框选择的星期数,用String表示,如1,2,3,
				StringBuilder sb = new StringBuilder();
				String week = null;
				for(int index = 0;index<7;index++)
		        {
					if(mChoosed[index] == true)
					{		
						sb.append(index+1);
						sb.append(",");
					}
		        }
				//循环闹铃
				ArrayList<String>  flagList = new ArrayList<String>();
				if(!sb.toString().isEmpty())
				{
					week = sb.toString().substring(0,sb.toString().length()-1);
					//调用DateUtil的静态方法获取闹铃第一次响起的时间,该时间是Unix时间戳
					long[] task = DateUtil.getNextAlarmTime(week, triggerTime);
					for(long trigger : task)
					{
						flagList.add(String.valueOf(createClock(trigger,true)));
					}
				}
				//非循环闹铃,只响一次
				else
				{
					//计算非循环闹钟响铃的UTC时间
					Long onceTriggerTime = DateUtil.getOnceNextAlarmTime(triggerTime);
					flagList.add(String.valueOf(createClock(onceTriggerTime,false)));
				}
				//储存闹钟信息
				for(int i=0;i<7;i++)
					mChoosed[i] = false;
				storageClockInfo(flagList);
				finish();
			}});
	}
	
	private void storageClockInfo(ArrayList<String> flagList)
	{
		//储存三个重要信息,flag(唯一标识,主界面使用它关闭闹钟),time(闹钟响起时间),date(闹钟响起日期)		
		SharedPreferences alarmInfo = getSharedPreferences("alarmInfo",0);
        Editor e = alarmInfo.edit();
        Set<String> values = new HashSet<String>();
        values.add(mTime.getCurrentHour() + ":" + mTime.getCurrentMinute());
        values.add((String) mDate.getText());
        values.addAll(flagList);
        e.putStringSet(String.valueOf(flagList.get(0)), values);
        e.commit();
	}
	/**
	 * 
	 * @param time		闹钟响的时间
	 * @param isRepeat	是否重复
	 * 
	 * 为什么需要flag?
	 * 
	 * 主要作用就是为了给主界面提供取消闹钟操作提供可能.对于一个AlarmManager来说,系统只存在一个该服务实例,
	 * 而AlarmManager通过setxxxx()等方法设定定时任务需要用到PendingIntent,该类可以理解为延迟的Intent,作
	 * 用是使得一个定义好的Intent任务,可以不依赖当前activity而在任何需要的时刻启动这个Intent任务,PendingIntent
	 * 是通过getActivity(),getBroadcast(),getService()创建,三种方法的第二个参数叫requestCode,该参数用来
	 * 唯一标识PendingInent,这样,唯一标识的PendingIntent就等价于唯一的AlarmManager,也就是唯一的一个闹钟.
	 * 取消AlarmManager的操作和创建AlarmManager是一样的,同样需要定义PendingIntent,这时候PendingIntent
	 * 必须设定成和原始PendingIntent一样,才能成功取消掉这个AlarmManager.
	 *  	 
	 */
	private int createClock(long time,boolean isRepeat) {
		//获取一个唯一标识,这里使用时间戳,标识的作用是为了创建多个不同的AlarmManager
		int flag = (int) ((int) System.currentTimeMillis() / 1000 + Math.random()*1000);
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(getApplicationContext(),PerformAction.class);
		i.setAction("AlarmService");
		if(isRepeat)
		{
			i.putExtra("isRepeat", true);
			i.putExtra("finish", false);
		}
		else
		{
			i.putExtra("isRepeat", false);
		}
		i.putExtra("flag", flag);
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), flag, i, PendingIntent.FLAG_UPDATE_CURRENT);
		am.set(AlarmManager.RTC_WAKEUP, time, pi);
		
		if(DEBUG)
		{
			System.out.println("Create alarm,the flag is : "+String.valueOf(flag));
			System.out.println("PendingIntent resource id is : "+pi);
		}
		return flag;
	}
	 
	/**
	 * 
	 * mChoosed[]数组包含选择的循环触发星期数,0~6的数组元素对应是周一到周六被选择的情况
	 * 
	 */
	private void createDialog() {
		Builder date = new AlertDialog.Builder(ClockCreate.this);
		final String[] choice = {"周一","周二","周三","周四","周五","周六","周日"};
		for(int i=0;i<7;i++)
			mChoosed[i] = false;
		
		DialogInterface.OnMultiChoiceClickListener mutiListener =	
				new DialogInterface.OnMultiChoiceClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialogInterface,   
                            int which, boolean isChecked) {  
                    	mChoosed[which] = isChecked;
                    }  
                };  
        //点确定后改变重复提示的textview的值.
        DialogInterface.OnClickListener btnListener =   
                new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialogInterface, int which) {  
                    	StringBuilder sb = new StringBuilder();
                    	for(int i = 0;i<7;i++)
                    	{
                    		System.out.println(mChoosed[i]);
                    		if(mChoosed[i] == true)
                    		{
                    			sb.append(choice[i]);
                    			sb.append(",");
                    		}
                    	}
                    	
                    	if(!sb.toString().isEmpty())
                    	{
                    		String s = sb.toString();
                    		mDate.setText(s.substring(0,s.length()-1));
                    	}
                    	else
                    	{
                    		mDate.setText("从不");
                    	}
                    }  
                };  
		
        date.setMultiChoiceItems(choice, mChoosed, mutiListener) ;
        date.setTitle("选择日期");
        date.setIcon(android.R.drawable.ic_dialog_info);
        date.setPositiveButton("确定",btnListener);
        date.setNegativeButton("取消", null);
        AlertDialog dateDialog = date.create();
        dateDialog.show();
	}
	
}
