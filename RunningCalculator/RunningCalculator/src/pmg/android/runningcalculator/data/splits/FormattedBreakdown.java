package pmg.android.runningcalculator.data.splits;

import java.util.EnumSet;

import pmg.android.runningcalculator.PreferenceValues.UnitFilterValue;
import android.content.res.Resources;

public abstract class FormattedBreakdown {
	private final int formattedStringId;
	private final EnumSet<UnitFilterValue> unitTypes;

	protected FormattedBreakdown(int formattedStringId, EnumSet<UnitFilterValue> unitTypes) {
		this.formattedStringId = formattedStringId;
		this.unitTypes = unitTypes;
	}

	protected int getFormattedStringId() {
		return formattedStringId;
	}

	public boolean isViewableWithPreferences(UnitFilterValue unitFilterValue) {
		return unitTypes.contains(unitFilterValue);
	}
	
	public abstract String getCalculationAsString(Resources resources, double raceDistanceInMetres, double raceTimeInSeconds);

}
