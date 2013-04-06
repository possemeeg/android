package pmg.android.runningcalculator.data.splits;

import java.util.ArrayList;
import java.util.List;

import pmg.android.runningcalculator.PreferenceValues;
import pmg.android.runningcalculator.PreferenceValues.AbilityFilterValue;
import pmg.android.runningcalculator.PreferenceValues.UnitFilterValue;
import pmg.android.runningcalculator.data.CalculatedItem;
import pmg.android.runningcalculator.data.InputValue;
import pmg.android.runningcalculator.data.InputValue.InputValueType;
import android.content.Context;

public class RaceBreakdownCalculatedItem extends CalculatedItem {
	private final RaceBreakdown raceBreakdown;
	private final double timeInSeconds;

	private RaceBreakdownCalculatedItem(Context context, InputValue inputValue, RaceBreakdown raceBreakdown, double timeInSeconds) {
		super(context, inputValue);
		this.raceBreakdown = raceBreakdown;
		this.timeInSeconds = timeInSeconds;
	}

	@Override
	public String getSynopsis() {
		return raceBreakdown.getFormattedRaceAsString(getResources(), timeInSeconds);
	}

	@Override
	public String getCalculation() {
		return raceBreakdown.getFormattedCalculationAsString(getResources(), timeInSeconds);
	}

	public static List<RaceBreakdown> getRaces(Context context) {
		List<RaceBreakdown> races = new ArrayList<RaceBreakdown>();
		for(FormattedRace race : FormattedRace.getFormattedRaces(context)) {
			if(race.getRace().getDistanceInMetres() <= 10000)
				addShortItem(races,race);
			else
				addNormalItem(races,race);
			
		}
		return races;
	}
	private static void addShortItem(List<RaceBreakdown> races, FormattedRace race) {
		races.add(new RaceBreakdown(race,FormattedLap.L_TRACK_LAP));
		races.add(new RaceBreakdown(race,FormattedLap.L_TRACK_HALF_LAP));
		addNormalItem(races, race);
	}
	private static void addNormalItem(List<RaceBreakdown> races, FormattedRace race) {
		races.add(new RaceBreakdown(race, FormattedLap.L_1km));
		races.add(new RaceBreakdown(race, FormattedLap.L_1mi));
		races.add(new RaceBreakdown(race,FormattedSpeed.FS_kph_mps_mph));
		races.add(new RaceBreakdown(race,FormattedSpeed.FS_kph_mps));
		races.add(new RaceBreakdown(race,FormattedSpeed.FS_mph));
	}

	public static void generateItems(Context context, InputValue value, PreferenceValues preferenceValues, List<CalculatedItem> list) {
		if (value.getValueType() != InputValueType.TIME_SECONDS)
			return;

		UnitFilterValue unitFilterPreference = preferenceValues.getUnitFilterPreference();
		AbilityFilterValue abilityFilterPreference = preferenceValues.getAbilityFilterPreference();

		for (RaceBreakdown race : getRaces(context)) {
			if (value.getValue() >= race.getRace().getMinSeconds() && value.getValue() <= race.getRace().getMaxSeconds() &&
					race.isViewableWithPreferences(unitFilterPreference) && 
					PreferenceValues.isInAbility(value.getValue(), race.getRace().getMaxSeconds(), race.getRace().getMinSeconds(), 
							abilityFilterPreference)
			) {
				list.add(new RaceBreakdownCalculatedItem(context, value, race, value.getValue()));
			}
		}

	}
}
