package com.possemeeg.android.programabletimer.timer;

import java.util.ArrayList;
import java.util.List;

public final class TimerItemSequence extends TimerItem {
	private static final long serialVersionUID = 1L;

	private final List<TimerItem> timerItems;

	public static class Builder {
		private final String timerId;
		private final List<TimerItem> timerItems;

		public Builder(final String timerId) {
			this.timerId = timerId;
			this.timerItems = new ArrayList<TimerItem>();
		}

		public Builder addTimerItem(final TimerItem item) {
			timerItems.add(item);
			return this;
		}

		public TimerItemSequence build() {
			return new TimerItemSequence(timerId, timerItems);
		}
	}

	private TimerItemSequence(final String timerId, final List<TimerItem> timerItems) {
		super(timerId);
		this.timerItems = timerItems;
	}

	@Override
	public int totalTime() {
		int result = 0;
		for (final TimerItem timerItem : timerItems) {
			result += timerItem.totalTime();
		}
		return result;
	}

	@Override
	public boolean runCountdown(final TimerContext context) throws InterruptedException {
		for(final TimerItem timer : timerItems){
			if(!timer.run(context))
				return false;
		}
		return true;
	}
}
