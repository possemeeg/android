package com.possemeeg.android.programabletimer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.possemeeg.android.programabletimer.timer.CountdownTimerItem;
import com.possemeeg.android.programabletimer.timer.RepeatTimerItem;
import com.possemeeg.android.programabletimer.timer.TimerItem;
import com.possemeeg.android.programabletimer.timer.TimerItemSequence;
import com.possemeeg.android.programabletimer.timer.TimerService;
import com.possemeeg.android.programabletimer.timer.TimerService.TimerBinder;
import com.possemeeg.android.programabletimer.timer.TimerServiceMonitor;
import com.possemeeg.android.programabletimer.timer.TimerStatus;

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

		timerServiceMonitor = new TimerServiceMonitor(new TimerServiceMonitor.UpdateReceiver() {

			@Override
			public void update(final TimerStatus status, final TimerService.BroadcastReason reason, final String timerId) {
				Log.d(TAG, String.format("Update received %s/%s (%s)", status.toString(), reason.toString(), timerId));
			}
		});
		LocalBroadcastManager.getInstance(this).registerReceiver(timerServiceMonitor, new IntentFilter(TimerService.BROADCAST_INTENT));

		startButton = (Button) findViewById(R.id.buttonStart);
		stopButton = (Button) findViewById(R.id.buttonStop);
		resetButton = (Button) findViewById(R.id.buttonReset);

		setupService();
	}

	@Override
	protected void onDestroy() {
		breakdownService(false);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timer, menu);
		return true;
	}

	private class TimerServiceConnection implements ServiceConnection {

		private TimerService.TimerBinder binder;
		private final Intent serviceIntent;

		TimerServiceConnection() {
			serviceIntent = new Intent(TimerActivity.this, TimerService.class);
		}

		@Override
		public void onServiceConnected(final ComponentName name, final IBinder service) {
			setBinder((TimerBinder) service);
			enable(true);
		}

		@Override
		public void onServiceDisconnected(final ComponentName name) {
			setBinder(null);
		}

		private boolean initialise(final TimerItem sequence) {
			serviceIntent.putExtra(TimerService.TIMER_SEQUENCE, sequence);
			Log.d(TAG, "starting timer service");
			TimerActivity.this.startService(serviceIntent);
			return bindService(serviceIntent, this, BIND_IMPORTANT);
		}

		private void close() {
			unbindService(this);
			TimerActivity.this.stopService(serviceIntent);
		}

		public TimerService.TimerBinder getBinder() {
			return binder;
		}

		public void setBinder(final TimerService.TimerBinder binder) {
			this.binder = binder;
		}
	}

	private TimerServiceConnection serviceConnection;

	private boolean setupService() {
		serviceConnection = new TimerServiceConnection();
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

		final TimerItemSequence.Builder builder = new TimerItemSequence.Builder("seq 1");

		builder.addTimerItem(new CountdownTimerItem("cd 1", 5, CountdownTimerItem.Unit.SECONDS));
		builder.addTimerItem(new RepeatTimerItem("rep", new CountdownTimerItem("cd 2", 5, CountdownTimerItem.Unit.SECONDS), 5));
//		return new CountdownTimerItem(7, CountdownTimerItem.Unit.SECONDS);

		return builder.build();
	}

	private TimerService.TimerBinder getBinder() {
		return serviceConnection.getBinder();
	}

	public void start(final View sender) {
		Log.d(TAG, "starting timer");
		getBinder().start();
	}

	public void stop(final View sender) {
		Log.d(TAG, "stopping timer");

		getBinder().stop();
	}

	public void reset(final View sender) {
		Log.d(TAG, "resetting timer");
		breakdownService(true);
	}
}
