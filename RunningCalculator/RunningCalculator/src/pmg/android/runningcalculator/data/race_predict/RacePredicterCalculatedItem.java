package pmg.android.runningcalculator.data.race_predict;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import pmg.android.runningcalculator.PreferenceValues;
import pmg.android.runningcalculator.R;
import pmg.android.runningcalculator.data.CalculatedItem;
import pmg.android.runningcalculator.data.InputValue;
import pmg.android.runningcalculator.data.PaceSpeedConverter;
import pmg.android.runningcalculator.data.splits.FormattedRace;
import pmg.android.runningcalculator.view.DisplayStringFormatter;

public class RacePredicterCalculatedItem extends CalculatedItem {
	private final String speedOrPace;
	private final RacePrediction prediction;
	private final double secondsPerKilometre;

	public RacePredicterCalculatedItem(Context context, InputValue inputValue, String speedOrPace, RacePrediction prediction, double secondsPerKilometre) {
		super(context, inputValue);
		this.speedOrPace = speedOrPace;
		this.prediction = prediction;
		this.secondsPerKilometre = secondsPerKilometre;
	}

	@Override
	public String getSynopsis() {
		return speedOrPace;
	}

	@Override
	public String getCalculation() {
		return prediction.getFormattedRace(getResources(), secondsPerKilometre);
	}

	// @formatter:off
//	private static final RacePrediction[] predictions = new RacePrediction[] { 
//		new RacePrediction(FormattedRace.FR_200m),
//		new RacePrediction(FormattedRace.FR_400m),
//		new RacePrediction(FormattedRace.FR_800m),
//		new RacePrediction(FormattedRace.FR_1000m),
//		new RacePrediction(FormattedRace.FR_1500m),
//		new RacePrediction(FormattedRace.FR_1mi),
//		new RacePrediction(FormattedRace.FR_1mi_km),
//		new RacePrediction(FormattedRace.FR_2000m),
//		new RacePrediction(FormattedRace.FR_3000m),
//		new RacePrediction(FormattedRace.FR_2mi),
//		new RacePrediction(FormattedRace.FR_2mi_km),
//		new RacePrediction(FormattedRace.FR_3mi),
//		new RacePrediction(FormattedRace.FR_3mi_km),
//		new RacePrediction(FormattedRace.FR_5000m),
//		new RacePrediction(FormattedRace.FR_8000m),
//		new RacePrediction(FormattedRace.FR_5mi),
//		new RacePrediction(FormattedRace.FR_5mi_km),
//		new RacePrediction(FormattedRace.FR_10km),
//		new RacePrediction(FormattedRace.FR_12km),
//		new RacePrediction(FormattedRace.FR_8mi),
//		new RacePrediction(FormattedRace.FR_8mi_km),
//		new RacePrediction(FormattedRace.FR_15km),
//		new RacePrediction(FormattedRace.FR_10mi),
//		new RacePrediction(FormattedRace.FR_10mi_km),
//		new RacePrediction(FormattedRace.FR_20km),
//		new RacePrediction(FormattedRace.FR_HALF_MARATHON_km),
//		new RacePrediction(FormattedRace.FR_HALF_MARATHON_mi),
//		new RacePrediction(FormattedRace.FR_HALF_MARATHON_km_mi),
//		new RacePrediction(FormattedRace.FR_MARATHON_km),
//		new RacePrediction(FormattedRace.FR_MARATHON_mi),
//		new RacePrediction(FormattedRace.FR_MARATHON_km_mi)
//		};
	// @formatter:on
	public static List<RacePrediction> getPredictions(Context context) {
		List<RacePrediction> predictions = new ArrayList<RacePrediction>();
		for(FormattedRace race : FormattedRace.getFormattedRaces(context))
			predictions.add(new RacePrediction(race));
		return predictions;
	}
	
	
	public static void generateItems(Context context, InputValue value, PreferenceValues preferenceValues, List<CalculatedItem> list) {
		// we want paces and speeds

		switch (value.getValueType()) {
		case PACE_SEC_PER_KM:
			final String pacepk = context.getResources().getString(R.string.at_time_per_km_synopsis,
					DisplayStringFormatter.formatPace(context.getResources(), value.getValue()));

			addApplicable(context, value, list, preferenceValues, value.getValue(), new ItemCreator() {
				
				@Override
				public CalculatedItem create(Context context, InputValue value, RacePrediction prediction, double secondsPerKilometre) {
					return new RacePredicterCalculatedItem(context, value, pacepk, prediction, secondsPerKilometre);
				}
			});

			break;

		case PACE_SEC_PER_MILE:
			final String pacepm = context.getResources().getString(R.string.at_time_per_mi_synopsis,
					DisplayStringFormatter.formatPace(context.getResources(), value.getValue()));

			addApplicable(context, value, list,preferenceValues, PaceSpeedConverter.getSecPerMiInSecPerKm(value.getValue()), new ItemCreator() {
				
				@Override
				public CalculatedItem create(Context context, InputValue value, RacePrediction prediction, double secondsPerKilometre) {
					return new RacePredicterCalculatedItem(context, value, pacepm, prediction, secondsPerKilometre);
				}
			});
			break;

		case SPEED_KPH:
			final String speedk = context.getResources().getString(R.string.at_speed_kph_synopsis, DisplayStringFormatter.formatSpeed(value.getValue()));

			addApplicable(context, value, list,preferenceValues, PaceSpeedConverter.getKphInSecPerKm(value.getValue()), new ItemCreator() {
				
				@Override
				public CalculatedItem create(Context context, InputValue value, RacePrediction prediction, double secondsPerKilometre) {
					return new RacePredicterCalculatedItem(context, value, speedk, prediction, secondsPerKilometre);
				}
			});
			break;

		case SPEED_MPH:
			final String speedm = context.getResources().getString(R.string.at_speed_mph_synopsis, DisplayStringFormatter.formatSpeed(value.getValue()));

			addApplicable(context, value, list,preferenceValues, PaceSpeedConverter.getMphInSecPerKm(value.getValue()), new ItemCreator() {
				
				@Override
				public CalculatedItem create(Context context, InputValue value, RacePrediction prediction, double secondsPerKilometre) {
					return new RacePredicterCalculatedItem(context, value, speedm, prediction, secondsPerKilometre);
				}
			});
			break;
		}
	}
	private interface ItemCreator{
		CalculatedItem create(Context context, InputValue value, RacePrediction prediction, double secondsPerKilometre);
	}
	private static void addApplicable(Context context, InputValue value, List<CalculatedItem> list, PreferenceValues preferenceValues, double secondsPerKilometre, ItemCreator itemCreator){
		for (RacePrediction prediction : getPredictions(context)) {
			if(prediction.isApplicable(secondsPerKilometre, preferenceValues))
				list.add(itemCreator.create(context, value, prediction, secondsPerKilometre));
		}
	}

}
