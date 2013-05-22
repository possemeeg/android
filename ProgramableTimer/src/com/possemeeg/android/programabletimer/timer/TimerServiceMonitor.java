package com.possemeeg.android.programabletimer.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class TimerServiceMonitor extends BroadcastReceiver {

	public interface UpdateReceiver {
		void update(TimerStatus status, TimerService.BroadcastReason reason, String timerId);
	}

	private final UpdateReceiver updateReceiver;
	public TimerServiceMonitor(final UpdateReceiver updateReceiver){
		this.updateReceiver = updateReceiver;
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
		updateReceiver.update((TimerStatus)intent.getSerializableExtra(TimerService.BROADCAST_DATA_STATUS),
				(TimerService.BroadcastReason)intent.getSerializableExtra(TimerService.BROADCAST_DATA_REASON),
				(String)intent.getSerializableExtra(TimerService.BROADCAST_DATA_TIMER_ID));

	}

}
