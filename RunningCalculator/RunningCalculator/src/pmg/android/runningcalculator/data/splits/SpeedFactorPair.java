package pmg.android.runningcalculator.data.splits;

public class SpeedFactorPair {
	private final double raceDistanceFactor;
	private final double timeFactor;
	
	public SpeedFactorPair(double raceDistanceFactor, double timeFactor){
		this.raceDistanceFactor = raceDistanceFactor;
		this.timeFactor = timeFactor;
	}

	public double getRaceDistanceFactor() {
		return raceDistanceFactor;
	}

	public double getTimeFactor() {
		return timeFactor;
	}
}
