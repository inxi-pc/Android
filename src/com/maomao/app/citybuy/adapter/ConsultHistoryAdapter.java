package com.maomao.app.citybuy.adapter;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maomao.app.citybuy.R;
import com.maomao.app.citybuy.entity.Consult;
import com.maomao.app.citybuy.util.DecorateApplication;

/**
 * 咨询历史记录适配器
 * 
 * 2014-09-19
 * 
 * @author peng
 * 
 */
public class ConsultHistoryAdapter extends BaseAdapter {

	private List<Consult> consults;
	private LayoutInflater layoutInflater;
	private String deviceId;
	private Integer currentYear;

	public ConsultHistoryAdapter(final Context context,
			final List<Consult> consults) {
		this.consults = consults;
		this.layoutInflater = LayoutInflater.from(context);
		this.deviceId = DecorateApplication.getInstance().getDeviceId();
		this.currentYear = Calendar.getInstance().get(Calendar.YEAR);
	}

	@Override
	public int getCount() {
		return consults.size();
	}

	@Override
	public Object getItem(int arg0) {
		return consults.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder viewHolder;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.consult_lv_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tvTime = (TextView) view
					.findViewById(R.id.tv_consult_lv_item_time);
			viewHolder.tvSystemText = (TextView) view
					.findViewById(R.id.tv_consult_lv_item_system_text);
			viewHolder.tvCustomerText = (TextView) view
					.findViewById(R.id.tv_consult_lv_item_customer_text);
			viewHolder.llSystemWrapper = (LinearLayout) view
					.findViewById(R.id.ll_consult_lv_item_system_wrapper);
			viewHolder.llCustomerWrapper = (LinearLayout) view
					.findViewById(R.id.ll_consult_lv_item_customer_wrapper);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		Consult consult = consults.get(position);
		if (consult.getSender().equals(deviceId)) {
			viewHolder.llCustomerWrapper.setVisibility(View.VISIBLE);
			viewHolder.llSystemWrapper.setVisibility(View.GONE);
			viewHolder.tvCustomerText.setText(consult.getContent());
		} else {
			viewHolder.llCustomerWrapper.setVisibility(View.GONE);
			viewHolder.llSystemWrapper.setVisibility(View.VISIBLE);
			viewHolder.tvSystemText.setText(consult.getContent());
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(Long.valueOf(consult.getTime()) * 1000);
		Long prevMillis = 0L;
		if (position != 0) {
			prevMillis = Long.valueOf(((Consult) consults.get(position - 1))
					.getTime()) * 1000;
		}
		if (calendar.getTimeInMillis() - prevMillis < 1000 * 60 * 10) {
			viewHolder.tvTime.setVisibility(View.GONE);
		} else {
			String time = null;
			if (calendar.get(Calendar.YEAR) != currentYear) {
				time = calendar.get(Calendar.YEAR) + "-"
						+ (calendar.get(Calendar.MONTH) + 1) + "-"
						+ calendar.get(Calendar.DAY_OF_MONTH) + " "
						+ formatTime(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
						+ formatTime(calendar.get(Calendar.MINUTE));
			} else {
				time = (calendar.get(Calendar.MONTH) + 1) + "-"
						+ calendar.get(Calendar.DAY_OF_MONTH) + " "
						+ formatTime(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
						+ formatTime(calendar.get(Calendar.MINUTE));
			}
			viewHolder.tvTime.setText(time);
			viewHolder.tvTime.setVisibility(View.VISIBLE);
		}
		return view;
	}

	private String formatTime(final Integer time) {
		if (time < 10)
			return "0" + time;
		return time.toString();
	}

	private class ViewHolder {
		TextView tvTime;
		TextView tvSystemText;
		TextView tvCustomerText;
		LinearLayout llSystemWrapper;
		LinearLayout llCustomerWrapper;
	}

}
