package pmg.android.runningcalculator.data.splits;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import pmg.android.runningcalculator.R;
import pmg.android.runningcalculator.data.DistanceConverter;
import pmg.android.runningcalculator.data.RunningConstants;
import pmg.android.runningcalculator.data.db.RunningEvent.RacesColumns;

public class Race {
	private final double distanceInMetres;
	private final String name;
//	private final int nameResId;
	private final double minSeconds;
	private final double maxSeconds;
	private final double maxSecondsPerKm;
//	private static int INVALID_RESOURCE = -1;

//	public Race(double distanceInMetres) {
//		this(distanceInMetres, null);
//	}

	public Race(double distanceInMetres, String name) {
		this.distanceInMetres = distanceInMetres;
		this.name = name;
//		if (distanceInMetres == RunningConstants.HALF_MARATHON)
//			this.nameResId = R.string.half_marathon_name;
//		else if (distanceInMetres == RunningConstants.MARATHON_DIST)
//			this.nameResId = R.string.marathon_name;
//		else
//			this.nameResId = INVALID_RESOURCE;
		// speed in seconds per km
		maxSecondsPerKm = (((distanceInMetres - RunningConstants.SPRINT_DIST) / (RunningConstants.MARATHON_DIST - RunningConstants.SPRINT_DIST)))
				* (RunningConstants.PACE_MARATHON_SEC_PER_KM - RunningConstants.PACE_SPRINT_SEC_PER_KM) + RunningConstants.PACE_SPRINT_SEC_PER_KM;

		double distInKilometres = DistanceConverter.getMetresInKilometres(distanceInMetres);
		this.minSeconds = maxSecondsPerKm * distInKilometres;
		this.maxSeconds = RunningConstants.PACE_WALKING_SEC_PER_KM * distInKilometres;
	}

	public double getDistanceInMetres() {
		return distanceInMetres;
	}

	public double getMinSeconds() {
		return minSeconds;
	}

	public double getMaxSeconds() {
		return maxSeconds;
	}

	public double getMaxSecondsPerKm() {
		return maxSecondsPerKm;
	}

	public String getName(){
		return name;
	}

	public static List<Race> getRaces(Context context) {
		String [] projection = new String [] {RacesColumns.DISTANCE, RacesColumns.NAME};
		
		Cursor cursor = context.getContentResolver().query(RacesColumns.CONTENT_URI, projection, null, null, null);
		
		List<Race> ret = new ArrayList<Race>();

		for(boolean fin = cursor.moveToFirst(); fin; fin = cursor.moveToNext()){ 
			ret.add(new Race(cursor.getInt(0), cursor.getString(1)));
		}
		
		return ret;
	}
}
