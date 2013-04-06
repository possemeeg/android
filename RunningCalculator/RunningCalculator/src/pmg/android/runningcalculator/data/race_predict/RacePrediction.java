package pmg.android.runningcalculator.data.race_predict;

import android.content.res.Resources;
import pmg.android.runningcalculator.PreferenceValues;
import pmg.android.runningcalculator.data.DistanceConverter;
import pmg.android.runningcalculator.data.RunningConstants;
import pmg.android.runningcalculator.data.splits.FormattedRace;

public class RacePrediction {
	private final FormattedRace race;
	
	public RacePrediction(FormattedRace race) {
		this.race = race;
	}

	public String getFormattedRace(Resources resources, double secondsPerKilometre) {
		double totalSeconds = (DistanceConverter.getMetresInKilometres(race.getRace().getDistanceInMetres()) * secondsPerKilometre );

		return race.getRaceAsString(resources, totalSeconds);
	}

	public boolean isApplicable(double secondsPerKilometre, PreferenceValues preferenceValues) {
		return RunningConstants.isSlowerThanPace(secondsPerKilometre, race.getRace().getMaxSecondsPerKm()) &&
			RunningConstants.isFasterThanWalking(secondsPerKilometre) && 
			race.isApplicableUnit(preferenceValues.getUnitFilterPreference());
	}

}
