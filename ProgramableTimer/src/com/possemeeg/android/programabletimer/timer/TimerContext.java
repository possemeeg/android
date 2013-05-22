package com.possemeeg.android.programabletimer.timer;

import android.os.SystemClock;

public class TimerContext {
	private long effectiveStartTime;
	private TimerStatus currentStatus;
	private final Object syncObject = new Object();
	private final StateUpdateHandler statusChangedHandler;
	private final static int STEP_MILLIS = 1000;

	public interface StateUpdateHandler {
		void statusChanged(final TimerStatus newStatus);
		void tick();
		void timerConnected(String timerId, final TimerStatus status);
	}

	TimerContext(final StateUpdateHandler statusChangedHandler) {
		this.statusChangedHandler = statusChangedHandler;
		currentStatus = TimerStatus.STOPPED;
	}

	public long getEffectiveStartTime() {
		return effectiveStartTime;
	}

	public void setEffectiveStartTime(final long effectiveStartTime) {
		this.effectiveStartTime = effectiveStartTime;
	}

	public int getStepMillis() {
		return STEP_MILLIS;
	}

	TimerStatus getCurrentStatus(final long toWait) throws InterruptedException {
		synchronized (syncObject) {
			if (toWait > 0) {
				syncObject.wait(toWait);
			}
			return currentStatus;
		}
	}

	TimerStatus waitStopped() throws InterruptedException {
		final long pauseTime = SystemClock.uptimeMillis();
		synchronized (syncObject) {
			while (currentStatus == TimerStatus.STOPPED) {
				syncObject.wait();
			}
			effectiveStartTime += (SystemClock.uptimeMillis() - pauseTime);
			return currentStatus;
		}
	}

	void setStatus(final TimerStatus status) {
		synchronized (syncObject) {
			currentStatus = status;
			syncObject.notify();
		}
		statusChangedHandler.statusChanged(currentStatus);
	}

	public void tick() throws InterruptedException {
		statusChangedHandler.tick();
	}

	public void connect(final String timerId) throws InterruptedException {
		effectiveStartTime = SystemClock.uptimeMillis();
		statusChangedHandler.timerConnected(timerId, getCurrentStatus(0));
	}
}
