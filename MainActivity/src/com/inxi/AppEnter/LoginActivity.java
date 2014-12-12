package com.inxi.AppEnter;

import java.util.ArrayList;

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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inxi.R;
import com.inxi.Interface.MainActivity;

public class LoginActivity extends Activity implements Handler.Callback
{
	private static final String TAG = "Login";
	private static final String url = "http://www.guanghezhang.com/loginFromAndroid.php";
	public static String mLoginInfo;
	//mLoginInfo静态信息，提示错误，在静态方法LoginValidate()中调用
	private Button mLoginBackBt;
	private Button mRegisterBt;
	private Button mLoginTabBt1;
	private Button mLoginTabBt2;
	private TextView mLoginTip;
	private Button mLoginSubmit;
	private EditText mEditUserId;
	private EditText mEditPwd;
	
	private boolean isInputId = false;
	private boolean isInputPwd = false;
	private boolean isSetListener = false;

	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login_activity);
		mLoginBackBt = (Button) findViewById(R.id.login_back_bt);
		mLoginTabBt1 = (Button) findViewById(R.id.login_tab1_bt);
		mLoginTabBt2 = (Button) findViewById(R.id.login_tab2_bt);
		mLoginTip = (TextView) findViewById(R.id.login_tip_text);
		mLoginSubmit = (Button) findViewById(R.id.login_submit_bt);
		mEditUserId = (EditText) findViewById(R.id.login_id_edit);
		mEditPwd = (EditText) findViewById(R.id.login_pwd_edit);
		
		mLoginSubmit.setBackgroundResource(R.drawable.login_btn_disabled);
		mLoginTabBt1.setTextColor(0xff3382e4);
		
		if(mLoginInfo != "")
		{
			mLoginTip.setTextColor(Color.rgb(15, 43, 255));
			mLoginTip.setText(mLoginInfo);
		}
		
		setupListener();
		mHandler = new Handler(this);
	}
	
	@Override
	public boolean handleMessage(Message msg)
	{
		if(msg.what == 1)
		{
			if(isInputId == true && isInputPwd == true)
			{
				mLoginSubmit.setClickable(true);
				mLoginSubmit.setBackgroundResource(R.drawable.login_submit_bt_change);
				if(isSetListener == false)
				{
					mLoginSubmit.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) 
						{
							// TODO Auto-generated method stub
							String userId = mEditUserId.getText().toString();
							String pwd = mEditPwd.getText().toString();
							System.out.println("userId:"+userId+"\tpwd:"+pwd);
							
							Thread loginThread = new LoginThread(url,userId,pwd);
							loginThread.start();
						}
					});
					isSetListener = true;
				}
			}
			else
			{
				mLoginSubmit.setClickable(false);
				mLoginSubmit.setBackgroundResource(R.drawable.login_btn_disabled);
			}
		}
		
		if(msg.what == 2)
		{
			mLoginTip.setTextColor(Color.rgb(15, 43, 255));
			mLoginTip.setText(mLoginInfo);
		}
		return true;
	}
	
	private void setupListener()
	{
		mLoginBackBt.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mLoginTabBt1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				mLoginTabBt1.setBackgroundResource(R.drawable.login_tab_pressed);
				mLoginTabBt1.setTextColor(0xff3382e4);
				mLoginTabBt2.setBackgroundResource(R.drawable.login_tab_normal);
				mLoginTabBt2.setTextColor(0xff000000);
			}
		});
		
		mLoginTabBt2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				mLoginTabBt2.setBackgroundResource(R.drawable.login_tab_pressed);
				mLoginTabBt2.setTextColor(0xff3382e4);
				mLoginTabBt1.setBackgroundResource(R.drawable.login_tab_normal);
				mLoginTabBt1.setTextColor(0xff000000);
			}
		});
		
		mEditUserId.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) 
			{
				// TODO Auto-generated method stub
	
			}

			@Override
			public void afterTextChanged(Editable s) 
			{
				// TODO Auto-generated method stub
				if(s.length() > 0)
				{
					isInputId = true;
				}
				else
				{
					isInputId = false;
				}
				//通知handler判断值，更新Button
				Message msg = Message.obtain();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
		});
		
		mEditPwd.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) 
			{
				// TODO Auto-generated method stub
				if(s.length() > 0)
				{
					isInputPwd = true;
				}
				else
				{
					isInputPwd = false;
				}
				Message msg = Message.obtain();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
		});
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
		}catch(Exception e)
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
				SharedPreferences loginSp = LoginActivity.this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
				Editor edit = loginSp.edit();
				edit.putString("userId", userId);
				edit.putString("pwd", pwd);
				edit.commit();
				
				Intent i = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(i);
				finish();
			}
			else
			{
				Message m = Message.obtain();
				m.what = 2;
				m.obj = mLoginInfo;
				mHandler.sendMessage(m);
			}
		}
	}
}
