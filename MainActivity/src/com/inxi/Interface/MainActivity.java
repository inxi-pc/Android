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
		
		//����������ʱ���������Ϣ����̺߳�AlarmManager��ʱ��ȡ������Ϣ
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
		 * handler�ɻ�:����ҵ�activity������ͣ״̬����onPause��onStop״̬��handler�Ƿ񻹻�ִ���أ�
		 * 
		 * ���ԣ�������������Ȼ����HOME���������棬�����ݿ������һ������Ϣ�����������Ϣnotificationʱ
		 * ���ص������У�����ǵײ��ĵ��������������Ϣ��Ŀ��
		 * 
		 * ���Խ��˵��handler�ܴ����Ӧ��Looper��ȡ����Ϣ������������첽�ģ����ҵ�activity
		 * û�д���onStart״̬��Ҳ�ܻ�ȡ��Ϣ������Ϣ(activity��ʱ��ִֹ��)����������handler��
		 * ��һ���߳���������Ϣ��
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
		// ��Ҫ˵����������Ϊtab����˱�ǩ��ʱ��û����������activity��OnCreate����û�е���
		// ֻ�е�mTabHost.setCurrentTabByTag(tagName);��ʱ��Żᴴ����Ӧ��activity
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
    	//activity���٣�����Ӧ�ü���ִ��
    	PollingUtils.stopPollingService(this, PollingService.class, PollingService.ACTION);
    }
    
}
