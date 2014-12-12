package com.inxi.Utility.PullRefresh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.inxi.R;

/**
 * @author linxi
 *
 */
public class Container extends RelativeLayout implements OnTouchListener
{
	private int mCurrementState = -1;
	private static final int mCriticalValue = 10;
	private static final int ON_BEGINING = 0;
	private static final int ON_PULLING = 1;
	private static final int WAITING_REFRESH = 2;
	private static final int ON_REFRESHING = 3;

	private View mHeaderView;
	private View mBody;
	private ListView mContentView;
	private List<HashMap<String, String>> mPList;
	private SimpleAdapter mPAdapter;
	private Handler mRefreshHandler;
	private int mHeaderHeight;
	private float mActionDown = 0;

	public Container(Context context)
	{
		super(context);
	}
	
	public Container(Context context,View v1,ListView v2,SimpleAdapter s,List l) 
	{
		super(context);
		mHeaderView = LayoutInflater.from(context).inflate(R.layout.fresh_header_layout,null);
		mBody = v1;
		mContentView = v2;
		mPList = l;
		mPAdapter = s;
		mCurrementState = ON_BEGINING;
		mContentView.setOnTouchListener(this);
		addView(mHeaderView,0);
		addView(mBody,1);
	}
		
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		float offset = 0.0f;
		v.performClick();
		System.out.println("touch");
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			mActionDown = event.getRawY();
			break;
			
		case MotionEvent.ACTION_MOVE:
			offset = event.getRawY() - mActionDown;
			mActionDown = event.getRawY();

			if(setIsEnablePull() == true)
			{
				System.out.println(mCurrementState);
				return onPull(offset);
			}
			break;
			
		case MotionEvent.ACTION_UP:
			offset = 0;
			onPull(0);
			break;
		}
		return false;
	}
	
	private boolean setIsEnablePull()
	{
		View childFirst = mContentView.getChildAt(0);
		if(childFirst == null || childFirst.getTop() == 0)
		{
			return true;
		}
		return false;
	}

	private boolean onPull(float offset)
	{
		RelativeLayout.LayoutParams hL = (LayoutParams) mHeaderView.getLayoutParams();
		RelativeLayout.LayoutParams cL = (LayoutParams) mContentView.getLayoutParams();
		int headerDistanceTop  = mHeaderView.getTop();
	
		if(mCurrementState == ON_BEGINING)
		{
			setAnimation(ON_BEGINING);
			if(offset >= 0)
			{
				hL.topMargin += offset;
				cL.topMargin += offset;
				mCurrementState = ON_PULLING;
			}
			return false;
		}
		
		else if(mCurrementState == ON_PULLING)
		{
			if(offset <= 0)
			{
				if(headerDistanceTop < mCriticalValue)
				{
					hL.topMargin = -mHeaderHeight;
					cL.topMargin = 0;
					mCurrementState = ON_BEGINING;
				}

				if(headerDistanceTop >= mCriticalValue)
				{
					hL.topMargin = mCriticalValue - 10;
					cL.topMargin = mCriticalValue - 10 + mHeaderHeight;
					setAnimation(ON_REFRESHING);
					mCurrementState = ON_REFRESHING;
				}
			}
			if(offset > 0)
			{
				hL.topMargin += offset;
				cL.topMargin += offset;
				mCurrementState = ON_PULLING ;

				if(headerDistanceTop >= mCriticalValue)
				{
					setAnimation(ON_PULLING);
					mCurrementState = WAITING_REFRESH;
				}
			}
		}
		
		else if(mCurrementState == WAITING_REFRESH)
		{
			Timer t = new Timer();
			mRefreshHandler = new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					refreshData();
					mCurrementState = ON_REFRESHING;
					onPull(-1);
				}
			};
			if(offset <= 0)
			{
				hL.topMargin = mCriticalValue - 10;
				cL.topMargin = mCriticalValue - 10 + mHeaderHeight;
				setAnimation(ON_REFRESHING);
				TimerTask tt = new TimerTask(){
					@Override
					public void run() {
						Message m = Message.obtain();
						mRefreshHandler.sendEmptyMessage(1);
					};
				};
				t.schedule(tt, 1000);
			}
			if(offset > 0)
			{
				hL.topMargin += offset;
				cL.topMargin += offset;
				mCurrementState = WAITING_REFRESH;
				t.cancel();
			}	
		}
		
		else if(mCurrementState == ON_REFRESHING)
		{
			if(offset > 0)
			{
				hL.topMargin += offset;
				cL.topMargin += offset;
				mCurrementState = ON_PULLING;
			}
			if(offset <= 0)
			{
				hL.topMargin = -mHeaderHeight;
				cL.topMargin = 0;
				mCurrementState = ON_BEGINING;
			}
		}

		mContentView.setLayoutParams(cL);
		mHeaderView.setLayoutParams(hL);
		return true;
	}
	
	private void setAnimation(int state)
	{
		ImageView arrow = (ImageView)mHeaderView.findViewById(R.id.header_arrow);
		ProgressBar p = (ProgressBar)mHeaderView.findViewById(R.id.header_reflesh);

		float piontX = arrow.getWidth() / 2;
		float piontY = arrow.getHeight() / 2;
		float fromDegrees = 0f;
		float toDegrees = 0f;
		RotateAnimation r;
		
		if(state == ON_BEGINING)
		{
			arrow.clearAnimation();
			arrow.setVisibility(View.VISIBLE);
			p.setVisibility(View.GONE);
		}
		
		if(state == ON_PULLING)
		{
			arrow.setVisibility(View.VISIBLE);
			p.setVisibility(View.GONE);
			fromDegrees = 0f;
			toDegrees = 180f;
			r = new RotateAnimation(fromDegrees,toDegrees,piontX,piontY);
			r.setDuration(200);
			r.setFillAfter(true);
			arrow.setAnimation(r);
		}

		if(state == ON_REFRESHING)
		{
			arrow.clearAnimation();
			arrow.setVisibility(View.GONE);
			p.setVisibility(View.VISIBLE);
		}
	}
	
	public static List<HashMap<String, String>> getDataFromRemote()
	{
		String url = "http://www.guanghezhang.com/getNoteInfo.php";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		
		try 
		{
			HttpResponse response;
			response = client.execute(post);
			/**
			 *  json格式:{NoteTitle:value,NoteContent:value,NoteTime:value,UserId:value}
			 */
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
			{
				System.out.println("连接成功");
				JSONArray content = new JSONArray(EntityUtils.toString(response.getEntity()));
				List<HashMap<String, String>> res = new ArrayList<HashMap<String,String>>();
				for(int i=0;i<content.length();i++)
				{
					JSONObject j = content.getJSONObject(i);
					Iterator it = j.keys();
					HashMap<String,String> m = new HashMap<String,String>();
					while(it.hasNext())
					{
						m.put((String) it.next(),(String) j.get((String) it.next()));
					}
					res.add(m);
				}
				return res;
			} 
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return null; 
	}
	
	public static List<HashMap<String, String>> getDataFromLocal()
	{
		List<HashMap<String, String>> res = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> m = new HashMap<String,String>();
		m.put("NoteTitle", "test");
		m.put("UserId", "test");
		m.put("NoteContent", "test");
		res.add(m);
		return res;
	}
	
	private void refreshData()
	{
		List<HashMap<String, String>> data = Container.getDataFromLocal();
		if(data != null)
		{
			Iterator<HashMap<String, String>> it = data.iterator();
			while(it.hasNext())
			{
				mPList.add((HashMap<String, String>) it.next());
			}
		}
		mPAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onLayout(boolean changed,int l,int t,int r,int b)
	{
		super.onLayout(changed, l, t, r, b);
		
		if(changed == true)
		{
			mHeaderHeight = mHeaderView.getHeight();
			RelativeLayout.LayoutParams rL = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			rL.topMargin = -mHeaderHeight;
			mHeaderView.setLayoutParams(rL);
		}
	}
}
