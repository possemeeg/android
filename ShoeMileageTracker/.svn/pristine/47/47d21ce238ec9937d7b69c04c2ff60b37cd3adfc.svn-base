package pmg.android.showmileagetracker;

import android.app.Application;
import android.preference.PreferenceManager;

public class ShoeTrackerApplication extends Application {

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
