/**
 * 
 */
package com.inxi.Mention;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.inxi.Utility.PushData.PollingService;
import com.inxi.Utility.PushData.PollingUtils;

/**
 * @author linxi
 *
 */
public class AutoStartMentionService extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		// TODO Auto-generated method stub
		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
		{
			SharedPreferences loginSp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
			if(loginSp.getString("userId", null) != null)
			{
				//PollingUtils.startPollingService(context, 4, PollingService.class, PollingService.ACTION);
				//PollingService.startServiceFromBoot = true;
			}
		}
	}

}
