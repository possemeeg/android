package pmg.android.runningcalculator.view;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pmg.android.runningcalculator.R;
import pmg.android.runningcalculator.PreferenceValues.UnitFilterValue;
import pmg.android.runningcalculator.data.DistanceConverter;
import pmg.android.runningcalculator.data.DistanceUnit;
import pmg.android.runningcalculator.data.InputValue;

import android.content.res.Resources;

public class DisplayStringFormatter {

	private static Calendar calcCalendar = Calendar.getInstance();
	private static SimpleDateFormat minSecTimeFormat = new SimpleDateFormat("mm:ss");
	private static SimpleDateFormat hourMinSecTimeFormat = new SimpleDateFormat("HH:mm:ss");

	public static String formatSecondsAsTime(Resources resources, double seconds) {
		if(seconds < 60) return formatSecondsPrecise(resources, seconds);
		calcCalendar.set(1969, 1, 12, 0, 0, 0);
		calcCalendar.setTimeInMillis(calcCalendar.getTimeInMillis() + (long) (seconds * 1000));
		Date time = calcCalendar.getTime();
		return time.getHours()>0 ? resources.getString(R.string.hrs_mins_seconds_as_lable, hourMinSecTimeFormat.format(time)) : 
			resources.getString(R.string.mins_seconds_as_lable, minSecTimeFormat.format(time));
	}

	private static DecimalFormat distanceFormat = new DecimalFormat("#,##0.###");
	
	public static String formatDistance(double distance){
		return distanceFormat.format(distance);
	}

	private static DecimalFormat secondsFormat = new DecimalFormat("##0.#");
	private static String formatSecondsPrecise(Resources resources, double seconds) {
		return resources.getString(R.string.precise_seconds_as_lable,secondsFormat.format(seconds));
	}

	private static DecimalFormat speedFormat = new DecimalFormat("#,##0.##");
	
	public static String formatSpeed(double speed) {
		return speedFormat.format(speed);
	}

	public static String formatPace(Resources resources, double from) {
		return formatSecondsAsTime(resources,from);
	}

	private static DecimalFormat vo2MaxFormat = new DecimalFormat("0");
	public static String formatVO2Max(double d) {
		return vo2MaxFormat.format(d);
	}

	public static String formatInputValue(Resources resources, InputValue inputValue){
		switch(inputValue.getValueType()){
		case TIME_SECONDS:
			return resources.getString(R.string.input_value_time,formatSecondsAsTime(resources, inputValue.getValue()));
		case DISTANCE_KILOMETRES:
			return resources.getString(R.string.input_value_km,formatDistance(inputValue.getValue()));
		case DISTANCE_METRES:
			return resources.getString(R.string.input_value_m,formatDistance(inputValue.getValue()));
		case DISTANCE_TRACKLAPS:
			return resources.getString(R.string.input_value_tracklaps,formatDistance(inputValue.getValue()));
		case DISTANCE_MILES:
			return resources.getString(R.string.input_value_mi,formatDistance(inputValue.getValue()));
		case PACE_SEC_PER_KM:
			return resources.getString(R.string.input_value_sec_per_km,formatPace(resources,inputValue.getValue()));
		case PACE_SEC_PER_MILE:
			return resources.getString(R.string.input_value_sec_per_mi,formatPace(resources,inputValue.getValue()));
		case SPEED_KPH:
			return resources.getString(R.string.input_value_kph,formatSpeed(inputValue.getValue()));
		case SPEED_MPH:
			return resources.getString(R.string.input_value_mph,formatSpeed(inputValue.getValue()));
		}
		return "";
	}
	
	public static String getFormattedDistance(Resources resources, int distanceInMetres) {
		switch(DistanceConverter.estimatePrimaryDistanceUnit(distanceInMetres)){
		case MILES:
			return resources.getString(R.string.input_value_mi, formatDistance(DistanceConverter.getMetresInMiles(distanceInMetres)));
		case METRES:
			return resources.getString(R.string.input_value_m,formatDistance(distanceInMetres));
		default:
			return resources.getString(R.string.input_value_km, formatDistance(DistanceConverter.getMetresInKilometres(distanceInMetres)));
		}

	}
	
}
