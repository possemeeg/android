package pmg.android.showmileagetracker.util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import pmg.android.showmileagetracker.R;
import android.preference.PreferenceManager;

public class DistanceHelper {

	private static final double MILE_CONVERSION_FACTOR = 1.609;
	private static final double KM_METRE_FACTOR = 1000.0;

	public DistanceHelper(Context context)
	{
		this.context = context;
	}

	public int convertFromEntry(double enteredValue) {
		return (int) ((getUnitPreferenceKey().equals("Imperial") ? enteredValue * MILE_CONVERSION_FACTOR : enteredValue) * KM_METRE_FACTOR);
	}

	public int convertFromEntry(String enteredValue) {
		if(enteredValue == null || enteredValue.isEmpty())
			return 0;
		try{
			return convertFromEntry(Double.parseDouble(enteredValue));
		}
		catch(Throwable e)
		{
		}
		return 0;
	}

	public String getUnitPreferenceKey()
	{
		return context.getResources().getString(R.string.preference_key_distance_unit);
	}
	
	private static double convertForDisplay(String unit, int distanceInMetres)
	{
		double km = ((double) distanceInMetres) / KM_METRE_FACTOR;
		return unit.equals("Imperial") ?  km / MILE_CONVERSION_FACTOR : km;
	}

	public String getUnitPreference() {
		Resources resources = context.getResources();
		String key_dist_unit = resources.getString(R.string.preference_key_distance_unit);
		return PreferenceManager.getDefaultSharedPreferences(context).getString(key_dist_unit, 
					resources.getString(R.string.default_distance_unit));
	}
	
	public String getDistanceDisplay(int distance) {
		return getDistanceDisplay(getUnitPreference(), distance);
	}
	private String getDistanceDisplay(String unitPreference, int distance) {
		return String.format("%s %s", getDistanceForDisplay(distance), getShortName(unitPreference));
	}
	public String getDistanceForDisplay(int distance)
	{
		DecimalFormat inst = (DecimalFormat) DecimalFormat.getInstance();
		inst.applyLocalizedPattern("0.00");
		return inst.format(convertForDisplay(getUnitPreference(), distance));
	}

	private class UnitName
	{
		public UnitName(String longName, String shortName)
		{
			this.longName = longName;
			this.shortName = shortName;
		}
		public final String longName;
		public final String shortName;
	};
	private Map<String, UnitName> distUnitNameMap;
	private final Context context;
	private Map<String, UnitName> getDistUnitMap()
	{
		if(distUnitNameMap != null)
			return distUnitNameMap;

		distUnitNameMap = new HashMap<String,UnitName>();
		
		Resources resources = context.getResources();
		String[] units = resources.getStringArray(R.array.distance_units);
		String[] unit_long_names = resources.getStringArray(R.array.distance_unit_long_names);
		String[] unit_short_names = resources.getStringArray(R.array.distance_unit_short_names);

		for (int i = 0, imax = Math.min(Math.min(units.length, unit_long_names.length), unit_short_names.length); i < imax; ++i) {
			distUnitNameMap.put(units[i], new UnitName(unit_long_names[i],unit_short_names[i]));
		}
		
		return distUnitNameMap;
	}
	public String getLongName(String string) {
		return getDistUnitMap().get(string).longName;
	}
	public String getShortName(String string) {
		return getDistUnitMap().get(string).shortName;
	}

}
