package com.inxi.Interface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.inxi.R;
import com.inxi.AppEnter.LoginActivity;
import com.inxi.PersonCenter.FastIdeaAnalysis;

/**
 * @author linxi
 *
 */
public class PersonActivity extends Activity
{
	private Button mLoginout;
	private ImageView mIdeaAnalysis;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_info_activity);
		
		mLoginout = (Button) findViewById(R.id.personinfo_loginout_bt);
		mIdeaAnalysis = (ImageView) findViewById(R.id.imageView2);
		setListener();
	}
	
	private void setListener()
	{
		mLoginout.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) 
			{
				SharedPreferences loginSp = PersonActivity.this.getSharedPreferences("userInfo", 
						Context.MODE_PRIVATE);
				if(loginSp.getString("userId", null) != null)
				{
					Editor ed = loginSp.edit();
					ed.remove("userId");
					ed.remove("pwd");
					ed.commit();
					System.out.println("Stop polling service...");
					//PollingUtils.stopPollingService(PersonActivity.this, PollingService.class,
							//PollingService.ACTION);
					System.out.println("login out successful!");
					finish();
					Intent i = new Intent(PersonActivity.this,LoginActivity.class);
					startActivity(i);
				}
			}
		});
		
		mIdeaAnalysis.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(PersonActivity.this,FastIdeaAnalysis.class);
				startActivity(i);
			}
		});
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
