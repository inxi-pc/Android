/**
 * 
 */
package com.inxi.PersonCenter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.inxi.R;

/**
 * @author linxi
 *
 */
public class FastIdeaAnalysis extends Activity
{
	private EditText mText;
	private Button mBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_idea_analysis);
		
		mText = (EditText) findViewById(R.id.editText1);
		mBtn = (Button) findViewById(R.id.button1);
		
		mBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				String tx = mText.getText().toString();
				System.out.println(tx);
				
			}	
		});
		
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	public void analysis(String s)
	{
		
	}
	
	class treeNode
	{
		String nodeName;
	}
}
