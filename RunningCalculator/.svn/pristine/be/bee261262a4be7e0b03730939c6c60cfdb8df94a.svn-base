package pmg.android.runningcalculator.data;

public class RunningConstants {
	public static final double PACE_WALKING_SEC_PER_KM = 500; // sec/km
	public static final double PACE_SPRINT_SEC_PER_KM = 85; // sec/km
	public static final double PACE_MARATHON_SEC_PER_KM = 165; // sec/km
	public static final double PACE_WALKING_SEC_PER_MILE = PaceSpeedConverter.getSecPerKmInSecPerMile(PACE_WALKING_SEC_PER_KM);
	public static final double PACE_SPRINT_SEC_PER_MILE = PaceSpeedConverter.getSecPerKmInSecPerMile(PACE_SPRINT_SEC_PER_KM);;
	public static final double SPRINT_DIST = 200;
	public static final double HALF_MARATHON = 21097.5;
	public static final double MARATHON_DIST = 42195;
	public static final double SPEED_SPRINT_KPH = PaceSpeedConverter.getSecPerKmInKph(PACE_SPRINT_SEC_PER_KM);;
	public static final double SPEED_SPRINT_MPH = PaceSpeedConverter.getSecPerKmInMph(PACE_SPRINT_SEC_PER_KM);;
	public static final double SPEED_WALKING_KPH = PaceSpeedConverter.getSecPerKmInKph(PACE_WALKING_SEC_PER_KM);;
	public static final double SPEED_WALKING_MPH = PaceSpeedConverter.getSecPerKmInMph(PACE_WALKING_SEC_PER_KM);;

	public static final double KM_M_CONVERSION_RATE = 1000;
	public static final double M_KM_CONVERSION_RATE = 1 / KM_M_CONVERSION_RATE;
	public static final double MI_M_CONVERSION_RATE = 1609.344;
	public static final double M_MI_CONVERSION_RATE = 1 / MI_M_CONVERSION_RATE;
	public static final double TRACK_LAP_M_CONVERSION_RATE = 400;
	public static final double M_TRACK_LAP_CONVERSION_RATE = 1 / TRACK_LAP_M_CONVERSION_RATE;
	public static final double TRACK_LAP_KM_CONVERSION_RATE = TRACK_LAP_M_CONVERSION_RATE * M_KM_CONVERSION_RATE;
	public static final double TRACK_LAP_MI_CONVERSION_RATE = TRACK_LAP_M_CONVERSION_RATE * M_MI_CONVERSION_RATE;
	public static final double MI_KM_CONVERSION_RATE = MI_M_CONVERSION_RATE / KM_M_CONVERSION_RATE;
	public static final double KM_MI_CONVERSION_RATE = 1 / MI_KM_CONVERSION_RATE;
	public static final double KM_TRACK_LAP_CONVERSION_RATE = KM_M_CONVERSION_RATE * M_TRACK_LAP_CONVERSION_RATE ;
	public static final double MI_TRACK_LAP_CONVERSION_RATE = MI_M_CONVERSION_RATE * M_TRACK_LAP_CONVERSION_RATE;

	public static final double MIN_COOPERS_METRES = 1000;
	public static final double MAX_COOPERS_METRES = 5000;

	public static boolean isFasterThanWalking(double secondsPerKm){
		return secondsPerKm <= PACE_WALKING_SEC_PER_KM;
	}
	public static boolean isSlowerThanPace(double testSecondsPerKm, double fastSecondsPerKm){
		return testSecondsPerKm >= fastSecondsPerKm;
	}
}
