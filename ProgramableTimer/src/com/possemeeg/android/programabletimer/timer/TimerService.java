package com.possemeeg.android.programabletimer.timer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class TimerService extends Service {
	public final static String TAG = TimerService.class.toString();

	public static final String TIMER_SEQUENCE = "timer.sequence";

	public enum BroadcastReason {
		STATE_CHANGED, TICK, TIMER_CHANGE
	}

	public static final String BROADCAST_INTENT = "com.possemeeg.android.programabletimer.timer.TimerService";
	public static final String BROADCAST_DATA_STATUS = "Status";
	public static final String BROADCAST_DATA_TIMER_ID = "TimerId";
	public static final String BROADCAST_DATA_REASON = "Reason";

	public class TimerBinder extends Binder {
		public void start() {
			setTimerContextStatus(TimerStatus.RUNNING);
		}

		public void stop() {
			setTimerContextStatus(TimerStatus.STOPPED);
		}

//		public void reset() {
//			final TimerContext timerContext = getTimerContext();
//			if (timerContext != null) {
//				timerContext.reset();
//			}
//		}

	}

	private final Object syncObject = new Object();
	private TimerContext timerContext;

	private Looper serviceThreadLooper;
	private ServiceHandler serviceHandler;
	private final TimerBinder timerBinder;

	private static final class ServiceHandler extends Handler {
		private final TimerService service;

		private ServiceHandler(final Looper looper, final TimerService service) {
			super(looper);
			this.service = service;
		}

		@Override
		public void handleMessage(final Message msg) {
			Log.i(TAG, "Starting timer sequence.");
			service.runSequence((TimerItem) msg.obj);
			service.stopSelf(msg.arg1);
		}
	}

	public TimerService() {
		timerBinder = new TimerBinder();
	}

	private TimerContext getTimerContext() {
		return timerContext;
	}

	private void setTimerContextStatus(final TimerStatus status) {
		final TimerContext timerContext = this.timerContext;
		if (timerContext != null) {
			timerContext.setStatus(status);
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();

		final HandlerThread thread = new HandlerThread("Timer service handler");
		thread.start();

		serviceThreadLooper = thread.getLooper();
		serviceHandler = new ServiceHandler(serviceThreadLooper, this);
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		Log.i(TAG, "Starting service");

		final Message msg = serviceHandler.obtainMessage();
		msg.arg1 = startId;
		msg.obj = intent.getSerializableExtra(TIMER_SEQUENCE);
		serviceHandler.sendMessage(msg);
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		synchronized (syncObject) {
			if (timerContext != null) {
				timerContext.setStatus(TimerStatus.INTERUPTED);
			}
		}
		serviceThreadLooper.quit();
	}

	private void resetTimerContext() {
		synchronized (syncObject) {
			timerContext = null;
		}
	}

	private TimerContext createTimerContext() throws InterruptedException {
		synchronized (syncObject) {
			if (timerContext != null) {
				throw new AssertionError("Unexpected situation. TimerContext should be null.");
			}
			return (timerContext = new TimerContext(new TimerContext.StateUpdateHandler() {

				@Override
				public void statusChanged(final TimerStatus newStatus) {
					broadcastStatus(BroadcastReason.STATE_CHANGED, newStatus);
				}

				@Override
				public void tick() {
					broadcastStatus(BroadcastReason.TICK, TimerStatus.RUNNING);
				}

				@Override
				public void timerConnected(final String timerId, final TimerStatus status) {
					broadcastStatus(BroadcastReason.TIMER_CHANGE, status, timerId);
				}
			}));
		}
	}

	private void runSequence(final TimerItem timer) {
		try {
			final TimerContext timerContext = createTimerContext();
//			timerContext.reset();

			if(timer.run(timerContext))
				timerContext.setStatus(TimerStatus.COMPLETED);

		} catch (final InterruptedException e) {
			Log.w(TAG, "Timer service inturrupted");
		} finally {
			resetTimerContext();
		}
	}

	private void broadcastStatus(final BroadcastReason event, final TimerStatus status) {
		broadcastStatus(event, status, null);
	}
	private void broadcastStatus(final BroadcastReason event, final TimerStatus status, final String timerId) {
		final Intent intent = new Intent(BROADCAST_INTENT).putExtra(BROADCAST_DATA_STATUS, status).putExtra(BROADCAST_DATA_REASON, event);
		if(intent != null){
			intent.putExtra(BROADCAST_DATA_TIMER_ID, timerId);
		}

		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	@Override
	public IBinder onBind(final Intent intent) {
		return timerBinder;
	}

}
