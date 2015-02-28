package com.maomao.app.citybuy.activity.accounts;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.maomao.app.citybuy.R;
import com.maomao.app.citybuy.activity.BaseActivity;
import com.maomao.app.citybuy.activity.accounts.db.DatabaseHelper;
import com.maomao.app.citybuy.util.CommonUtil;
import com.maomao.app.citybuy.util.LogUtil;

/**
 * 记账
 * 
 * @author DingMaolin
 * 
 */
public class AccountingActivity extends BaseActivity {

	private String[] items = { "人工", "主材", "家具", "家电", "软装", "辅料", "其他" };
	private TextView monthTextView, dayTextView;
	private TextView typeTextView;
	private EditText moneyEditText, remarkEditText;
	private String todayTime, currentTime, currentWeek;
	private DatabaseHelper databaseHelper;
	private ImageView imageView;
	private ListView listView;
	private AccountingListAdapter adapter;

	private ProgressDialog progressDialog;
	private List<AccountingData> list = new ArrayList<AccountingData>();
	private TextView todayCashTextView;
	private double todayTotal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accounting_layout);
		databaseHelper = new DatabaseHelper(getApplicationContext());
		currentTime = DateUtil.getCurrentTime();
		todayTime = currentTime;
		currentWeek = DateUtil.getWeek(currentTime);

		initTitleView(R.string.home_accounts, true, true);
		ibMore.setImageResource(R.drawable.accounting_title_right_icon);
		ibMore.setPadding(CommonUtil.dip2px(this, 12), 0,
				CommonUtil.dip2px(this, 12), 0);
		ibMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(AccountingActivity.this,
						AccountingHistoryActivity.class));
			}
		});

		Button saveButton = (Button) findViewById(R.id.save_btn);
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String price = moneyEditText.getText().toString();
				if (TextUtils.isEmpty(price)) {
					moneyEditText.setError("消费金额不能为空");
					moneyEditText.requestFocus();
					return;
				}
				String remark = remarkEditText.getText().toString();
				if (TextUtils.isEmpty(remark)) {
					remarkEditText.setError("备注不能为空");
					remarkEditText.requestFocus();
					return;
				}
				AccountingData accountingData = new AccountingData();
				accountingData.setPrice(price);
				accountingData.setRemark(remark);
				accountingData.setType(typeTextView.getText().toString());
				accountingData.setTime(currentTime);
				databaseHelper.saveAccountingData(accountingData);
				if (list.size() == 0) {
					imageView.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
					list.add(accountingData);
					adapter = new AccountingListAdapter(
							getApplicationContext(), list);
					listView.setAdapter(adapter);
				} else {
					list.add(0, accountingData);
					adapter.notifyDataSetChanged();
				}

				todayTotal = todayTotal + Double.parseDouble(price);
				todayCashTextView.setText("￥" + todayTotal);
				moneyEditText.setText(null);
				remarkEditText.setText(null);
				remarkEditText.requestFocus();
				// 隐藏键盘
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(remarkEditText.getWindowToken(), 0);
			}
		});

		initWeiget();
		imageView = (ImageView) findViewById(R.id.image);
		listView = (ListView) findViewById(R.id.listView);
		adapter = new AccountingListAdapter(getApplicationContext(), list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				AccountingData accountingData = list.get(arg2);
				Intent intent = new Intent(AccountingActivity.this,
						AccountingEditActivity.class);
				intent.putExtra("accountingData", accountingData);
				// startUI(intent, false);
				// startUIForResult(intent, 1);
				startActivityForResult(intent, 1);
			}
		});

		loadData(todayTime);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == 1) {
			loadData(currentTime);
		}
	}

	private void initWeiget() {
		monthTextView = (TextView) findViewById(R.id.monthTV);
		dayTextView = (TextView) findViewById(R.id.dayTV);
		typeTextView = (TextView) findViewById(R.id.typeTV);
		moneyEditText = (EditText) findViewById(R.id.moneyEdit);
		moneyEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (arg1) {
					moneyEditText.setHint("");
					moneyEditText.setBackgroundResource(R.drawable.frame_bg2);
				} else {
					moneyEditText.setHint("0.00");
					moneyEditText.setBackgroundResource(0);
				}
			}
		});
		remarkEditText = (EditText) findViewById(R.id.remarkEdit);
		remarkEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (arg1) {
					remarkEditText.setHint("");
					remarkEditText.setBackgroundResource(R.drawable.frame_bg2);
				} else {
					remarkEditText.setHint("请输入备注");
					remarkEditText.setBackgroundResource(0);
				}
			}
		});
		remarkEditText.addTextChangedListener(textWatcher);
		monthTextView.setText(currentTime.substring(
				currentTime.indexOf(".") + 1).replace(".", "-"));
		dayTextView.setText(currentWeek);
		View xialaBtn = findViewById(R.id.xiala_layout);
		xialaBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String time = currentTime.replace(".", "");
				int year = Integer.parseInt(time.substring(0, 4));
				int monthOfYear = Integer.parseInt(time.substring(4, 6)) - 1;
				int dayOfMonth = Integer.parseInt(time.substring(6, 8));

				new DatePickerDialog(AccountingActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								monthOfYear = monthOfYear + 1;
								String month = "" + monthOfYear;
								if (monthOfYear < 10) {
									month = "0" + monthOfYear;
								}
								String day = "" + dayOfMonth;
								if (dayOfMonth < 10) {
									day = "0" + dayOfMonth;
								}
								currentTime = year + "." + month + "." + day;
								currentWeek = DateUtil.getWeek(currentTime);
								monthTextView.setText(month + "-" + day);
								dayTextView.setText(currentWeek);
								loadData(currentTime);
							}
						}, year, monthOfYear, dayOfMonth).show();
			}
		});
		typeTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(AccountingActivity.this).setItems(
						items, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								typeTextView.setText(items[which]);
							}
						}).show();
			}
		});
		ImageButton xialaBtn2 = (ImageButton) findViewById(R.id.xiala_btn2);
		xialaBtn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(AccountingActivity.this).setItems(
						items, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								typeTextView.setText(items[which]);
							}
						}).show();
			}
		});
		todayCashTextView = (TextView) findViewById(R.id.todayCash);
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			try {
				boolean overMaxLength = false;
				int len = s.toString().getBytes("GBK").length;
				overMaxLength = (len > 200) ? true : false;
				if (overMaxLength) {
					String inputStr = remarkEditText.getText().toString();
					remarkEditText.setText(inputStr.subSequence(0, 100));
					remarkEditText.setError("备注最多可录入100字");
					remarkEditText.requestFocus();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

	/**
	 * 加载当天数据
	 */
	private void loadData(final String time) {
		list.clear();
		adapter.notifyDataSetChanged();
		todayTotal = 0.0;
		// new Thread() {
		// public void run() {
		LogUtil.D("time:" + time);
		List<AccountingData> list = databaseHelper.getTodayAccountingData(time);
		if (list != null && list.size() > 0) {
			for (AccountingData accountingData : list) {
				todayTotal = todayTotal
						+ Double.parseDouble(accountingData.getPrice());
			}
		}

		Message msg = new Message();
		msg.obj = list;
		msg.what = 1;
		handler.sendMessage(msg);
		// };
		// }.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isFinishing()) {
				return;
			}
			switch (msg.what) {
			case 1:
				List<AccountingData> tempList = (List<AccountingData>) msg.obj;
				todayCashTextView.setText("￥" + todayTotal);
				if (tempList != null && tempList.size() > 0) {
					imageView.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
					list.clear();
					list.addAll(tempList);
					adapter.notifyDataSetChanged();
				} else {
					imageView.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				}
			}
		};
	};
}
