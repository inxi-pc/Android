package com.maomao.app.citybuy.activity;

import android.os.Bundle;

import com.maomao.app.citybuy.R;
import com.maomao.app.citybuy.util.Constants;

/**
 * 家居美图界面
 * 
 * 2014-08-27
 * 
 * @author peng
 * 
 */
public class PicturesActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pictures);
		initView();
	}

	private void initView() {
		initTitleView(R.string.home_pictures, true, false);
		initWebView(Constants.Http.PICTURES);
	}

}
