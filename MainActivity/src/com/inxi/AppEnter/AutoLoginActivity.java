package com.inxi.AppEnter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.inxi.R;
import com.inxi.Interface.MainActivity;

/**
 * @author linxi
 *
 */
public class AutoLoginActivity extends Activity
{
	private static final String TAG = "AutoLogin";
	private static final String url = "http://www.guanghezhang.com/loginFromAndroid.php";
	private static String mLoginInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_login_activity);
		SharedPreferences loginSp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		
		if(loginSp.getString("userId", null) != null)
		{
			LoginThread lt = new LoginThread(url, loginSp.getString("userId", "error"), 
					loginSp.getString("pwd", "error"));
			lt.start();
		}
		else
		{
			Intent i = new Intent(AutoLoginActivity.this,LoginActivity.class);
			skipActivity(this,i);
		}
	}

	private void skipActivity(final Activity from,final Intent to)
	{
		Timer timer = new Timer();
		TimerTask task = new TimerTask()
		{
			@Override
			public void run() 
			{
				from.finish();
				startActivity(to);
			}	
		};
		timer.schedule(task, 2000);
	}
	
	public static boolean loginValidate(String url,String userId,String pwd)
	{
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		
		ArrayList<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
		postData.add(new BasicNameValuePair("id",userId));
		postData.add(new BasicNameValuePair("pwd",pwd));
		
		try 
		{
			post.setEntity(new UrlEncodedFormEntity(postData,"utf-8"));
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				Log.i(TAG, "连接成功");
				JSONObject code = new JSONObject(EntityUtils.toString(response.getEntity()));
				String info = code.getString("code");
				int codeInfo = Integer.parseInt(info);
				switch(codeInfo)
				{
				case 0:
					System.out.println("登陆成功!");
					return true;
				case 1:
					System.out.println("用户名密码为空!");
					mLoginInfo = "用户名或密码为空";
					break;
				case 2:
					System.out.println("用户密码错误!");
					mLoginInfo = "密码错误，请重新输入";
					break;
				case 3:
					System.out.println("用户名不存在!");
					mLoginInfo = "用户名不存在，请重新输入";
					break;
				default:
					System.out.println("未知错误!");
					mLoginInfo = "未知错误";
				}
			}
		}catch (Exception e) 
		{
			e.printStackTrace();
		}
		return true;
	}
	
	class LoginThread extends Thread
	{
		private String userId;
		private String pwd;
		private String url;
		public LoginThread(String url,String userId,String pwd)
		{
			this.userId = userId;
			this.pwd = pwd;
			this.url = url;
		}
		
		@Override
		public void run()
		{
			if(loginValidate(url,userId,pwd))
			{
				SharedPreferences loginSp = AutoLoginActivity.this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
				Editor edit = loginSp.edit();
				edit.putString("userId", userId);
				edit.putString("pwd", pwd);
				edit.commit();
				
				Intent i = new Intent(AutoLoginActivity.this,MainActivity.class);
				skipActivity(AutoLoginActivity.this,i);
			}
			else
			{
				Message m = Message.obtain();
				m.what = 2;
				m.obj = mLoginInfo;
				
				Intent i = new Intent(AutoLoginActivity.this,LoginActivity.class);
				skipActivity(AutoLoginActivity.this,i);
				LoginActivity.mLoginInfo = mLoginInfo;
			}
		}
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
