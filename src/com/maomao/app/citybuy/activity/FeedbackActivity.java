package com.maomao.app.citybuy.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.maomao.app.citybuy.R;
import com.maomao.app.citybuy.task.FeedbackTask;

/**
 * 用户反馈界面
 * 
 * 2014-09-04
 * 
 * @author peng
 * 
 */
public class FeedbackActivity extends BaseActivity {

	private EditText etContact;
	private EditText etContent;
	private FeedbackTask feedbackTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		initView();
	}

	@Override
	protected void onDestroy() {
		if (feedbackTask != null && !feedbackTask.isCancelled())
			feedbackTask.cancel(true);
		super.onDestroy();
	}

	private void initView() {
		initTitleView(R.string.setting_feedback, true, false);
		etContact = (EditText) findViewById(R.id.et_feedback_contact);
		etContent = (EditText) findViewById(R.id.et_feedback_content);
		findViewById(R.id.btn_feedback_commit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						commit();
					}
				});
	}

	private void commit() {
		String contact = etContact.getText().toString();
		if (TextUtils.isEmpty(contact.trim())) {
			toast("请留下您的联系方式");
			return;
		}
		String content = etContent.getText().toString();
		if (TextUtils.isEmpty(content.trim())) {
			toast("请填写反馈内容");
			return;
		}
		showProgressDialog();
		feedbackTask = new FeedbackTask(this, handler, contact, content);
		feedbackTask.execute();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (isFinishing())
				return;
			closeProgressDialog();
			switch (msg.what) {
			case FeedbackTask.RESPONSE_ERROR:
				toast(msg.obj.toString());
				break;
			case FeedbackTask.RESPONSE_OK:
				toast("提交成功");
				finish();
				break;
			}
		}
	};

}
