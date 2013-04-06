package pmg.android.runningcalculator.data.splits;

import java.util.EnumSet;

import pmg.android.runningcalculator.R;
import pmg.android.runningcalculator.PreferenceValues.UnitFilterValue;
import pmg.android.runningcalculator.view.DisplayStringFormatter;
import android.content.res.Resources;
import static java.util.EnumSet.of;

public class FormattedSpeed extends FormattedBreakdown {

	private final SpeedFactorPair speedFactorPair1;
	private final SpeedFactorPair speedFactorPair2;
	private final SpeedFactorPair speedFactorPair3;

	private FormattedSpeed(int formattedStringId, SpeedFactorPair speedFactorPair1, UnitFilterValue unitType) {
		this(formattedStringId, speedFactorPair1, null, unitType);
	}

	private FormattedSpeed(int formattedStringId, SpeedFactorPair speedFactorPair1, SpeedFactorPair speedFactorPair2, UnitFilterValue unitType) {
		this(formattedStringId, speedFactorPair1, speedFactorPair2, null, unitType);
	}

	private FormattedSpeed(int formattedStringId, SpeedFactorPair speedFactorPair1, SpeedFactorPair speedFactorPair2,
				SpeedFactorPair speedFactorPair3, UnitFilterValue unitType) {
		this(formattedStringId, speedFactorPair1, speedFactorPair2,
				speedFactorPair3, of(unitType));
	}
	private FormattedSpeed(int formattedStringId, SpeedFactorPair speedFactorPair1, SpeedFactorPair speedFactorPair2,
			SpeedFactorPair speedFactorPair3, EnumSet<UnitFilterValue> unitTypes) {
		super(formattedStringId, unitTypes);
		this.speedFactorPair1 = speedFactorPair1;
		this.speedFactorPair2 = speedFactorPair2;
		this.speedFactorPair3 = speedFactorPair3;
	}

	@Override
	public String getCalculationAsString(Resources resources, double raceDistanceInMetres, double raceTimeInSeconds) {
// @formatter:off		
		return speedFactorPair3 != null ? 
			resources.getString(getFormattedStringId(),
					getFormattedSpeed(speedFactorPair1,raceDistanceInMetres,raceTimeInSeconds),
					getFormattedSpeed(speedFactorPair2,raceDistanceInMetres,raceTimeInSeconds),
					getFormattedSpeed(speedFactorPair3,raceDistanceInMetres,raceTimeInSeconds)) :
			speedFactorPair2 != null ?
					resources.getString(getFormattedStringId(),
							getFormattedSpeed(speedFactorPair1,raceDistanceInMetres,raceTimeInSeconds),
							getFormattedSpeed(speedFactorPair2,raceDistanceInMetres,raceTimeInSeconds)) :
					resources.getString(getFormattedStringId(),
							getFormattedSpeed(speedFactorPair1,raceDistanceInMetres,raceTimeInSeconds));
// @formatter:on
	}

	private static String getFormattedSpeed(SpeedFactorPair speedFactorPair, double raceDistanceInMetres, double raceTimeInSeconds) {
		return DisplayStringFormatter.formatSpeed((raceDistanceInMetres * speedFactorPair.getTimeFactor())
				/ (speedFactorPair.getRaceDistanceFactor() * raceTimeInSeconds));
	}

	private static final SpeedFactorPair SF_KPH = new SpeedFactorPair(1000, (60 * 60));
	private static final SpeedFactorPair SF_MPH = new SpeedFactorPair(1609, (60 * 60));
	private static final SpeedFactorPair SF_MPS = new SpeedFactorPair(1, 1);
//	public static final FormattedSpeed FS_kph = new FormattedSpeed(R.string.ave_speed_kph, SF_KPH, UnitFilterValue.Metric);
	public static final FormattedSpeed FS_mph = new FormattedSpeed(R.string.ave_speed_mph, SF_MPH, UnitFilterValue.Imperial);
//	public static final FormattedSpeed FS_mps = new FormattedSpeed(R.string.ave_speed_mps, SF_MPS, UnitFilterValue.Metric);
	public static final FormattedSpeed FS_kph_mps = new FormattedSpeed(R.string.ave_speed_kph_mps, SF_KPH, SF_MPS, UnitFilterValue.Metric);
	public static final FormattedSpeed FS_kph_mps_mph = new FormattedSpeed(R.string.ave_speed_kph_mps_mph, SF_KPH, SF_MPS, SF_MPH,
			UnitFilterValue.Both);

}
/*
 * speed_mps = dist_m / time_s speed_kph = dist_km / time_h = (dist_m/1000) *
 * (3600/time_h) = (dist_m * 3600) / (1000*time_h)
 * 
 * average speed of 14.06 kph, 8.8 mph or 44 m/s
 */