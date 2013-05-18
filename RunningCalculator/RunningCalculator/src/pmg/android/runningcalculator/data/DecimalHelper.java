package pmg.android.runningcalculator.data;

public class DecimalHelper {

	public static double round(double in, int dp) {
		return ((double) Math.round(in * Math.pow(10,dp))) / Math.pow(10,dp);
	}
	
	public static double roundDistance(double in){
		return round(in,3);
	}
	public static double roundSpeed(double in){
		return round(in,3);
	}
	public static double roundPace(double in){
		return round(in,3);
	}

}
