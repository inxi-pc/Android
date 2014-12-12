package com.alarmclock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
/**
 * 
 * @author linxi
 * 主界面类,主要作用是更新闹钟列表,提供闹钟添加删除功能按钮.
 * 每一个闹钟使用SharedPreferences类进行储存.
 * SharedPreferences的数据结构就是key-value键值对.此处我使用的key是string类型,
 * 值为flag,value的类型我选用的是HashSet,里面可以存放多个值,包括闹钟的flag,时间,
 * 循环日期.
 * 
 * 两个函数功能:
 * 1.stopAlarm()
 * 点击listview中的闹钟后会触发该功能,它将SharedPreferences储存的闹钟信息删除,
 * 并通过flag取消闹钟定时任务.
 * 
 * 2.updateListView()
 * 启动程序时,通过获取SharedPreferences中的闹钟内容来更新listview.
 * 
 */
public class MainActivity extends Activity {
	
	/**
	 * listview工作原理:
	 * 
	 *	使用SimpleAdapter适配器将数据映射到listview对应视图上,
	 *	从SimpleAdapter的构造方法就可以看出..
	 *	SimpleAdapter(创建适配器上下文环境,数据,每一列的视图,数据的字段,每段数据对应到视图的某一部分)
	 *
	 *	通过SimpleAdapter.notifyDataSetChanged()来更新数据,如果数据有变化的话.
	**/
	public ArrayList<HashMap<String,Object>> mAlarmData;
	public ListView mListAlarm;
	public SimpleAdapter mAdapter;
	public SharedPreferences mAlarmInfo;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        mAlarmInfo = getSharedPreferences("alarmInfo",0);
        Button btCreate = (Button) findViewById(R.id.bt_create); 
        btCreate.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,ClockCreate.class);
				startActivity(i);
			}
        });
        
        mListAlarm = (ListView) findViewById(R.id.list_clock);
        mListAlarm.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView t = (TextView) view.findViewById(R.id.flag);
				String flag = (String) t.getText();
				String[] split = flag.split(",");
				for(int i = 0;i<split.length;i++)
				{
					stopAlarm(Integer.parseInt(split[i]));
				}
			}
        });
        mAlarmData = new ArrayList<HashMap<String,Object>>();
        //我把flag的值映射到一个隐藏的textview上,这样删除时才能获取对应的闹钟的flag
        mAdapter = new SimpleAdapter(this, mAlarmData, R.layout.list_content, 
        		new String[]{"time","date","flag"}, new int[]{R.id.title,R.id.content,R.id.flag});
        mListAlarm.setAdapter(mAdapter);
        updateListView();
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();
    	updateListView();
    }
    
    /**
     * Iterator这个东西是用来循环获取集合中的值.
     * 
     * 显示的时候要判断值的内容,来决定映射到那一种view上,没有别的意义,仅仅是为了美观.
     */
    private void updateListView()
    {
    	mAlarmData.clear();
		Iterator it = mAlarmInfo.getAll().keySet().iterator();
		//遍历SharedPreferences中的值
    	while(it.hasNext())
    	{
    		String key = (String) it.next();
    		StringBuilder flagSB = new StringBuilder();
    		HashSet s = (HashSet) mAlarmInfo.getStringSet(key,null);
    		HashMap<String,Object> m = new HashMap<String,Object>();
    		Iterator si = s.iterator();
    		//遍历hashset,获得闹钟信息
    		while(si.hasNext())
    		{
    	    	String st = (String) si.next();
    	    	if(st.equals("从不") || st.contains("周"))
    	    	{
    	    		m.put("date", st);
    	    	}
    	    	else if(st.contains(":"))
    	    	{
    	    		m.put("time",st);
    	    	}
    	    	else
    	    	{
    	    		flagSB.append(st+",");
    	    	}
       		}
    		/**
    		 * 2014-8-19修复BUG
    		 */
    		//如果有多个flag值,把最后的","去掉
    		if(!flagSB.toString().isEmpty())
    		{
    			String flagList = flagSB.toString();
    			m.put("flag",flagList.substring(0, flagList.length()-1));
    		}
    		//如果flagSB没有字符串,表示这是一个一次性闹钟,key值就是它的flag值
    		else
    		{
    			m.put("flag", key);
    		}
      		mAlarmData.add(m);
    	}
    	mAdapter.notifyDataSetChanged();
    }
    /**
     * 
     * @param flag
     * 闹钟停止过程就是一个PendingIntent创建过程,注意flag要和原来闹钟创建一致,才能成功停止
     */
    private void stopAlarm(int flag)
    {
    	AlarmManager am =  (AlarmManager) this.getSystemService(ALARM_SERVICE);
		Intent i = new Intent(getApplicationContext(),PerformAction.class);
		i.setAction("AlarmService");
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), flag, i, PendingIntent.FLAG_CANCEL_CURRENT);
		System.out.println(flag);
		System.out.println(pi);
		am.cancel(pi);
		
		Editor e = mAlarmInfo.edit();
    	e.remove(String.valueOf(flag));
    	e.commit();
    	updateListView();
    }
    
}
