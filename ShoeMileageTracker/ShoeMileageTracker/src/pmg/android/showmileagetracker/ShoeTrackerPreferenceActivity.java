package pmg.android.showmileagetracker;

import pmg.android.showmileagetracker.util.DistanceHelper;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class ShoeTrackerPreferenceActivity extends PreferenceActivity {

	private final DistanceHelper distanceHelper;

	public ShoeTrackerPreferenceActivity() {
		distanceHelper = new DistanceHelper(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

		PreferenceScreen preferenceScreen = getPreferenceScreen();
		String key_dist_unit = distanceHelper.getUnitPreferenceKey();
		Preference pref = preferenceScreen.findPreference(key_dist_unit);

		setSummaryDistanceUnit(preferenceScreen.findPreference(key_dist_unit), distanceHelper.getUnitPreference() );
		
		pref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				setSummaryDistanceUnit(preference, (String)newValue);
				return true;
			}
		});
	}

	private void setSummaryDistanceUnit(Preference preference, String newValue) {
		preference.setSummary(getResources().getString(R.string.summary_distance_unit_preference,
				distanceHelper.getLongName(newValue.toString())));
	}

}
