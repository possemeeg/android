package pmg.android.runningcalculator.data.splits;

import pmg.android.runningcalculator.PreferenceValues.UnitFilterValue;
import android.content.res.Resources;

public class RaceBreakdown {

	private final FormattedRace formattedRace;
	private final FormattedBreakdown formattedBreakdown;
	
	public RaceBreakdown(FormattedRace formattedRace, FormattedBreakdown formattedBreakdown) {
		this.formattedRace = formattedRace;
		this.formattedBreakdown = formattedBreakdown;
	}
	
	public Race getRace() {
		return formattedRace.getRace();
	}

	public String getFormattedRaceAsString(Resources resorces, double seconds) {
		return formattedRace.getRaceAsString(resorces, seconds);
	}

	public String getFormattedCalculationAsString(Resources resources, double raceTimeInSeconds) {
		return formattedBreakdown.getCalculationAsString(resources, getRace().getDistanceInMetres(), raceTimeInSeconds);
	}

	public boolean isViewableWithPreferences(UnitFilterValue unitFilterValue) {
		return formattedRace.isApplicableUnit(unitFilterValue) && formattedBreakdown.isViewableWithPreferences(unitFilterValue);
	}

}
