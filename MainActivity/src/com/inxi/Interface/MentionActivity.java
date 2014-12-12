package com.inxi.Interface;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.inxi.R;
import com.inxi.Mention.NewNotiActivity;
import com.inxi.Mention.WholeNotiActivity;

/**
 * @author linxi
 *
 */
public class MentionActivity extends ActivityGroup implements View.OnClickListener
{
	private static final String NEWNOTIFICATION_ID = "newnotification";
	private static final String ALLNOTIFICATION_ID = "allnotification"; 
	
	private Button mButtonNewNotification;
	private Button mButtonAllNotification;
	private FrameLayout mContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mention_activity);
		setupDisplay();
		
		switchPage(R.id.mention_bt_newnotification);
	}
	
	public void addView(String id, Class<?> clazz) 
	{
		Intent intent = new Intent(this, clazz);
		mContent.removeAllViews();
		mContent.addView(getLocalActivityManager().startActivity(id, intent).getDecorView());
	}
	
	@Override
	public void onClick(View v) 
	{
		switchPage(v.getId());
	}
	
	private void setupDisplay()
	{
		this.mContent = (FrameLayout) findViewById(R.id.mention_content);
		this.mButtonNewNotification = (Button) findViewById(R.id.mention_bt_newnotification);
		this.mButtonAllNotification = (Button) findViewById(R.id.mention_bt_all_notification);
		
		this.mButtonNewNotification.setOnClickListener(this);
		this.mButtonAllNotification.setOnClickListener(this);
	}
	
	private void switchPage(int viewId)
	{
		switch(viewId)
		{
		case R.id.mention_bt_newnotification:
			this.addView(NEWNOTIFICATION_ID, NewNotiActivity.class);
			this.mButtonNewNotification.setTextColor(0xffffffff);
			this.mButtonNewNotification.setBackgroundResource(R.drawable.home_topbar_bt);
			this.mButtonAllNotification.setTextColor(0xff90afff);
			this.mButtonAllNotification.setBackgroundResource(0);
			break;
			
		case R.id.mention_bt_all_notification:
			this.addView(ALLNOTIFICATION_ID, WholeNotiActivity.class);
			this.mButtonAllNotification.setTextColor(0xffffffff);
			this.mButtonAllNotification.setBackgroundResource(R.drawable.home_topbar_bt);
			this.mButtonNewNotification.setTextColor(0xff90afff);
			this.mButtonNewNotification.setBackgroundResource(0);
			break;
		}
	}
	
	@Override 
	protected void onDestroy()
	{
		super.onDestroy();
	}
}