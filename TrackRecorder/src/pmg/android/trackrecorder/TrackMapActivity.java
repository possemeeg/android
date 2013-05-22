package pmg.android.trackrecorder;

import java.util.List;

import pmg.android.trackrecorder.data.Tracks.TrackTable;
import pmg.android.trackrecorder.service.RecorderService;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class TrackMapActivity extends MapActivity  {
	private static final String TAG = TrackMapActivity.class.getName();
 
	private LinearLayout linearLayout;
	private MapView mapView;
//	private LocationManager locationManager;
	
	//Drawable drawable;
	List<Overlay> mapOverlays;
	LocationItemizedOverlay itemizedOverlay;
	
	private Intent recordingServiceIntent;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    	recordingServiceIntent = new Intent(this, RecorderService.class);

        setContentView(R.layout.main);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        
        mapOverlays = mapView.getOverlays();

        itemizedOverlay = new LocationItemizedOverlay(this, mapView);
        mapOverlays.add(itemizedOverlay);
    }
    
    @Override 
    public void onResume(){
    	super.onResume();
    	itemizedOverlay.enableCompass();
    	itemizedOverlay.enableMyLocation();
    	
    }
    

    @Override
    public void onPause(){
    	super.onPause();
    	
    }
    
    @Override
	protected boolean isRouteDisplayed() {
		return false;
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.map_activity_options, menu);
		return true;
    }
    
    private boolean queryServiceRunning(){
    	
    	ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
    	for(RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)){
    		if(serviceInfo.service.compareTo(recordingServiceIntent.getComponent()) == 0)
    			return true;
    	}
    	
    	return false;
    }
    
	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {
		
		
		boolean running = queryServiceRunning();
		
		menu.findItem(R.id.start_recording).setVisible(!running);
		menu.findItem(R.id.stop_recording).setVisible(running);
		
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.start_recording:
	    	startService(recordingServiceIntent);
			return true;
		case R.id.stop_recording:
	    	stopService(recordingServiceIntent);
			return true;
		case R.id.view_tracks:
			startActivity(new Intent(Intent.ACTION_VIEW, TrackTable.CONTENT_URI));
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}