package pmg.android.showmileagetracker.util;

import android.app.Activity;

public abstract class ActivityAlertDialog {
	private final Activity activity;


	public ActivityAlertDialog(Activity activity) {
		this.activity = activity;
	}

	protected Activity getActivity() {
		return activity;
	}
}
