package com.inxi.BBS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.inxi.R;
import com.inxi.Interface.MentionActivity;

public class NoteContentActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_content);

		TextView contentView = (TextView) findViewById(R.id.note_content);
		TextView titleView = (TextView) findViewById(R.id.note_content_title);
		TextView timeView = (TextView) findViewById(R.id.note_content_time); 
		
		
		Button backButton = (Button) findViewById(R.id.bbs_back_bt);
		Button homeButton = (Button) findViewById(R.id.bbs_home_bt);
		backButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				NoteContentActivity.this.finish();
			}
		});
		homeButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(NoteContentActivity.this,MentionActivity.class);
				NoteContentActivity.this.finish();
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
