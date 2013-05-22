package pmg.android.trackrecorder;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

public class LocationItemizedOverlay extends MyLocationOverlay {
	
	public LocationItemizedOverlay(Context context, MapView mapView) {
		super(context, mapView);
	}

	//private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();

//	public LocationItemizedOverlay(Drawable defaultMarker) {
//		super(boundCenter(defaultMarker));
//	}
//
//	@Override
//	protected OverlayItem createItem(int i) {
//		return overlays.get(i);
//	}
//
//	@Override
//	public int size() {
//		return overlays.size();
//	}
//
//	public void addOverlay(OverlayItem overlay) {
//	    overlays.add(overlay);
//	    populate();
//	}
}
