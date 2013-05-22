package com.possemeeg.android.programabletimer.timer;

public final class RepeatTimerItem extends TimerItem {
	private static final long serialVersionUID = 1L;

	private final TimerItem timerItem;
	private final int repeatCount;

	public RepeatTimerItem(final String timerId, final TimerItem timerItem, final int repeatCount){
		super(timerId);
		this.timerItem = timerItem;
		this.repeatCount = repeatCount;
	}

	@Override
	public int totalTime() {
		return timerItem.totalTime() * repeatCount;
	}

	@Override
	public boolean runCountdown(final TimerContext context) throws InterruptedException {
		for(int i = 0; i < repeatCount; ++i){
			if(!timerItem.run(context))
				return false;
		}
		return true;
	}
}
