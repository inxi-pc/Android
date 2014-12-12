package com.inxi.Mention;


import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.inxi.R;
import com.inxi.Interface.MentionActivity;

public class NotiContentActivity extends Activity 
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_content);

		TextView contentView = (TextView) findViewById(R.id.messageContent);
		TextView titleView = (TextView) findViewById(R.id.messageTitle);
		TextView timeView = (TextView) findViewById(R.id.messageTime); 
		
		String messageContent = this.getIntent().getExtras().getString("messageContent");
		String messageTitle = this.getIntent().getExtras().getString("messageTitle");
		String messageTime = this.getIntent().getExtras().getString("messageTime");
		
		titleView.setText(messageTitle);
		timeView.setText(messageTime);
		contentView.setText(messageContent);
		
		Button backButton = (Button) findViewById(R.id.back);
		Button homeButton = (Button) findViewById(R.id.home);
		backButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				NotiContentActivity.this.finish();
			}
		});
		homeButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(NotiContentActivity.this,MentionActivity.class);
				NotiContentActivity.this.finish();
				startActivity(i);
			}
			
		});
		if(this.getIntent().getExtras().getBoolean("isFromService"))
		{
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancelAll();
		}
	}
	
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
}
