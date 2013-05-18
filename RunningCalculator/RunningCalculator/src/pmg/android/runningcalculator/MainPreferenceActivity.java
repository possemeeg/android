package pmg.android.runningcalculator;

import java.util.Map;

import pmg.android.runningcalculator.PreferenceValues.AbilityFilterValue;
import pmg.android.runningcalculator.PreferenceValues.UnitFilterValue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;

public class MainPreferenceActivity extends PreferenceActivity {

	private PreferenceValues preferenceValues;

	public MainPreferenceActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		PreferenceScreen preferenceScreen = getPreferenceScreen();
		preferenceValues = new PreferenceValues(this);
		getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
				broadcastChange();
				
			}
		});

		setupUnitFilter(preferenceScreen);
		setupAbilityFilter(preferenceScreen);
	}

	private void setupUnitFilter(PreferenceScreen preferenceScreen) {
		setupFilter(preferenceScreen, R.string.preference_key_unit_filter, R.string.preference_default_unit_filter, new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// setSummaryUnitFilter(preference, (String) newValue);
				setSummary(UnitFilterValue.class, preference, (String) newValue, R.string.preference_summary_unit_filter_sel,
						preferenceValues.getUnitFilterLongNames());
				return true;
			}
		});
	}

	private void setupAbilityFilter(PreferenceScreen preferenceScreen) {
		setupFilter(preferenceScreen, R.string.preference_key_ability_filter, R.string.preference_default_ability_filter,
				new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference, Object newValue) {
						// setSummaryAbilityFilter(preference, (String)
						// newValue);
						setSummary(AbilityFilterValue.class, preference, (String) newValue, R.string.preference_summary_ability_filter_sel,
								preferenceValues.getAbilityFilterLongNames());
						return true;
					}
				});
	}

	private void setupFilter(PreferenceScreen preferenceScreen, int preferenceKeyId, int defaultStringId,
			OnPreferenceChangeListener onPreferenceChangeListener) {
		String key = getString(preferenceKeyId);
		Preference pref = preferenceScreen.findPreference(key);

		onPreferenceChangeListener.onPreferenceChange(pref,
				preferenceScreen.getSharedPreferences().getString(key, getString(defaultStringId)));

		pref.setOnPreferenceChangeListener(onPreferenceChangeListener);
	}

	private <T extends Enum<T>> void setSummary(Class<T> enumType, Preference preference, String newValue, int summaryStringId, Map<T, String> nameMap) {
		preference.setSummary(getString(summaryStringId, nameMap.get(Enum.valueOf(enumType, newValue))));
	}
	
	private void broadcastChange(){
		getApplicationContext().sendBroadcast(new Intent(CoreApplication.BROADCAST_ACTION_PREFERENCE_CHANGED));

	}
}
