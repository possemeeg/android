package com.possemeeg.android.programabletimer.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class TimerServiceMonitor extends BroadcastReceiver {

	private final TimerUpdatesHandler updateReceiver;
	private final Context context;

	public TimerServiceMonitor(final Context context, final TimerUpdatesHandler updateReceiver) {
		this.updateReceiver = updateReceiver;
		this.context = context;
		LocalBroadcastManager.getInstance(context).registerReceiver(this, new IntentFilter(TimerService.BROADCAST_INTENT));
	}

	public void close() {
		LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
		final TimerService.BroadcastReason reason = (TimerService.BroadcastReason) intent.getSerializableExtra(TimerService.BROADCAST_DATA_REASON);
		switch (reason) {
		case STATE_CHANGED:
			updateReceiver.statusChange((TimerStatus) intent.getSerializableExtra(TimerService.BROADCAST_DATA_STATUS));
			break;
		case TICK:
			updateReceiver.tick(intent.getLongExtra(TimerService.BROADCAST_DATA_ELAPSED, 0), intent.getLongExtra(TimerService.BROADCAST_DATA_TOTAL, 0));
			break;
		case TIMER_CHANGE:
			updateReceiver.componentChange((String) intent.getSerializableExtra(TimerService.BROADCAST_DATA_TIMER_ID));
			break;
		}
	}

}
