package pmg.android.runningcalculator.data;

public class PaceSpeedConverter {

	public static double getKphInSecPerKm(double kph) {
		return DecimalHelper.roundPace((60 * 60) / kph);
	}

	public static double getSecPerKmInKph(double secondsPerKm) {
		return DecimalHelper.roundSpeed((60 * 60) / secondsPerKm);
	}

	public static double getKphInSecPerMi(double kph) {
		return DecimalHelper.roundPace((RunningConstants.MI_KM_CONVERSION_RATE * 60 * 60) / kph);
	}

	public static double getMphInSecPerMi(double mph) {
		return DecimalHelper.roundPace((60 * 60) / mph);
	}

	public static double getSecPerMiInMph(double secondsPerMi) {
		return DecimalHelper.roundSpeed((60 * 60) / secondsPerMi);
	}

	public static double getMphInSecPerKm(double mph) {
		return DecimalHelper.roundPace((RunningConstants.KM_MI_CONVERSION_RATE * 60 * 60) / mph);
	}

	public static double getSecPerMiInKph(double secondsPerMi) {
		return DecimalHelper.roundSpeed((RunningConstants.MI_KM_CONVERSION_RATE * 60 * 60) / secondsPerMi);
	}

	public static double getSecPerKmInMph(double secondsPerKm) {
		return DecimalHelper.roundSpeed((RunningConstants.KM_MI_CONVERSION_RATE * 60 * 60) / secondsPerKm);
	}

	public static double getSecPerMiInSecPerKm(double secPerMile) {
		return DecimalHelper.roundSpeed( secPerMile * RunningConstants.KM_MI_CONVERSION_RATE);
	}

	public static double getSecPerKmInSecPerMile(double secPerKm) {
		return DecimalHelper.roundSpeed( secPerKm * RunningConstants.MI_KM_CONVERSION_RATE);
	}
	public static double getMphInKph(double value) {
		return DistanceConverter.getMilesInKilometres(value);
	}

	public static double getKphInMph(double value) {
		return DistanceConverter.getKilometresInMiles(value);
	}


	/*
	 * x km/h => 1 hr per x km => 1/x hr per 1 km => 1/x * 60 * 60 s per 1 km
	 * 
	 * x s/km => 1 km per x s => 1km * (60*60/x) per x * (60*60/x) = x/(60*60) /
	 * x/(60*60) h = x/(60*60) km per 1 h
	 */

}
