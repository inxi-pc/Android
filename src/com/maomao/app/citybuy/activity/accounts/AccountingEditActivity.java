package com.maomao.app.citybuy.activity.accounts;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.maomao.app.citybuy.R;
import com.maomao.app.citybuy.activity.BaseActivity;
import com.maomao.app.citybuy.activity.accounts.db.DatabaseHelper;

/**
 * 记账
 * 
 * @author DingMaolin
 * 
 */
public class AccountingEditActivity extends BaseActivity {

	private String[] items = { "人工", "主材", "家具", "家电", "软装", "辅料", "其他" };
	private TextView monthTextView, dayTextView;
	private TextView typeTextView;
	private EditText moneyEditText, remarkEditText;
	private String currentTime, currentWeek;
	private DatabaseHelper databaseHelper;
	private AccountingData accountingData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accounting_edit_layout);
		accountingData = (AccountingData) getIntent().getSerializableExtra(
				"accountingData");
		databaseHelper = new DatabaseHelper(getApplicationContext());
		currentTime = accountingData.getTime();
		currentWeek = DateUtil.getWeek(currentTime);

		initTitleView("编辑账目", true, false);

		Button saveButton = (Button) findViewById(R.id.save_btn);
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
				accountingData.setPrice(price);
				accountingData.setRemark(remark);
				accountingData.setType(typeTextView.getText().toString());
				accountingData.setTime(currentTime);
				databaseHelper.updateAccountingData(accountingData);
				setResult(RESULT_OK);
				finish();
			}
		});
		Button deleteButton = (Button) findViewById(R.id.delete_btn);
		deleteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				databaseHelper.deleteAccountingData(accountingData.getId());
				setResult(RESULT_OK);
				finish();
			}
		});
		initWeiget();
	}

	private void initWeiget() {
		monthTextView = (TextView) findViewById(R.id.monthTV);
		dayTextView = (TextView) findViewById(R.id.dayTV);
		monthTextView.setText(currentTime.substring(
				currentTime.indexOf(".") + 1).replace(".", "-"));
		// monthTextView.setText(currentTime);
		dayTextView.setText(currentWeek);
		typeTextView = (TextView) findViewById(R.id.typeTV);
		typeTextView.setText(accountingData.getType());
		moneyEditText = (EditText) findViewById(R.id.moneyEdit);
		moneyEditText.setText(accountingData.getPrice());
		remarkEditText = (EditText) findViewById(R.id.remarkEdit);
		remarkEditText.setText(accountingData.getRemark());
		remarkEditText.addTextChangedListener(textWatcher);
		ImageButton xialaBtn = (ImageButton) findViewById(R.id.xiala_btn);
		xialaBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String time = currentTime.replace(".", "");
				int year = Integer.parseInt(time.substring(0, 4));
				int monthOfYear = Integer.parseInt(time.substring(4, 6)) - 1;
				int dayOfMonth = Integer.parseInt(time.substring(6, 8));

				new DatePickerDialog(AccountingEditActivity.this,
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
								// monthTextView.setText(currentTime);
								dayTextView.setText(currentWeek);
								monthTextView.setText(currentTime.substring(
										currentTime.indexOf(".") + 1).replace(
										".", "-"));
							}
						}, year, monthOfYear, dayOfMonth).show();
			}
		});
		ImageButton xialaBtn2 = (ImageButton) findViewById(R.id.xiala_btn2);
		xialaBtn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(AccountingEditActivity.this).setItems(
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
}
