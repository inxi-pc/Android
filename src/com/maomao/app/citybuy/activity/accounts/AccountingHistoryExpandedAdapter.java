package com.maomao.app.citybuy.activity.accounts;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maomao.app.citybuy.R;

public class AccountingHistoryExpandedAdapter extends BaseExpandableListAdapter {

	private LayoutInflater inflater;
	private List<String> list;
	private Map<String, List<AccountingData>> map;
	private int type = 1;

	public AccountingHistoryExpandedAdapter(Context context,
			Map<String, List<AccountingData>> map, List<String> list, int type) {
		this.inflater = LayoutInflater.from(context);
		this.map = map;
		this.list = list;
		this.type = type;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		String key = list.get(groupPosition);
		return map.get(key).get(childPosition);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup arg4) {
		// TODO Auto-generated method stub
		ChildHorldView childHorldView;
		if (view == null) {
			childHorldView = new ChildHorldView();
			view = inflater
					.inflate(R.layout.accounting_child_item_layout, null);
			childHorldView.typeTextView = (TextView) view
					.findViewById(R.id.typeTV);
			childHorldView.remarkTextView = (TextView) view
					.findViewById(R.id.remarkTV);
			childHorldView.priceTextView = (TextView) view
					.findViewById(R.id.priceTV);
			view.setTag(childHorldView);
		} else {
			childHorldView = (ChildHorldView) view.getTag();
		}
		AccountingData accountingData = (AccountingData) getChild(
				groupPosition, childPosition);
		if (type == 1) {
			childHorldView.typeTextView.setText(accountingData.getType());
		} else {
			String time = accountingData.getTime();
			String week = DateUtil.getWeek(time);
			childHorldView.typeTextView.setText(time + week);
		}

		childHorldView.remarkTextView.setText(accountingData.getRemark());
		childHorldView.priceTextView.setText("￥ " + accountingData.getPrice());
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		String key = list.get(groupPosition);
		return map.get(key).size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int position, boolean isExpanded, View view,
			ViewGroup arg3) {
		// TODO Auto-generated method stub
		GroupHorldView groupHorldView;
		if (view == null) {
			groupHorldView = new GroupHorldView();
			view = inflater
					.inflate(R.layout.accounting_group_item_layout, null);
			groupHorldView.itemLayout = (RelativeLayout) view
					.findViewById(R.id.itemLayout);
			groupHorldView.timeTextView = (TextView) view
					.findViewById(R.id.timeTV);
			groupHorldView.weekTextView = (TextView) view
					.findViewById(R.id.weekTV);
			groupHorldView.priceTextView = (TextView) view
					.findViewById(R.id.priceTV);
			view.setTag(groupHorldView);
		} else {
			groupHorldView = (GroupHorldView) view.getTag();
		}
		if (isExpanded) {
			groupHorldView.itemLayout.setBackgroundResource(R.drawable.huang);
			groupHorldView.timeTextView.setTextSize(26);
			groupHorldView.priceTextView.setTextSize(26);
			groupHorldView.weekTextView.setPadding(0, 0, 0, 8);
		} else {
			groupHorldView.itemLayout
					.setBackgroundResource(R.drawable.accountging_list_selector);
			groupHorldView.timeTextView.setTextSize(17);
			groupHorldView.priceTextView.setTextSize(17);
			groupHorldView.weekTextView.setPadding(0, 0, 0, 0);
		}
		if (type == 1) {
			String time1 = (String) getGroup(position);
			String week = DateUtil.getWeek(time1);
			String time = time1.substring(time1.indexOf(".") + 1);
			if (isExpanded) {
				groupHorldView.timeTextView.setText(time.replace(".", "-"));
			} else {
				groupHorldView.timeTextView.setText(time1.replace(".", "-"));
			}
			groupHorldView.weekTextView.setText(week);
			double price = 0;
			List<AccountingData> list = map.get(time1);
			for (AccountingData accountingData : list) {
				price = price + Double.parseDouble(accountingData.getPrice());
			}
			groupHorldView.priceTextView.setText("￥" + price);
		} else {
			String type = (String) getGroup(position);
			groupHorldView.timeTextView.setText(type);
			groupHorldView.weekTextView.setVisibility(View.GONE);
			double price = 0;
			List<AccountingData> list = map.get(type);
			for (AccountingData accountingData : list) {
				price = price + Double.parseDouble(accountingData.getPrice());
			}
			groupHorldView.priceTextView.setText("￥" + price);
		}
		return view;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	class GroupHorldView {
		RelativeLayout itemLayout;
		TextView timeTextView;
		TextView weekTextView;
		TextView priceTextView;
	}

	class ChildHorldView {
		TextView typeTextView;
		TextView remarkTextView;
		TextView priceTextView;
	}

}
