package pmg.android.runningcalculator;

import android.app.Application;
import android.preference.PreferenceManager;

public class CoreApplication extends Application {

	public static final String BROADCAST_ACTION_PREFERENCE_CHANGED = "pmg.android.runningcalculator.PREFERENCE_CHANGED";
	public static final String ACTION_SHOW_DETAILS = "pmg.android.runningcalculator.SHOW_DETAILS";
	public static final String ACTIVITY_EXTRA_INPUT_VALUE = "input.value";
	
    @Override
    public void onCreate() {
        /*
         * This populates the default values from the preferences XML file. See
         * {@link DefaultValues} for more details.
         */
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    @Override
    public void onTerminate() {
    }

}
