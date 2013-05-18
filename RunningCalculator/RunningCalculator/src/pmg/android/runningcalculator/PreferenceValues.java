package pmg.android.runningcalculator;

import java.util.EnumMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

public class PreferenceValues {

	public enum UnitFilterValue {
		Both, Metric, Imperial;
	}

	public enum AbilityFilterValue {
		All, Slow, Medium, Fast;
	}

	private Context context;
	private SharedPreferences sharedPreferences;

	public PreferenceValues(Context context) {
		this.context = context;
		this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public UnitFilterValue getUnitFilterPreference() {
		return getListPreference(UnitFilterValue.class, R.string.preference_key_unit_filter, R.string.preference_default_unit_filter);
	}

	public AbilityFilterValue getAbilityFilterPreference() {
		return getListPreference(AbilityFilterValue.class, R.string.preference_key_ability_filter, R.string.preference_default_ability_filter);
	}

	public <T extends Enum<T>> T getListPreference(Class<T> enumType, int keyId, int defaultId) {
		Resources resources = context.getResources();
		String key = resources.getString(keyId);
		return (T) Enum.valueOf(enumType, sharedPreferences.getString(key, resources.getString(defaultId)));
	}

	private Map<UnitFilterValue, String> unitFilterNamesMap;
	public Map<UnitFilterValue, String> getUnitFilterLongNames() {
		return unitFilterNamesMap != null ? unitFilterNamesMap : (unitFilterNamesMap = getLongNamesMap(UnitFilterValue.class,
				R.array.unit_filter_values, R.array.unit_filter_values_names));
	}
	private Map<AbilityFilterValue, String> abilityFilterNamesMap;
	public Map<AbilityFilterValue, String> getAbilityFilterLongNames() {
		return abilityFilterNamesMap != null ? abilityFilterNamesMap : (abilityFilterNamesMap = getLongNamesMap(AbilityFilterValue.class,
				R.array.ability_filter_values, R.array.ability_filter_values_names));
	}

	private <T extends Enum<T>> Map<T, String> getLongNamesMap(Class<T> enumType, int valuesId, int namesId) {
		Map<T, String> ret = new EnumMap<T, String>(enumType);
		String[] names = context.getResources().getStringArray(valuesId);
		String[] descrs = context.getResources().getStringArray(namesId);
		final int imax = Math.min(names.length, descrs.length);
		for (int i = 0; i < imax; ++i) {
			ret.put(Enum.valueOf(enumType, names[i]), descrs[i]);
		}
		return ret;
	}

	public boolean getShowDebugOutput() {
		return sharedPreferences.getBoolean(context.getResources().getString(R.string.preference_key_debug_output), false);
	}

	private static class MinMaxPercentage {
		public final double min, max;

		public MinMaxPercentage(double min, double max) {
			this.min = min;
			this.max = max;
		}

		public boolean isInAbility(double value, double absMin, double absMax) {
			double percentage = (value - absMin) * 100 / (absMax - absMin);
			return percentage >= min && percentage <= max;
		}
	}

	private static final MinMaxPercentage ABILITY_ALL = new MinMaxPercentage(0, 100);
	private static final MinMaxPercentage ABILITY_SLOW = new MinMaxPercentage(0, 35);
	private static final MinMaxPercentage ABILITY_MEDIUM = new MinMaxPercentage(25, 75);
	private static final MinMaxPercentage ABILITY_FAST = new MinMaxPercentage(65, 100);

	private static MinMaxPercentage getMinMaxPercentage(AbilityFilterValue ability) {
		switch (ability) {
		case Slow:
			return ABILITY_SLOW;
		case Medium:
			return ABILITY_MEDIUM;
		case Fast:
			return ABILITY_FAST;
		default:
			return ABILITY_ALL;
		}
	}

	public static boolean isInAbility(double value, double slow, double fast, AbilityFilterValue ability) {
		if(slow > fast) {
			//
			fast = -fast;
			slow = -slow;
			value = -value;
		}
		MinMaxPercentage minMax = getMinMaxPercentage(ability);
		return minMax.isInAbility(value, slow, fast);
	}
}
