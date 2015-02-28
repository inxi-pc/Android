package com.maomao.app.citybuy.activity.accounts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * 获取当前日期是星期几<br>
	 * 
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "Sun", "Mon", "Tus", "Wen", "Sth", "Fri", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;

		return weekDays[w];
	}

	/**
	 * 获取当前日期是星期几<br>
	 * 
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String getMonthOfDate(Date dt) {
		String[] weekDays = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		int w = cal.get(Calendar.MONTH);
		if (w < 0)
			w = 0;

		return weekDays[w];
	}

	public static String getTimeString(String fromTime) {
		try {
			String format = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat df = new SimpleDateFormat(format);
			Date d1 = df.parse(dateFormat(format, new Date()));
			Date d2 = df.parse(fromTime);
			long diff = d1.getTime() - d2.getTime();

			long distance = diff / (60 * 24);
			//
			//
			// Date now = df.parse(dateFormat(format, new Date()));
			// Date date= df.parse(fromTime);
			// long l=now.getTime()-date.getTime();
			// long day=l/(24*60*60*1000);
			// long hour=(l/(60*60*1000)-day*24);
			// long min=((l/(60*1000))-day*24*60-hour*60);
			// long distance=(l/1000-day*24*60*60-hour*60*60-min*60);
			String timestamp;
			if (distance < 0)
				distance = 0;

			if (distance < 60) {
				timestamp = distance + "秒前";
			} else if (distance < 60 * 60) {
				distance = distance / 60;
				timestamp = distance + "分钟前";
			} else if (distance < 60 * 60 * 24) {
				distance = distance / 60 / 60;
				timestamp = distance + "小时前";
			} else if (distance < 60 * 60 * 24 * 7) {
				distance = distance / 60 / 60 / 24;
				timestamp = distance + "天前";
			} else if (distance < 60 * 60 * 24 * 7 * 4) {
				distance = distance / 60 / 60 / 24 / 7;
				timestamp = distance + "周前";
			} else {
				timestamp = getTime(fromTime);
			}
			return timestamp;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return getTime(fromTime);
	}

	public static String dateFormat(String format, Date date) {
		SimpleDateFormat time = new SimpleDateFormat(format);
		return time.format(date);
	}

	private static String getTime(String time) {
		if (null == time || "".equals(time))
			return null;
		time = time.replace("-", "");
		String result = "";
		String year = time.substring(0, 4);
		String month = time.substring(4, 6);
		String day = time.substring(6, 8);
		result = year + "年" + month + "月" + day + "日";
		return result;
	}

	public static String getWeek(String time) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		// String[] weekDays = { "星期四", "星期五", "星期六", "星期日", "星期一", "星期二",
		// "星期三",};
		if (null == time || "".equals(time))
			return null;
		time = time.replace(".", "");
		String year = time.substring(0, 4);
		String month = time.substring(4, 6);
		String day = time.substring(6, 8);
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(year), Integer.parseInt(month) - 1,
				Integer.parseInt(day));
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	public static String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		int mYear = cal.get(Calendar.YEAR);
		int mMonth = cal.get(Calendar.MONTH) + 1;
		int mDay = cal.get(Calendar.DAY_OF_MONTH);
		String month = "" + mMonth;
		if (mMonth < 10) {
			month = "0" + mMonth;
		}
		String day = "" + mDay;
		if (mDay < 10) {
			day = "0" + mDay;
		}
		String result = mYear + "." + month + "." + day;
		return result;
	}
}
