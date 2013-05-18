package pmg.android.runningcalculator.data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pmg.android.runningcalculator.data.InputValue.InputValueType;

public class InputValueGenerator {
	private static Pattern canBeInt = Pattern.compile("^[0-9]+$");
	private static Pattern canBeDouble = Pattern.compile("^[0-9]+\\.{1}[0-9]+$");
	private static Pattern canBeTwoPartTime = Pattern.compile("^([0-9]{1,2})[\\.:,]{1}([0-5]{0,1}[0-9]{1})$");
	private static Pattern canBeHoursMinsSeconds = Pattern.compile("^([0-9]{1,2})([\\.:,]{1})([0-5]{0,1}[0-9]{1})\\2([0-5]{0,1}[0-9]{1})$");

	public static interface AddFilter {
		boolean canAdd(InputValue inputValue);
	}
	
	public static InputValueList generateValuesFromSeed(String seed) {
		return generateValuesFromSeed(seed, null);
	}
	public static InputValueList generateValuesFromSeed(String seed, AddFilter addFilter) {
		if(addFilter == null)
			addFilter = new AddFilter() {
				
				@Override
				public boolean canAdd(InputValue inputValue) {
					return true;
				}
			};
		
		List<InputValue> list = new ArrayList<InputValue>();

		if (canBeInt.matcher(seed).find()) {
			addIntCandidates(Integer.parseInt(seed), list, addFilter);
		} else if (canBeDouble.matcher(seed).find()) {
			addDoubleCandidates(Double.parseDouble(seed), list, addFilter);
		}
		Matcher matcher = canBeTwoPartTime.matcher(seed);
		if (matcher.find()) {
			// time in hours and minutes
			add(new InputValue(InputValueType.TIME_SECONDS, Integer.parseInt(matcher.group(1)) * 60 * 60 + Integer.parseInt(matcher.group(2))
					* 60), list, addFilter);
			// time in minutes and seconds
			double timeSeconds = Integer.parseInt(matcher.group(1)) * 60 + Integer.parseInt(matcher.group(2));
			add(new InputValue(InputValueType.TIME_SECONDS, timeSeconds), list, addFilter);

			tryAddPaces(list, timeSeconds, addFilter);
		}
		matcher = canBeHoursMinsSeconds.matcher(seed);
		if (matcher.find()) {
			add(new InputValue(InputValueType.TIME_SECONDS, Integer.parseInt(matcher.group(1)) * 60 * 60 + Integer.parseInt(matcher.group(3))
					* 60 + Integer.parseInt(matcher.group(4))), list, addFilter);
		}

		return new InputValueList(seed,list);
	}

	private static void addIntCandidates(int value, List<InputValue> list, AddFilter addFilter) {
		addDoubleCandidates((double) value, list, addFilter);
	}

	private static void addDoubleCandidates(double value, List<InputValue> list, AddFilter addFilter) {
		if (value >= 1 && value <= 125)
			add(new InputValue(InputValueType.DISTANCE_TRACKLAPS, value), list, addFilter);
		if (value >= 1 && value <= 100)
			add(new InputValue(InputValueType.DISTANCE_KILOMETRES, value), list, addFilter);
		if (value >= 1 && value <= 100)
			add(new InputValue(InputValueType.DISTANCE_MILES, value), list, addFilter);
		if (value >= 100 && value <= 10000)
			add(new InputValue(InputValueType.DISTANCE_METRES, value), list, addFilter);
		if (value >= 0) {
			if(value < 24 * 60 * 60)
				add(new InputValue(InputValueType.TIME_SECONDS, value), list, addFilter);
			if(value < 24 * 60 )
				add(new InputValue(InputValueType.TIME_SECONDS, value * 60), list, addFilter);
			if(value < 24 )
				add(new InputValue(InputValueType.TIME_SECONDS, value * 60 * 60), list, addFilter);
		}

		tryAddPaces(list, value, addFilter);
		tryAddPaces(list, value * 60, addFilter);

		if (value >= RunningConstants.SPEED_WALKING_KPH && value <= RunningConstants.SPEED_SPRINT_KPH)
			add(new InputValue(InputValueType.SPEED_KPH, value), list, addFilter);
		if (value >= RunningConstants.SPEED_WALKING_MPH && value <= RunningConstants.SPEED_SPRINT_MPH)
			add(new InputValue(InputValueType.SPEED_MPH, value), list, addFilter);
	}

	private static void tryAddPaces(List<InputValue> list, double timeInSeconds, AddFilter addFilter) {
		if (timeInSeconds <= RunningConstants.PACE_WALKING_SEC_PER_KM && timeInSeconds >= RunningConstants.PACE_SPRINT_SEC_PER_KM)
			add(new InputValue(InputValueType.PACE_SEC_PER_KM, timeInSeconds), list, addFilter);
		if (timeInSeconds <= RunningConstants.PACE_WALKING_SEC_PER_MILE && timeInSeconds >= RunningConstants.PACE_SPRINT_SEC_PER_MILE)
			add(new InputValue(InputValueType.PACE_SEC_PER_MILE, timeInSeconds), list, addFilter);
	}
	
	private static void add(InputValue inputValue, List<InputValue> list, AddFilter addFilter){
		if(addFilter.canAdd(inputValue))
			list.add(inputValue);
	}
}
