package pmg.android.runningcalculator.data;

public class DistanceConverter {
	
	public static double getMetresInKilometres(double metres) {
		return DecimalHelper.roundDistance(metres * RunningConstants.M_KM_CONVERSION_RATE);
	}

	public static double getMetresInTrackLaps(double metres) {
		return DecimalHelper.roundDistance(metres * RunningConstants.M_TRACK_LAP_CONVERSION_RATE);
	}

	public static double getMetresInMiles(double metres) {
		return DecimalHelper.roundDistance(metres * RunningConstants.M_MI_CONVERSION_RATE);
	}

	public static double getKilometresInMiles(double kilometres) {
		return DecimalHelper.roundDistance(kilometres * RunningConstants.KM_MI_CONVERSION_RATE);
	}

	public static double getMilesInKilometres(double miles) {
		return DecimalHelper.roundDistance(miles * RunningConstants.MI_KM_CONVERSION_RATE);
	}

	public static double getTrackLapsInKilometres(double laps) {
		return DecimalHelper.roundDistance(laps * RunningConstants.TRACK_LAP_KM_CONVERSION_RATE);
	}

	public static double getTrackLapsInMiles(double laps) {
		return DecimalHelper.roundDistance(laps * RunningConstants.TRACK_LAP_MI_CONVERSION_RATE);
	}
	public static double getKilometresInTrackLaps(double kilometres) {
		return DecimalHelper.roundDistance(kilometres * RunningConstants.KM_TRACK_LAP_CONVERSION_RATE);
	}
	public static double getMilesInTrackLaps(double miles) {
		return DecimalHelper.roundDistance(miles * RunningConstants.MI_TRACK_LAP_CONVERSION_RATE);
	}

	public static double getKilometresInMetres(double kilometres) {
		return DecimalHelper.roundDistance(kilometres * RunningConstants.KM_M_CONVERSION_RATE);
	}
	public static double getMilesInMetres(double miles) {
		return DecimalHelper.roundDistance(miles * RunningConstants.MI_M_CONVERSION_RATE);
	}
	public static double getTrackLapsInMetres(double laps) {
		return DecimalHelper.roundDistance(laps * RunningConstants.TRACK_LAP_M_CONVERSION_RATE);
	}

	public static double getMetresInUnit(double metres, DistanceUnit displayUnit) {
		switch (displayUnit) {
		case KILOMETRES:
			return getMetresInKilometres(metres);
		case MILES:
			return getMetresInMiles(metres);
		case TRACKLAPS:
			return getMetresInTrackLaps(metres);
		case UNSPECIFIED:
		case METRES:
			return metres;
		}
		return 0;
	}
	public static DistanceUnit estimatePrimaryDistanceUnit(double metres){
		double test = getMetresInKilometres(metres); 
		if(test == Math.round(test))
			return DistanceUnit.KILOMETRES;
		test = getMetresInMiles(metres); 
		if(test == Math.round(test))
			return DistanceUnit.MILES;
		return DistanceUnit.METRES;
	}

}
