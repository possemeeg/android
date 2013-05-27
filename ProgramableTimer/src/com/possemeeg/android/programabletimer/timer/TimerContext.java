package com.possemeeg.android.programabletimer.timer;

import com.possemeeg.android.programabletimer.content.TimerItem;

import android.os.SystemClock;

public class TimerContext {
	private TimerStatus currentStatus;
	private final Object syncObject = new Object();
	private final TimerUpdatesHandler updatesHandler;
	private final static int STEP_MILLIS = 1000;
	private long effectiveStartTime;
	private TimerItem currentTimer;

	TimerContext(final TimerUpdatesHandler statusChangedHandler) {
		this.updatesHandler = statusChangedHandler;
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
		updatesHandler.statusChange(currentStatus);
	}

	public void tick() throws InterruptedException {
		updatesHandler.tick(SystemClock.uptimeMillis() - effectiveStartTime, currentTimer.getTotalTime() );
	}

	public void connect(final TimerItem timer) throws InterruptedException {
		effectiveStartTime = SystemClock.uptimeMillis();
		currentTimer = timer;
		updatesHandler.componentChange(timer.getTimerId());
	}
}
