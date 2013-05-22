package com.possemeeg.android.programabletimer.timer;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class TimerItem implements Serializable {
	private final String timerId;
	public TimerItem(final String timerId) {
		this.timerId = timerId;
	}
	public abstract int totalTime();
	public final boolean run(final TimerContext context) throws InterruptedException{
		context.connect(timerId);
		return runCountdown(context);
	}
	public abstract boolean runCountdown(final TimerContext context) throws InterruptedException;
}
