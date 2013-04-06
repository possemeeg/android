package pmg.android.runningcalculator.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InputValue implements Serializable, Comparable<InputValue> {
	private static final long serialVersionUID = -4847314366513183937L;

	public enum InputValueType {
		TIME_SECONDS, DISTANCE_KILOMETRES, DISTANCE_METRES, DISTANCE_TRACKLAPS, DISTANCE_MILES, PACE_SEC_PER_KM, PACE_SEC_PER_MILE, SPEED_KPH, SPEED_MPH
	}

	private final InputValueType valueType;
	private final double value;

	public InputValue(InputValueType valueType, double value) {
		this.valueType = valueType;
		this.value = value;
	}

	@Override
	public String toString() {
		if (valueType == InputValueType.TIME_SECONDS) {
			Calendar calcCalendar = Calendar.getInstance();
			calcCalendar.set(1969, 1, 12, 0, 0, 0);
			calcCalendar.setTimeInMillis(calcCalendar.getTimeInMillis() + (long) (value * 1000));
			return String.format("%s: %s", valueType.toString(), dateFormat.format(calcCalendar.getTime()));
		}
		return String.format("%s: %s", valueType.toString(), new Double(value).toString());
	}

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	public InputValueType getValueType() {
		return valueType;
	}

	public double getValue() {
		return value;
	}

	public double getDistanceInMetres() {
		switch (valueType) {
		case DISTANCE_KILOMETRES:
			return DistanceConverter.getKilometresInMetres(value);
		case DISTANCE_METRES:
			return value;
		case DISTANCE_MILES:
			return DistanceConverter.getMilesInMetres(value);
		case DISTANCE_TRACKLAPS:
			return DistanceConverter.getTrackLapsInMetres(value);
		default:
			throw new IllegalArgumentException("Not a distance");
		}
	}

	public boolean isDistance() {
		switch (valueType) {
		case DISTANCE_KILOMETRES:
		case DISTANCE_METRES:
		case DISTANCE_MILES:
		case DISTANCE_TRACKLAPS:
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(InputValue rhs) {
		int en = valueType.compareTo(rhs.valueType);
		return en != 0 ? en : Double.compare(value,rhs.value);
	}
}
