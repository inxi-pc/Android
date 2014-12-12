package com.inxi.Mention;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.inxi.R;
import com.inxi.Utility.Database.DBManager;
import com.inxi.Utility.Database.Record;

/**
 * @author linxi
 *
 */
public class WholeNotiActivity extends Activity implements OnItemClickListener
{
	private ListView mListNotification;
	private SimpleAdapter mAdapter;
	private List<Map<String,Object>> mList;
	private DBManager mgr;
	//private Container mContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_listview);
		//RelativeLayout body = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.notification_listview,null);
		//mListNotification = (ListView) body.findViewById(R.id.list_message);
		mListNotification = (ListView) findViewById(R.id.list_message);
		mList = new ArrayList<Map<String,Object>>();
		mAdapter = new SimpleAdapter(this,mList,R.layout.notification_listview_row,
				new String[]{"image","title","time"},
				new int[]{R.id.image,R.id.title,R.id.time});
		//mContainer = new Container(this,body,mListNotification,mAdapter,mList);
		
		mgr = new DBManager(this);
		mListNotification.setAdapter(mAdapter);
		mListNotification.setOnItemClickListener(this);
		//setContentView(mContainer);
		setupDisplay();
	}
	
	private void setupDisplay()
	{
		List<Record> listMessage = mgr.query();
		mList.clear();
		
		for(Record tRecord : listMessage)
		{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("image", R.drawable.ic_launcher);
			map.put("title", tRecord.messageTitle);
			map.put("time", tRecord.messageTime);
			mList.add(map);
		} 
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		
		HashMap<String,String> map = (HashMap<String, String>) mAdapter.getItem(arg2);
		String[] title= new String[1];
        title[0] = map.get("title");
        List<Record> list = mgr.query("select *from tMessage where MessageTitle like ?",title);
        
        for(Record tRecord : list)
        {
    		Intent intent = new Intent(this,NotiContentActivity.class);
    		intent.putExtra("messageTitle", tRecord.messageTitle);
    		intent.putExtra("messageTime", tRecord.messageTime);
    		intent.putExtra("messageContent", tRecord.messageContent);
    		startActivity(intent);
        }
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		// Í£Ö¹·þÎñºÍAlarmManager
		/*System.out.println("Stop polling service...");
		PollingUtils.stopPollingService(this, PollingService.class,
				PollingService.ACTION);*/
	}
}