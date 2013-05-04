package pmg.android.showmileagetracker.util;

import java.util.Calendar;

import android.content.Context;
import android.text.format.DateUtils;

public class DateHelper {
	public static String getDisplayDate(Context context, Calendar d)
	{
		return getDisplayDate(context, d.getTimeInMillis());
	}
	public static String getDisplayDate(Context context, long l)
	{
		return DateUtils.formatDateTime(context, l, DateUtils.FORMAT_NUMERIC_DATE);
	}
}
