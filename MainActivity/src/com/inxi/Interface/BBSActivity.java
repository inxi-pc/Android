package com.inxi.Interface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.inxi.R;
import com.inxi.BBS.ToolBarActivity;
import com.inxi.Utility.PullRefresh.Container;

/**
 * @author linxi
 *
 **/
public class BBSActivity extends Activity implements OnItemClickListener
{	
	public final String TAG = "BBS";
	private FrameLayout mNoteListLayout;
	private ListView mListBBS;
	private SimpleAdapter mAdapter;
	private List<HashMap<String,Object>> mList;
	private Container mContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_activity);
		
		mList = new ArrayList<HashMap<String, Object>>();
		mAdapter = new SimpleAdapter(this,mList,R.layout.bbs_listview_row,
				new String[]{"NoteTitle","UserId","NoteContent"},
				new int[]{R.id.note_list_title,R.id.note_list_author,R.id.note_list_content});
		
		RelativeLayout body = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.bbs_listview,null);
		mListBBS = (ListView) body.findViewById(R.id.list_bbs);
		mContainer = new Container(this,body,mListBBS,mAdapter,mList);
		mNoteListLayout = (FrameLayout) findViewById(R.id.bbs_list);
		mNoteListLayout.addView(mContainer);
		
		mListBBS.setAdapter(mAdapter);
		mListBBS.setOnItemClickListener(this);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		HashMap<String,String> map = (HashMap<String, String>) mAdapter.getItem(arg2);
		String[] title= new String[1];
        title[0] = map.get("title");
        Intent i = new Intent(BBSActivity.this,ToolBarActivity.class);
        startActivity(i);
	}
}
