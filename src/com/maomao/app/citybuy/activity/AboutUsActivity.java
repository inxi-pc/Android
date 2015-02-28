package com.maomao.app.citybuy.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.maomao.app.citybuy.R;

/**
 * 关于我们界面
 * 
 * 2014-09-04
 * 
 * @author peng
 * 
 */
public class AboutUsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		initView();
	}

	private void initView() {
		initTitleView(R.string.setting_about_us, true, false);
	}

	public void onCallPhone(View view) {
		try {
			String phone = "4006868911";
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ phone));
			startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, "无法拨打电话", Toast.LENGTH_LONG).show();
		}

	}

	public void onCallEmail(View view) {
		try {
			String email = "app@citytogo.com.cn";
			Uri uri = Uri.parse("mailto:" + email);
			Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
			startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, "无法发送邮件", Toast.LENGTH_LONG).show();
		}
	}

}
