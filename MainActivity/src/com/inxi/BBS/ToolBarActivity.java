package com.inxi.BBS;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TabHost;

import com.inxi.R;

/**
 * @author linxi
 *
 */
public class ToolBarActivity extends  TabActivity implements CompoundButton.OnCheckedChangeListener 
{	
	private static final String DISCUSS_TAB = "discuss_tab";
	private static final String COLLECT_TAB = "collect_tab";
	private static final String PERSON_TAB = "person_tab";
	private static final String MORE_TAB = "more_tab";
	private static final String NOTE_TAB = "note_tab";
	
	private Intent mNoteIntent;
	private Intent mDiscussIntent;
	private Intent mCollectIntent;
	private Intent mPersonIntent;
	private Intent mMoreIntent;
	
	private TabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_tool_tab);
		
		mTabHost = getTabHost();
		initIntent();
		initRadios();
		setupIntents();
		
		mTabHost.setCurrentTabByTag(NOTE_TAB);
	}

	private void initIntent() 
	{
		// TODO Auto-generated method stub
		this.mDiscussIntent = new Intent(this,NoteDiscussActivity.class);
		this.mNoteIntent = new Intent(this,NoteContentActivity.class);
		/*this.mCollectIntent = new Intent(this,CollectNoteActivity.class);
		this.mPersonIntent = new Intent(this,PersonActivity.class);
		this.mMoreIntent = new Intent(this,MoreActivity.class);*/
		
	}
	
	
	private void initRadios() 
	{
		// TODO Auto-generated method stub
		((RadioButton) findViewById(R.id.tool_radio_discuss))
		.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.tool_radio_collect))
		.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.tool_radio_personinfo))
		.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.tool_radio_more))
		.setOnCheckedChangeListener(this);
	}
	
	private void setupIntents() 
	{
		// TODO Auto-generated method stub
		// 需要说明的是这里为tab添加了标签的时候并没有启动所以activity，OnCreate方法没有调用
		// 只有当mTabHost.setCurrentTabByTag(tagName);的时候才会创建对应的activity
		((RadioButton)findViewById(R.id.tool_radio_discuss)).setChecked(true);
		mTabHost.addTab(this.buildTabSpec(NOTE_TAB,this.mNoteIntent));
		mTabHost.addTab(this.buildTabSpec(DISCUSS_TAB, this.mDiscussIntent));
		/*mTabHost.addTab(this.buildTabSpec(COLLECT_TAB, this.mCollectIntent));
		mTabHost.addTab(this.buildTabSpec(PERSON_TAB, this.mPersonIntent));
		mTabHost.addTab(this.buildTabSpec(MORE_TAB, this.mMoreIntent));*/
	}
	
	private  TabHost.TabSpec buildTabSpec(String tag,Intent intent)
	{
		TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setContent(intent).setIndicator("",getResources().getDrawable(R.drawable.ic_launcher));
		return tabSpec;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
	{
		if(isChecked)
		{
			switch(buttonView.getId())
			{
			case R.id.tool_radio_discuss:
				mTabHost.setCurrentTabByTag(DISCUSS_TAB);
				break;
			/*case R.id.tool_radio_collect:
				mTabHost.setCurrentTabByTag(COLLECT_TAB);
				break;
			case R.id.tool_radio_personinfo:
				mTabHost.setCurrentTabByTag(PERSON_TAB);
				break;
			case R.id.tool_radio_more:
				mTabHost.setCurrentTabByTag(MORE_TAB);
				break;*/
			default:
				break;
			}
		}
	}
	
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    }
    
}
