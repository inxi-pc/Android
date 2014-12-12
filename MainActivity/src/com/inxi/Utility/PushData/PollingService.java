package com.inxi.Utility.PushData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.inxi.R;
import com.inxi.Interface.MainActivity;
import com.inxi.Mention.NewNotiActivity;
import com.inxi.Mention.NotiContentActivity;
import com.inxi.Utility.Database.DBManager;
import com.inxi.Utility.Database.Record;

public class PollingService extends Service 
{
	public  static final String ACTION = "com.inxi.service.PollingService";
	private static final String TAG = "pollingService";
	private static String[] mMessage = new String[3];
	public  static ArrayList<String> mWaitingMessage = new ArrayList<String>();
	public  static boolean startServiceFromBoot = false;

	public static int newMessageCount = 0;
	
	private Notification mNotification;
	private NotificationManager mManager;
	private DBManager mgr;
	private Thread mPollingThread;

	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}

	@Override
	public void onCreate() 
	{
		mgr = new DBManager(this);
		initNotifiManager();
	}

	@Override
	public void onStart(Intent intent, int startId) 
	{
		mPollingThread = new PollingThread();
		mPollingThread.start();
	}
	
	private synchronized boolean insertToDB(String[] message)
	{
		List<Record> DBdate = mgr.query();
		ArrayList<Record> insert = new ArrayList<Record>();
		Record r = new Record(message);
		
		if(DBdate.isEmpty())
		{
			insert.add(r);
			mgr.insert(insert);
			return true;
		}
		else
		{
			for(Record tRecord : DBdate)
			{
				if(tRecord.messageTitle.equals(r.messageTitle))
				{
					return false;
				}
			}
		}
		insert.add(r);
		mgr.insert(insert);
		mgr.closeDB();
		return true;
	}

	private void initNotifiManager() 
	{
		int icon = R.drawable.ic_launcher;
		mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotification = new Notification();
		mNotification.icon = icon;
		mNotification.tickerText = "New Message";
		mNotification.defaults |= Notification.DEFAULT_SOUND;
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
	}

	private void showNotification() 
	{
		Intent i = new Intent(this, NotiContentActivity.class);
		i.putExtra("messageTitle", mMessage[0]);
		i.putExtra("messageTime", mMessage[1]);
		i.putExtra("messageContent", mMessage[2]);
		i.putExtra("isFromService", true);
	
		mNotification.when = System.currentTimeMillis();
		String title = getResources().getString(R.string.app_name);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,Intent.FLAG_ACTIVITY_NEW_TASK);
		mNotification.setLatestEventInfo(this, title, mMessage[0], pendingIntent);
		mManager.notify(0, mNotification);
	}

	public JSONObject[] getNotificationFromServer(String url) 
	{
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		List<BasicNameValuePair> values = new ArrayList<BasicNameValuePair>();
		values.add(new BasicNameValuePair("name", "admin"));
		values.add(new BasicNameValuePair("pwd", "admin"));
	
		try 
		{
			post.setEntity(new UrlEncodedFormEntity(values, "utf-8"));
			HttpResponse response;
			response = client.execute(post);
			/**
			 *  json格式:{messageTitle:value,messageTime:value,messageContent:value}
			 */
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
			{
				Log.i(TAG, "连接成功返回数据");
				JSONArray content = new JSONArray(EntityUtils.toString(response.getEntity()));
				JSONObject resJSON[] = new JSONObject[content.length()];
				for(int i=0;i<content.length();i++)
				{
					resJSON[i] = content.getJSONObject(i);
				}
				return resJSON;
			} 
			else 
			{
				Log.i(TAG, "连接失败");
			}
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		System.out.println("Service:onDestroy");
	}
	
	class PollingThread extends Thread 
	{
		@Override
		public void run() 
		{
			System.out.println("Polling...");
			
			String url = "http://192.168.1.1/pushNotification.php";
			JSONObject[] messageJSON = getNotificationFromServer(url);
			int length = messageJSON[0].length();
			JSONArray title = messageJSON[0].names();
			
			for(int i=0;i<length;i++)
			{
				try 
				{
					mMessage[i] = messageJSON[0].getString(title.getString(i));
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			
			if(mMessage[0] !=null && insertToDB(mMessage))
			{
				System.out.println("New message!"+mMessage[2]);
				showNotification();
				//如果是开机自启动服务不能更新主界面，也无法更新主界面，否则会报错
				if(startServiceFromBoot == false)
				{
					newMessageCount++;
					Message messageToMain = Message.obtain();
					messageToMain.what = 1;
					messageToMain.arg1 = newMessageCount;
					MainActivity.mMainHandler.sendMessage(messageToMain);
					
					if(NewNotiActivity.NewActivityIsCreate == true)
					{
						System.out.println("send message to NewNotiActivity successful");
						
						Message messageToNew = Message.obtain();
						messageToNew.what = 1;
						
						Bundle bundleToNews = new Bundle();
						mWaitingMessage.add(mMessage[0]);
						mWaitingMessage.add(mMessage[1]);
						mWaitingMessage.add(mMessage[2]);
						bundleToNews.putStringArrayList("waitingMessage", mWaitingMessage);
						messageToNew.setData(bundleToNews);
						
						NewNotiActivity.mNewsHandler.sendMessage(messageToNew);
				
					}
					else if(NewNotiActivity.NewActivityIsCreate == false)
					{
						mWaitingMessage.add(mMessage[0]);
						mWaitingMessage.add(mMessage[1]);
						mWaitingMessage.add(mMessage[2]);
					}
				}
			}
			else
			{
				System.out.println("Old message!");
			}
		}
	}
}
