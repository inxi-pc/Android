package com.inxi.Interface;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;

import com.inxi.R;
import com.inxi.Utility.PushData.PollingService;
import com.inxi.Utility.PushData.PollingUtils;

/**
 * @author linxi
 *
 */
public class MainActivity extends  TabActivity implements CompoundButton.OnCheckedChangeListener 
{	
	private static final String HOME_TAB = "home_tab";
	private static final String MENTION_TAB = "mention_tab";
	private static final String PERSON_TAB = "person_tab";
	private static final String MORE_TAB = "more_tab";
	
	public static Handler mMainHandler;
	
	private Intent mHomeIntent = null;
	private Intent mMentionIntent = null;
	private Intent mPersonIntent = null;
	private Intent mMoreIntent = null;
	
	private TabHost mTabHost = null;
	private TextView mMentionTipsMessage = null;
	private TextView mMessageTipsMessage = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintabs_activity);
		
		//程序启动的时候就启动消息检测线程和AlarmManager定时获取最新消息
		System.out.println("Start polling service...");
		PollingUtils.startPollingService(this, 4, PollingService.class, PollingService.ACTION);
		
		mTabHost = getTabHost();
		initIntent();
		initTips();
		initRadios();
		setupIntents();
	}

	private void initIntent() 
	{
		// TODO Auto-generated method stub
		this.mHomeIntent = new Intent(this,BBSActivity.class);
		this.mMentionIntent = new Intent(this,MentionActivity.class);
		this.mPersonIntent = new Intent(this,PersonActivity.class);
		this.mMoreIntent = new Intent(this,MoreActivity.class);
		
	}
	
	private void initTips() 
	{
		// TODO Auto-generated method stub
		mMentionTipsMessage = (TextView) findViewById(R.id.message_mention);
		mMessageTipsMessage = (TextView) findViewById(R.id.message_person);
		/**
		 * 
		 * @author inxi
		 * handler疑惑:如果我的activity处于暂停状态，如onPause，onStop状态，handler是否还会执行呢？
		 * 
		 * 调试：将程序启动，然后点击HOME键返回桌面，在数据库中添加一条新消息，当获得新消息notification时
		 * 返回到程序中，结果是底部的导航框更新了新消息数目。
		 * 
		 * 调试结果说明handler能处理对应的Looper获取的消息，这个过程是异步的，即我的activity
		 * 没有处于onStart状态它也能获取消息处理消息(activity暂时中止执行)，可以理解成handler开
		 * 了一个线程来处理消息。
		 * 
		 */
		mMainHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) 
			{
				super.handleMessage(msg);
				if(msg.what == 1)
				{
					if(msg.arg1 != 0)
					{
						mMentionTipsMessage.setVisibility(0);
						mMentionTipsMessage.setText(Integer.toString(msg.arg1));
					}
				}
				else if(msg.what == 2)
				{
					mMentionTipsMessage.setVisibility(4);
				}
				System.out.println("PollingService NewMessageCount is:"+PollingService.newMessageCount);
			}
		};
	}
	
	private void initRadios() 
	{
		// TODO Auto-generated method stub
		((RadioButton) findViewById(R.id.radio_home))
		.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_mention))
		.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_person_info))
		.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_more))
		.setOnCheckedChangeListener(this);
	}
	
	private void setupIntents() 
	{
		// TODO Auto-generated method stub
		// 需要说明的是这里为tab添加了标签的时候并没有启动所以activity，OnCreate方法没有调用
		// 只有当mTabHost.setCurrentTabByTag(tagName);的时候才会创建对应的activity
		((RadioButton)findViewById(R.id.radio_home)).setChecked(true);
		mTabHost.addTab(this.buildTabSpec(HOME_TAB, this.mHomeIntent));
		mTabHost.addTab(this.buildTabSpec(MENTION_TAB, this.mMentionIntent));
		mTabHost.addTab(this.buildTabSpec(PERSON_TAB, this.mPersonIntent));
		mTabHost.addTab(this.buildTabSpec(MORE_TAB, this.mMoreIntent));
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
			case R.id.radio_home:
				mTabHost.setCurrentTabByTag(HOME_TAB);
				break;
			case R.id.radio_mention:
				mTabHost.setCurrentTabByTag(MENTION_TAB);
				break;
			case R.id.radio_person_info:
				mTabHost.setCurrentTabByTag(PERSON_TAB);
				break;
			case R.id.radio_more:
				mTabHost.setCurrentTabByTag(MORE_TAB);
				break;
			default:
				break;
			}
		}
	}
	
	@Override  
    public boolean dispatchKeyEvent(KeyEvent event) 
	{  
        if ((event.getAction() == KeyEvent.ACTION_DOWN)  
        		&& (event.getKeyCode() == KeyEvent.KEYCODE_BACK)) 
        {  
            quitDialog();  
        }      
        return super.dispatchKeyEvent(event);  
    }  
  
    private void quitDialog() 
    {  
        new AlertDialog.Builder(this)  
                .setTitle(R.string.alerm_title)  
                .setIcon(null)  
                .setCancelable(false)  
                .setMessage(R.string.alert_quit_confirm)  
                .setPositiveButton(R.string.alert_yes_button,
                		new DialogInterface.OnClickListener() 
                		{  
                            @Override
							public void onClick(DialogInterface dialog,  
                                    int which) {  
                                MainActivity.this.finish();  
                            }  
                        })  
                .setNegativeButton(R.string.alert_no_button,  
                        new DialogInterface.OnClickListener() 
                		{  
                            @Override
							public void onClick(DialogInterface dialog,  
                                    int which) {  
                                dialog.dismiss();  
                            }  
                        }).create().show();  
    } 
    
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    	System.out.println("MainActivity destroy!");
    	//activity销毁，服务应该继续执行
    	PollingUtils.stopPollingService(this, PollingService.class, PollingService.ACTION);
    }
    
}
