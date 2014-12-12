package com.inxi.Mention;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.inxi.R;
import com.inxi.Interface.MainActivity;
import com.inxi.Utility.PushData.PollingService;

/**
 * @author linxi
 *
 */
public class NewNotiActivity extends Activity implements OnItemClickListener
{
	private ListView mListNotification;
	private LinearLayout mNoMessageTip;
	private List<Map<String,Object>> mList;
	private SimpleAdapter mAdapter;
	
	public static boolean NewActivityIsCreate = false;
	public static Handler mNewsHandler;
	
	//private Container mContainer;
	private Thread mDeleViewThread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_listview);
		
		//RelativeLayout body = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.notification_listview,null);
		//mNoMessageTip = (LinearLayout) body.findViewById(R.id.no_message_tip);
		//mListNotification = (ListView) body.findViewById(R.id.list_message);
		mNoMessageTip = (LinearLayout) findViewById(R.id.no_message_tip);
		mListNotification = (ListView) findViewById(R.id.list_message);
		//PollingServiceʹ�������־������ȷ���Ƿ�ѱ����δ����������Ϣ���ݹ�����
		//NewsHandler����listView
		NewActivityIsCreate = true;
		mList = new ArrayList<Map<String,Object>>();
		mAdapter = new SimpleAdapter(this,mList,R.layout.notification_listview_row,
				new String[]{"image","title","time"},
				new int[]{R.id.image,R.id.title,R.id.time});
		mListNotification.setAdapter(mAdapter);
		mListNotification.setOnItemClickListener(this);
		//mContainer = new Container(this,body,mListNotification,mAdapter,mList);
		//setContentView(mContainer);
		display();

		// ҳ���л���Mentionҳ��ʱ���ڴ�֮ǰAlarmManager��PollingService�Ѿ���MainActivity����
		// ��ʱ�ٴε���ԭ����AlarmManager�ᱻ�ͷŵ��������ᴴ���µ�.ͬ��,service�����onStart()������
		// Ҳ�����ظ�����service
		/*System.out.println("Start polling service...");
		PollingUtils.startPollingService(this, 5,
				PollingService.class, PollingService.ACTION);*/
	}
	
	/**
	 * @author linxi
	 * @content ��ʼ��listview,ע��handler��ȡ������Ϣ
	 */
	public void display()
	{
		if(!PollingService.mWaitingMessage.isEmpty())
		{
			ArrayList<String> strArray = PollingService.mWaitingMessage;
			for(int i=0;i<strArray.size();i++)
			{
				HashMap<String,Object> t = new HashMap<String,Object>();
				t.put("image", R.drawable.ic_launcher);
				t.put("title", strArray.get(i));
				i++;
				t.put("time", strArray.get(i));
				i++;
				t.put("content", strArray.get(i));
				i++;
				mList.add(t);
			}
			PollingService.mWaitingMessage.clear();
			mNoMessageTip.setVisibility(8);
			mAdapter.notifyDataSetChanged();
		}
		else
		{
			mNoMessageTip.setVisibility(0);
		}
		//����handler��ʱ����������Ϣ
		mNewsHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				if(msg.what == 1)
				{
					ArrayList<String> strArray = msg.getData().getStringArrayList("waitingMessage");
					for(int i=0;i<strArray.size();i++)
					{
						HashMap<String,Object> t = new HashMap<String,Object>();
						t.put("image", R.drawable.ic_launcher);
						t.put("title", strArray.get(i));
						i++;
						t.put("time", strArray.get(i));
						i++;
						t.put("content", strArray.get(i));
						i++;
						mList.add(t);
					}
					PollingService.mWaitingMessage.clear();
					mNoMessageTip.setVisibility(8);
					mAdapter.notifyDataSetChanged();
				}
				else if(msg.what == 2)
				{
					if(msg.arg1 <= 0)
					{
						mNoMessageTip.setVisibility(0);
					}
					mAdapter.notifyDataSetChanged();
				}
				
			}
		};
	}
	
	@Override
	public void onItemClick(AdapterView<?> listView, View child, final int position, long arg3) 
	{
		//��main������Ϣ��������Ϣ��ʾ��Ŀ
		PollingService.newMessageCount--;
    	Message message = Message.obtain();
    	if(PollingService.newMessageCount < 0)
    	{
    		PollingService.newMessageCount = 0;
    	}
    	else if(PollingService.newMessageCount == 0)
		{
			message.what = 2;
		}
		else
		{
			message.what = 1;
			message.arg1 = PollingService.newMessageCount;
		}
		MainActivity.mMainHandler.sendMessage(message);
		
		HashMap<String,String> map=(HashMap<String,String>)mAdapter.getItem(position);         
		Intent intent = new Intent(this,NotiContentActivity.class);
		intent.putExtra("messageTitle", map.get("title"));
		intent.putExtra("messageTime", map.get("time"));
		intent.putExtra("messageContent", map.get("content"));
		startActivity(intent);

		mDeleViewThread = new Thread()
		{
			@Override
			public void run()
			{
				Message msg = Message.obtain();
				msg.what = 2; 
				msg.arg1 = PollingService.newMessageCount;
				mList.remove(position);
				mNewsHandler.sendMessage(msg);
			}
		};
		mDeleViewThread.start(); 
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		// ֹͣ�����AlarmManager
		//System.out.println("Stop polling service...");
		/*PollingUtils.stopPollingService(this, PollingService.class,
				PollingService.ACTION);*/
	}
	
}