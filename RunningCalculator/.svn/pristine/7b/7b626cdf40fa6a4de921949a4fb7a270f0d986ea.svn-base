package pmg.android.runningcalculator.data.converters;

import java.util.List;

import pmg.android.runningcalculator.PreferenceValues;
import pmg.android.runningcalculator.R;
import pmg.android.runningcalculator.data.CalculatedItem;
import pmg.android.runningcalculator.data.DistanceConverter;
import pmg.android.runningcalculator.data.InputValue;
import pmg.android.runningcalculator.data.PaceSpeedConverter;
import android.content.Context;

public abstract class ConverterCalculatedItem extends CalculatedItem {

	private final double to;
	private final int synopsisStringId;
	private final int calculationStringId;

	public ConverterCalculatedItem(Context context, InputValue inputValue, double to, int synopsisStringId, int calculationStringId) {
		super(context, inputValue);
		this.to = to;
		this.synopsisStringId = synopsisStringId;
		this.calculationStringId = calculationStringId;
	}

	protected double getFrom() {
		return getInputValue().getValue();
	}

	protected abstract String getFormattedFrom();

	protected double getTo() {
		return to;
	}

	protected abstract String getFormattedTo();

	@Override
	public String getSynopsis() {

		return getResources().getString(synopsisStringId, getFormattedFrom());
	}

	@Override
	public String getCalculation() {

		return getResources().getString(calculationStringId, getFormattedTo());
	}

	public static void generateItems(Context context, InputValue value, PreferenceValues preferenceValues, List<CalculatedItem> list) {
		switch (value.getValueType()) {
		case DISTANCE_METRES:
			list.add(new DistanceConverterCalculatedItem(context, value, DistanceConverter.getMetresInKilometres(value.getValue()),
					R.string.m_synopsis, R.string.km_calculation));
			list.add(new DistanceConverterCalculatedItem(context, value, DistanceConverter.getMetresInMiles(value.getValue()),
					R.string.m_synopsis, R.string.mi_calculation));
			list.add(new DistanceConverterCalculatedItem(context, value, DistanceConverter.getMetresInTrackLaps(value.getValue()),
					R.string.m_synopsis, R.string.laps_calculation));
			break;
		case DISTANCE_KILOMETRES:
			list.add(new DistanceConverterCalculatedItem(context, value, DistanceConverter.getKilometresInMiles(value.getValue()),
					R.string.km_synopsis, R.string.mi_calculation));
			list.add(new DistanceConverterCalculatedItem(context, value, DistanceConverter.getKilometresInTrackLaps(value.getValue()),
					R.string.km_synopsis, R.string.laps_calculation));
			break;
		case DISTANCE_MILES:
			list.add(new DistanceConverterCalculatedItem(context, value, DistanceConverter.getMilesInKilometres(value.getValue()),
					R.string.mi_synopsis, R.string.km_calculation));
			list.add(new DistanceConverterCalculatedItem(context, value, DistanceConverter.getMilesInTrackLaps(value.getValue()),
					R.string.mi_synopsis, R.string.laps_calculation));
			break;
		case DISTANCE_TRACKLAPS:
			list.add(new DistanceConverterCalculatedItem(context, value, DistanceConverter.getTrackLapsInKilometres(value.getValue()),
					R.string.laps_synopsis, R.string.km_calculation));
			list.add(new DistanceConverterCalculatedItem(context, value, DistanceConverter.getTrackLapsInMiles(value.getValue()),
					R.string.laps_synopsis, R.string.mi_calculation));
			break;
		case PACE_SEC_PER_KM:
			list.add(new PaceSpeedConverterCalculatedItem(context, value, PaceSpeedConverter.getSecPerKmInKph(value.getValue()),
					R.string.sec_per_km_synopsis, R.string.kph_calculation));
			list.add(new PaceSpeedConverterCalculatedItem(context, value, PaceSpeedConverter.getSecPerKmInMph(value.getValue()),
					R.string.sec_per_km_synopsis, R.string.mph_calculation));
			list.add(new PacePaceConverterCalculatedItem(context, value, PaceSpeedConverter.getSecPerKmInSecPerMile(value.getValue()),
					R.string.sec_per_km_synopsis, R.string.sec_per_mi_calculation));
			break;
		case PACE_SEC_PER_MILE:
			list.add(new PaceSpeedConverterCalculatedItem(context, value, PaceSpeedConverter.getSecPerMiInKph(value.getValue()),
					R.string.sec_per_mi_synopsis, R.string.kph_calculation));
			list.add(new PaceSpeedConverterCalculatedItem(context, value, PaceSpeedConverter.getSecPerMiInMph(value.getValue()),
					R.string.sec_per_mi_synopsis, R.string.mph_calculation));
			list.add(new PacePaceConverterCalculatedItem(context, value, PaceSpeedConverter.getSecPerMiInSecPerKm(value.getValue()),
					R.string.sec_per_mi_synopsis, R.string.sec_per_km_calculation));
			break;
		case SPEED_MPH:
			list.add(new SpeedPaceConverterCalculatedItem(context, value, PaceSpeedConverter.getMphInSecPerKm(value.getValue()),
					R.string.mph_synopsis, R.string.sec_per_km_calculation));
			list.add(new SpeedPaceConverterCalculatedItem(context, value, PaceSpeedConverter.getMphInSecPerMi(value.getValue()),
					R.string.mph_synopsis, R.string.sec_per_mi_calculation));
			list.add(new SpeedSpeedConverterCalculatedItem(context, value, PaceSpeedConverter.getMphInKph(value.getValue()),
					R.string.mph_synopsis, R.string.kph_calculation));
			break;
		case SPEED_KPH:
			list.add(new SpeedPaceConverterCalculatedItem(context, value, PaceSpeedConverter.getKphInSecPerKm(value.getValue()),
					R.string.kph_synopsis, R.string.sec_per_km_calculation));
			list.add(new SpeedPaceConverterCalculatedItem(context, value, PaceSpeedConverter.getKphInSecPerMi(value.getValue()),
					R.string.kph_synopsis, R.string.sec_per_mi_calculation));
			list.add(new SpeedSpeedConverterCalculatedItem(context, value, PaceSpeedConverter.getKphInMph(value.getValue()),
					R.string.kph_synopsis, R.string.mph_calculation));
			break;

		}
	}
}
