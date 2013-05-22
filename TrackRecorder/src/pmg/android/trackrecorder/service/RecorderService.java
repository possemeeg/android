package pmg.android.trackrecorder.service;

import pmg.android.trackrecorder.R;
import pmg.android.trackrecorder.data.Tracks;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class RecorderService extends Service {
	private static final String TAG = RecorderService.class.getName();
	private LocationManager locationManager;
	private NotificationManager notificationManager;

	private static final int RECORDING_NOTIFICATION_ID = 1;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "starting service");
		
		if(!startRecording()) {
			stopSelf(startId);
			return START_NOT_STICKY;
		}
		
		return START_STICKY;
	}

	
	@Override
	public void onDestroy() {
		Log.i(TAG, "stopping service");
		stopRecording();
	}

	private class Listener implements LocationListener {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			Log.i(TAG, String.format("Position updated %f, %f, %f", location.getLatitude(), location.getLongitude(), location.getAltitude()));
		
			ContentResolver contentResolver = getContentResolver();
			Integer currentRecording = getCurrentTrackId(contentResolver);
			if(currentRecording == null)
				return;
			
			ContentValues trackPointValues = new ContentValues();
			
			trackPointValues.put(Tracks.TrackPointTable.TRACK_ID, currentRecording);
			trackPointValues.put(Tracks.TrackPointTable.LATITUDE, location.getLatitude());
			trackPointValues.put(Tracks.TrackPointTable.LONGITUDE, location.getLongitude());
			trackPointValues.put(Tracks.TrackPointTable.ALTITUDE, location.getAltitude());
			
			contentResolver.insert(Tracks.TrackPointTable.CONTENT_URI, trackPointValues);
		}
	};

	private static final String[] JOBS_PROJECTION = new String[] { Tracks.JobTable._ID, Tracks.JobTable.TRACK_ID};
	private static final int JOBS_PROJECTION_COLUMN_INDEX_TRACK_ID = 1;
	
	private Listener listener;
	private boolean startRecording(){
		if(listener != null)
			return false;

		ContentResolver contentResolver = getContentResolver();
		
		Integer currentRecording = getCurrentTrackId(contentResolver);
		if(currentRecording != null)
			return false;
		
		Uri uri = contentResolver.insert(Tracks.TrackTable.CONTENT_URI, new ContentValues());
		if(uri == null)
			return false;
		
		ContentValues jobValues = new ContentValues();
		jobValues.put(Tracks.JobTable.TRACK_ID, ContentUris.parseId(uri));
		contentResolver.insert(Tracks.JobTable.CONTENT_URI, jobValues);
		
		listener = new Listener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

		Notification.Builder builder = new Notification.Builder(this);
		builder.setOngoing(true);
		builder.setSmallIcon(R.drawable.play);
		builder.setContentText("Hello");
		
		notificationManager.notify(RECORDING_NOTIFICATION_ID, builder.getNotification());

		return true;
	}
	
	private void stopRecording(){
		if(listener != null)
			locationManager.removeUpdates(listener);
		
		notificationManager.cancel(RECORDING_NOTIFICATION_ID);

		ContentResolver contentResolver = getContentResolver();
		contentResolver.delete(Tracks.JobTable.CONTENT_URI, null, null);

		listener = null;
	}

	private static Integer getCurrentTrackId(ContentResolver contentResolver) {
		Cursor cursor = contentResolver.query(Tracks.JobTable.CONTENT_URI, JOBS_PROJECTION, null, null, null);
		try{
			if(cursor.getCount() <= 0)
				return null;
		
			cursor.moveToFirst();
			return cursor.getInt(JOBS_PROJECTION_COLUMN_INDEX_TRACK_ID);
		}
		finally
		{
			cursor.close();
		}
	}
}
