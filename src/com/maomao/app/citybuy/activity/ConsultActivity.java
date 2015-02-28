package com.maomao.app.citybuy.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ListView;

import com.maomao.app.citybuy.R;
import com.maomao.app.citybuy.adapter.ConsultHistoryAdapter;
import com.maomao.app.citybuy.entity.Consult;
import com.maomao.app.citybuy.task.BaseTask;
import com.maomao.app.citybuy.task.ConsultHistoryTask;
import com.maomao.app.citybuy.task.ConsultTask;
import com.maomao.app.citybuy.util.DecorateApplication;

/**
 * 客服咨询界面
 * 
 * 2014-09-04
 * 
 * @author peng
 * 
 */
public class ConsultActivity extends BaseActivity {

	private static final int MESSAGE_REFRESH_LIST = 4;
	private static final int REFRESH_LIST_INTERVAL = 10000;

	private EditText etInput;
	private ListView lvConsultList;
	private ConsultHistoryTask consultHistoryTask;
	private ConsultTask consultTask;
	private ConsultHistoryAdapter consultHistoryAdapter;
	private List<Consult> consults;

	private Boolean isSetListSelection = true;
	private String receiver;
	private String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consult);
		receiver = "管家01";
		initView();
		loading();
		getConsultHistory();
	}

	@Override
	protected void onDestroy() {
		if (consultHistoryTask != null && !consultHistoryTask.isCancelled())
			consultHistoryTask.cancel(true);
		if (consultTask != null && !consultTask.isCancelled())
			consultTask.cancel(true);
		super.onDestroy();
	}

	private void initView() {
		initTitleView(R.string.home_consult, true, false);
		initListView();

		etInput = (EditText) findViewById(R.id.et_consult_input);
		etInput.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (arg1) {
					etInput.setHint("");
				} else {
					etInput.setHint("请输入问题或关键词");
				}
			}
		});
		findViewById(R.id.btn_consult_send).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						consult();
					}
				});
	}

	private void initListView() {
		lvConsultList = (ListView) findViewById(R.id.lv_consult_list);
		consults = new ArrayList<Consult>();
		consultHistoryAdapter = new ConsultHistoryAdapter(this, consults);
		lvConsultList.setAdapter(consultHistoryAdapter);
	}

	private void getConsultHistory() {
		consultHistoryTask = new ConsultHistoryTask(this, handler);
		consultHistoryTask.execute();
	}

	private void consult() {
		content = etInput.getText().toString();
		if (TextUtils.isEmpty(content.trim())) {
			toast("请输入发送内容");
			return;
		}
		showProgressDialog();
		consultTask = new ConsultTask(this, handler, receiver, content);
		consultTask.execute();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (isFinishing())
				return;
			loaded();
			closeProgressDialog();
			switch (msg.what) {
			case BaseTask.RESPONSE_ERROR:
				toast(msg.obj.toString());
				break;
			case ConsultHistoryTask.RESPONSE_OK:
				consults.clear();
				consults.addAll((List<Consult>) msg.obj);
				if (!consults.isEmpty()) {
					receiver = consults.get(0).getSender();
					consultHistoryAdapter.notifyDataSetChanged();
				}
				if (isSetListSelection) {
					lvConsultList.setSelection(consults.size() - 1);
					isSetListSelection = false;
				}
				handler.sendEmptyMessageDelayed(MESSAGE_REFRESH_LIST,
						REFRESH_LIST_INTERVAL);
				break;
			case ConsultTask.RESPONSE_OK:
				etInput.setText("");
				Consult consult = new Consult();
				consult.setTime(String.valueOf(System.currentTimeMillis() / 1000));
				consult.setSender(DecorateApplication.getInstance()
						.getDeviceId());
				consult.setContent(content);
				consults.add(consult);
				consultHistoryAdapter.notifyDataSetChanged();
				lvConsultList.setSelection(consults.size() - 1);
				break;
			case MESSAGE_REFRESH_LIST:
				getConsultHistory();
				break;
			}
		}
	};

}
