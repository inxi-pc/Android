package com.maomao.app.citybuy.activity;

import android.os.Bundle;

import com.maomao.app.citybuy.R;
import com.maomao.app.citybuy.util.Constants;

/**
 * 装修经验界面
 * 
 * 2014-08-27
 * 
 * @author peng
 * 
 */
public class ExperienceActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.experience);
		initView();
	}

	private void initView() {
		initTitleView(R.string.home_experience, true, false);
		initWebView(Constants.Http.EXPERIENCE);
	}

}
