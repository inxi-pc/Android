package com.maomao.app.citybuy.activity.accounts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maomao.app.citybuy.R;
import com.maomao.app.citybuy.activity.BaseActivity;
import com.maomao.app.citybuy.activity.accounts.db.DatabaseHelper;

/**
 * 记账历史
 * 
 * @author DingMaolin
 * 
 */
public class AccountingHistoryActivity extends BaseActivity {

	private LinearLayout loadingLayout;
	private TextView errorTextView;
	private ExpandableListView expandableListView;
	private ExpandableListView expandableListView2;
	private Button timeButton, typeButton;
	private DatabaseHelper databaseHelper;
	private int currentTab = 0;
	private double totalPrice;
	private TextView totalPriceTextView;

	Map<String, List<AccountingData>> timeMap = new HashMap<String, List<AccountingData>>();
	List<String> timeKeyList = new ArrayList<String>();

	Map<String, List<AccountingData>> typeMap = new HashMap<String, List<AccountingData>>();
	List<String> typeKeyList = new ArrayList<String>();

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				loadingLayout.setVisibility(View.GONE);
				errorTextView.setVisibility(View.VISIBLE);
				break;
			case 1:
				loadingLayout.setVisibility(View.GONE);
				totalPriceTextView.setText("总花销:￥" + totalPrice);
				expandableListView.setVisibility(View.VISIBLE);
				AccountingHistoryExpandedAdapter adapter = new AccountingHistoryExpandedAdapter(
						getApplicationContext(), timeMap, timeKeyList, 1);
				expandableListView.setAdapter(adapter);

				AccountingHistoryExpandedAdapter adapter2 = new AccountingHistoryExpandedAdapter(
						getApplicationContext(), typeMap, typeKeyList, 2);
				expandableListView2.setAdapter(adapter2);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accounting_history_layout);
		databaseHelper = new DatabaseHelper(getApplicationContext());

		initTitleView("账目列表", true, false);

		initLoadingView();
		initTab();

		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		expandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						// TODO Auto-generated method stub
						for (int i = 0; i < timeMap.size(); i++) {
							if (groupPosition != i) {
								expandableListView.collapseGroup(i);
							}
						}
					}
				});
		expandableListView.setVisibility(View.GONE);
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				String time = timeKeyList.get(groupPosition);
				List<AccountingData> list = timeMap.get(time);
				AccountingData accountingData = list.get(childPosition);
				Intent intent = new Intent(AccountingHistoryActivity.this,
						AccountingEditActivity.class);
				intent.putExtra("accountingData", accountingData);
				startActivityForResult(intent, 1);
				return true;
			}
		});
		expandableListView2 = (ExpandableListView) findViewById(R.id.expandableListView2);
		expandableListView2.setVisibility(View.GONE);
		expandableListView2
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						// TODO Auto-generated method stub
						for (int i = 0; i < typeMap.size(); i++) {
							if (groupPosition != i) {
								expandableListView2.collapseGroup(i);
							}
						}
					}
				});
		expandableListView2.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				String time = typeKeyList.get(groupPosition);
				List<AccountingData> list = typeMap.get(time);
				AccountingData accountingData = list.get(childPosition);
				Intent intent = new Intent(AccountingHistoryActivity.this,
						AccountingEditActivity.class);
				intent.putExtra("accountingData", accountingData);
				startActivityForResult(intent, 1);
				return false;
			}
		});

		totalPriceTextView = (TextView) findViewById(R.id.tv2);
		totalPriceTextView.setText("总花销:￥0.00");

		loadData();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == 1) {
			timeMap.clear();
			timeKeyList.clear();
			typeMap.clear();
			typeKeyList.clear();
			expandableListView2.setVisibility(View.GONE);
			expandableListView.setVisibility(View.GONE);
			loadingLayout.setVisibility(View.VISIBLE);
			loadData();
		}
	}

	private void initTab() {
		timeButton = (Button) findViewById(R.id.timeButton);
		timeButton.setTextColor(getResources().getColor(R.color.text_color));
		timeButton.setBackgroundResource(R.drawable.account_history_btn_focus);
		typeButton = (Button) findViewById(R.id.typeButton);
		timeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (currentTab == 0) {
					return;
				}
				currentTab = 0;
				timeButton.setTextColor(getResources().getColor(
						R.color.text_color));
				timeButton
						.setBackgroundResource(R.drawable.account_history_btn_focus);
				typeButton.setTextColor(getResources().getColor(
						android.R.color.white));
				typeButton
						.setBackgroundResource(R.drawable.accounting_history_btn_selector);
				expandableListView2.setVisibility(View.GONE);
				expandableListView.setVisibility(View.VISIBLE);
			}
		});
		typeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (currentTab == 1) {
					return;
				}
				currentTab = 1;
				timeButton.setTextColor(getResources().getColor(
						android.R.color.white));
				timeButton
						.setBackgroundResource(R.drawable.accounting_history_btn_selector);
				typeButton.setTextColor(getResources().getColor(
						R.color.text_color));
				typeButton
						.setBackgroundResource(R.drawable.account_history_btn_focus);
				expandableListView.setVisibility(View.GONE);
				expandableListView2.setVisibility(View.VISIBLE);
			}
		});
	}

	private void initLoadingView() {
		loadingLayout = (LinearLayout) findViewById(R.id.loadingLayout);
		loadingLayout.setVisibility(View.VISIBLE);
		errorTextView = (TextView) findViewById(R.id.error_message);
		errorTextView.setVisibility(View.GONE);
		errorTextView.setText("暂无数据");
	}

	private void loadData() {
		new Thread() {
			public void run() {
				List<AccountingData> accountingDatas = databaseHelper
						.getAllAccountingDataByType("");
				if (accountingDatas != null && accountingDatas.size() > 0) {
					for (AccountingData accountingData : accountingDatas) {
						totalPrice = totalPrice
								+ Double.parseDouble(accountingData.getPrice());
						// 时间
						String time = accountingData.getTime();
						if (timeMap.containsKey(time)) {
							timeMap.get(time).add(accountingData);
						} else {
							List<AccountingData> tempList = new ArrayList<AccountingData>();
							tempList.add(accountingData);
							timeMap.put(time, tempList);
							timeKeyList.add(time);
						}
						// 列表
						String type = accountingData.getType();
						if (typeMap.containsKey(type)) {
							typeMap.get(type).add(accountingData);
						} else {
							List<AccountingData> tempList = new ArrayList<AccountingData>();
							tempList.add(accountingData);
							typeMap.put(type, tempList);
							typeKeyList.add(type);
						}
					}
					handler.sendEmptyMessage(1);
				} else {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();
	}

}
