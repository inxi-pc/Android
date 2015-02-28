package com.maomao.app.citybuy.activity.accounts;

import java.util.List;

import com.maomao.app.citybuy.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountingListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<AccountingData> list;
	
	public AccountingListAdapter(Context context, List<AccountingData> list){
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ChildHorldView childHorldView;
		if(view == null){
			childHorldView = new ChildHorldView();
			view = inflater.inflate(R.layout.accounting_list_item_layout, null);
			childHorldView.itemLayout = (RelativeLayout) view.findViewById(R.id.itemLayout);
			childHorldView.typeTextView = (TextView) view.findViewById(R.id.typeTV);
			childHorldView.remarkTextView = (TextView) view.findViewById(R.id.remarkTV);
			childHorldView.priceTextView = (TextView) view.findViewById(R.id.priceTV);
			view.setTag(childHorldView);
		}else{
			childHorldView = (ChildHorldView) view.getTag();
		}
		AccountingData accountingData = list.get(position);
		childHorldView.itemLayout.setBackgroundResource(R.drawable.accountging_list_selector);
		childHorldView.typeTextView.setText(accountingData.getType());
		childHorldView.remarkTextView.setText(accountingData.getRemark());
		childHorldView.priceTextView.setText("￥ "+accountingData.getPrice());
		return view;
	}

	class ChildHorldView{
		RelativeLayout itemLayout;
		TextView typeTextView;
		TextView remarkTextView;
		TextView priceTextView;
	}
}
