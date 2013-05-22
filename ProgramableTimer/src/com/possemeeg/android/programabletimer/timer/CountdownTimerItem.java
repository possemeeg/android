package com.possemeeg.android.programabletimer.timer;

import android.os.SystemClock;

public final class CountdownTimerItem extends TimerItem {
	private static final long serialVersionUID = 1L;

	private final int time;

	public enum Unit {
		SECONDS(1000), MILLISECONDS(1);

		private int mult;

		private Unit(final int mult) {
			this.mult = mult;
		}
	}

	public CountdownTimerItem(final String timerId, final int time, final Unit unit) {
		super(timerId);
		this.time = time * unit.mult;
	}

	@Override
	public int totalTime() {
		return time;
	}

	@Override
	public boolean runCountdown(final TimerContext context) throws InterruptedException {
		long currentTime;
		TimerStatus status = context.waitStopped();

		while ((currentTime = SystemClock.uptimeMillis()) < (context.getEffectiveStartTime() + totalTime())) {
			final long runFor = currentTime - context.getEffectiveStartTime();
			final long toWait = context.getStepMillis() + (runFor / context.getStepMillis()) * context.getStepMillis() - runFor;
			status = context.getCurrentStatus(toWait);

			if (status == TimerStatus.STOPPED) {
				status = context.waitStopped();
			}
			if (status == TimerStatus.INTERUPTED) {
				return false;
			}

			context.tick();
		}
		return (status == TimerStatus.RUNNING);
	}
}
