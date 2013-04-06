package pmg.android.runningcalculator.data.splits;

import java.util.EnumSet;
import static java.util.EnumSet.of;
import pmg.android.runningcalculator.PreferenceValues.UnitFilterValue;
import pmg.android.runningcalculator.R;
import pmg.android.runningcalculator.view.DisplayStringFormatter;
import android.content.res.Resources;

public class FormattedLap extends FormattedBreakdown {
	private final double distanceInMetres;

	private FormattedLap(double distanceInMetres, int formattedStringId, UnitFilterValue unitTypes) {
		this(distanceInMetres, formattedStringId, of(unitTypes));
	}
	private FormattedLap(double distanceInMetres, int formattedStringId, EnumSet<UnitFilterValue> unitTypes) {
		super(formattedStringId, unitTypes);
		this.distanceInMetres = distanceInMetres;
	}
	
	@Override
	public String getCalculationAsString(Resources resources, double raceDistanceInMetres, double raceTimeInSeconds) {
		return resources.getString(getFormattedStringId(),DisplayStringFormatter.formatSecondsAsTime(resources,getSplitTime(raceDistanceInMetres,raceTimeInSeconds)));
	}

	
	private double getSplitTime(double raceDistanceInMetres, double raceTimeInSeconds) {
		return (raceTimeInSeconds * distanceInMetres / raceDistanceInMetres);
	}

	public static final FormattedLap L_TRACK_LAP = new FormattedLap(400,R.string.time_per_lap_calculation, of(UnitFilterValue.Both,UnitFilterValue.Metric));
	public static final FormattedLap L_TRACK_HALF_LAP = new FormattedLap(200,R.string.time_per_half_lap_calculation, of(UnitFilterValue.Both,UnitFilterValue.Metric));
	public static final FormattedLap L_1km = new FormattedLap(1000,R.string.time_per_km_calculation,  of(UnitFilterValue.Both,UnitFilterValue.Metric));
	public static final FormattedLap L_1mi = new FormattedLap(1609,R.string.time_per_mi_calculation,  of(UnitFilterValue.Both,UnitFilterValue.Imperial));
}
