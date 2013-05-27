package com.possemeeg.android.programabletimer;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.possemeeg.android.programabletimer.content.CountdownTimerItem;
import com.possemeeg.android.programabletimer.content.RepeatTimerItem;
import com.possemeeg.android.programabletimer.content.TimerItem;
import com.possemeeg.android.programabletimer.content.SequenceTimerItem;
import com.possemeeg.android.programabletimer.timer.TimerService;
import com.possemeeg.android.programabletimer.timer.TimerServiceConnection;
import com.possemeeg.android.programabletimer.timer.TimerServiceMonitor;
import com.possemeeg.android.programabletimer.timer.TimerStatus;
import com.possemeeg.android.programabletimer.timer.TimerUpdatesHandler;

public class TimerActivity extends Activity {
	private final static String TAG = TimerActivity.class.toString();

	private TimerServiceMonitor timerServiceMonitor;
	private Button startButton;
	private Button stopButton;
	private Button resetButton;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);

		timerServiceMonitor = new TimerServiceMonitor(this, new TimerUpdatesHandler() {

			@Override
			public void statusChange(final TimerStatus status) {
				Log.d(TAG, String.format("New Status %s", status.toString()));
			}

			@Override
			public void tick(final long elapsed, final long total) {
				Log.d(TAG, String.format("Tick %d/%d", elapsed, total));
			}

			@Override
			public void componentChange(final String componentId) {
				Log.d(TAG, String.format("Component change %s", componentId));
			}
		});


		startButton = (Button) findViewById(R.id.buttonStart);
		stopButton = (Button) findViewById(R.id.buttonStop);
		resetButton = (Button) findViewById(R.id.buttonReset);

		setupService();
	}

	@Override
	protected void onDestroy() {
		breakdownService(false);
		timerServiceMonitor.close();

		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timer, menu);
		return true;
	}

	private TimerServiceConnection serviceConnection;

	private boolean setupService() {
		serviceConnection = new TimerServiceConnection(this, new TimerServiceConnection.StateMonitor() {

			@Override
			public void enabled(final boolean isEnabled) {
				enable(isEnabled);
			}} );
		return serviceConnection.initialise(getTimer());
	}

	private void breakdownService(final boolean reInit) {
		enable(false);
		if (serviceConnection != null) {
			serviceConnection.close();
			serviceConnection = null;
		}
		if (reInit) {
			setupService();
		}
	}

	private void enable(final boolean enabled) {
		startButton.setEnabled(enabled);
		stopButton.setEnabled(enabled);
		resetButton.setEnabled(enabled);
	}

	private TimerItem getTimer() {

		final SequenceTimerItem.Builder builder = new SequenceTimerItem.Builder("seq 1");

		builder.addTimerItem(new CountdownTimerItem("cd 1", 5, CountdownTimerItem.Unit.SECONDS));
		builder.addTimerItem(new RepeatTimerItem("rep", new CountdownTimerItem("cd 2", 5, CountdownTimerItem.Unit.SECONDS), 5));

		return builder.build();
	}

	private TimerService.TimerBinder getServiceBinder() {
		return serviceConnection.getBinder();
	}

	public void start(final View sender) {
		Log.d(TAG, "starting timer");
		getServiceBinder().start();
	}

	public void stop(final View sender) {
		Log.d(TAG, "stopping timer");

		getServiceBinder().stop();
	}

	public void reset(final View sender) {
		Log.d(TAG, "resetting timer");
		breakdownService(true);
	}
}
