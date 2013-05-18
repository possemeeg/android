package pmg.android.runningcalculator.data.splits;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import pmg.android.runningcalculator.PreferenceValues.UnitFilterValue;
import pmg.android.runningcalculator.R;
import pmg.android.runningcalculator.data.DistanceConverter;
import pmg.android.runningcalculator.data.DistanceUnit;
import pmg.android.runningcalculator.view.DisplayStringFormatter;
import android.content.Context;
import android.content.res.Resources;
import static java.util.EnumSet.of;

public class FormattedRace {

	private final Race race;
	private final DistanceUnit distanceUnit;
	private final DistanceUnit distanceUnitAlt;
	private final EnumSet<UnitFilterValue> unitTypes;

	private FormattedRace(Race race, DistanceUnit distanceUnit, UnitFilterValue unitType) {
		this(race, distanceUnit, DistanceUnit.UNSPECIFIED, of(unitType));
	}

	private FormattedRace(Race race, DistanceUnit distanceUnit, EnumSet<UnitFilterValue> unitTypes) {
		this(race, distanceUnit, DistanceUnit.UNSPECIFIED, unitTypes);
	}

	private FormattedRace(Race race, DistanceUnit distanceUnit, DistanceUnit distanceUnitAlt, UnitFilterValue unitType) {
		this(race, distanceUnit, distanceUnitAlt, of(unitType));
	}

	private FormattedRace(Race race, DistanceUnit distanceUnit, DistanceUnit distanceUnitAlt, EnumSet<UnitFilterValue> unitTypes) {
		super();
		this.race = race;
		this.distanceUnit = distanceUnit;
		this.distanceUnitAlt = distanceUnitAlt;
		this.unitTypes = unitTypes;
	}

	private String formatName(Resources resorces) {
//		String name = race.getResolvedName(resorces);
		if (race.getName() == null || race.getName().isEmpty())
			return "";

		return race.getName() + ": ";
	}

	public String getRaceAsString(Resources resorces, double seconds) {

		switch (distanceUnit) {
		case METRES:
			if (distanceUnitAlt == DistanceUnit.UNSPECIFIED)
				return resorces.getString(
						R.string.x_in_time_synopsis,
						formatName(resorces),
						resorces.getString(R.string.s_m_synopsis,
								DisplayStringFormatter.formatDistance(DistanceConverter.getMetresInUnit(race.getDistanceInMetres(), DistanceUnit.METRES))),
						DisplayStringFormatter.formatSecondsAsTime(resorces, seconds));

			return resorces.getString(
					R.string.x_altx_in_time_synopsis,
					formatName(resorces),
					resorces.getString(R.string.s_m_synopsis,
							DisplayStringFormatter.formatDistance(DistanceConverter.getMetresInUnit(race.getDistanceInMetres(), DistanceUnit.METRES))),
					resorces.getString(R.string.s_mi_synopsis,
							DisplayStringFormatter.formatDistance(DistanceConverter.getMetresInUnit(race.getDistanceInMetres(), DistanceUnit.MILES))),
					DisplayStringFormatter.formatSecondsAsTime(resorces, seconds));

		case KILOMETRES:
		default:
			if (distanceUnitAlt == DistanceUnit.UNSPECIFIED)
				return resorces.getString(
						R.string.x_in_time_synopsis,
						formatName(resorces),
						resorces.getString(R.string.s_km_synopsis,
								DisplayStringFormatter.formatDistance(DistanceConverter.getMetresInUnit(race.getDistanceInMetres(), DistanceUnit.KILOMETRES))),
						DisplayStringFormatter.formatSecondsAsTime(resorces, seconds));

			return resorces.getString(
					R.string.x_altx_in_time_synopsis,
					formatName(resorces),
					resorces.getString(R.string.s_km_synopsis,
							DisplayStringFormatter.formatDistance(DistanceConverter.getMetresInUnit(race.getDistanceInMetres(), DistanceUnit.KILOMETRES))),
					resorces.getString(R.string.s_mi_synopsis,
							DisplayStringFormatter.formatDistance(DistanceConverter.getMetresInUnit(race.getDistanceInMetres(), DistanceUnit.MILES))),
					DisplayStringFormatter.formatSecondsAsTime(resorces, seconds));

		case MILES:
			if (distanceUnitAlt == DistanceUnit.UNSPECIFIED)
				return resorces.getString(
						R.string.x_in_time_synopsis,
						formatName(resorces),
						resorces.getString(R.string.s_mi_synopsis,
								DisplayStringFormatter.formatDistance(DistanceConverter.getMetresInUnit(race.getDistanceInMetres(), DistanceUnit.MILES))),
						DisplayStringFormatter.formatSecondsAsTime(resorces, seconds));

			return resorces.getString(
					R.string.x_altx_in_time_synopsis,
					formatName(resorces),
					resorces.getString(R.string.s_mi_synopsis,
							DisplayStringFormatter.formatDistance(DistanceConverter.getMetresInUnit(race.getDistanceInMetres(), DistanceUnit.MILES))),
					resorces.getString(R.string.s_km_synopsis,
							DisplayStringFormatter.formatDistance(DistanceConverter.getMetresInUnit(race.getDistanceInMetres(), DistanceUnit.KILOMETRES))),
					DisplayStringFormatter.formatSecondsAsTime(resorces, seconds));
		}
	}

	public Race getRace() {
		return race;
	}

	public boolean isApplicableUnit(UnitFilterValue unitFilterPreference) {
		return unitTypes.contains(unitFilterPreference);
	}

	public static List<FormattedRace> getFormattedRaces(Context context) {
		List<FormattedRace> formattedRaces = new ArrayList<FormattedRace>();

		for (Race race : Race.getRaces(context)) {
			addRace(formattedRaces, race);
		}
		return formattedRaces;
	}

	private static void addRace(List<FormattedRace> formattedRaces, Race race) {
		DistanceUnit primUnit = DistanceConverter.estimatePrimaryDistanceUnit(race.getDistanceInMetres());
		if (race.getName() != null) {
			formattedRaces.add(new FormattedRace(race, DistanceUnit.MILES, UnitFilterValue.Metric));
			formattedRaces.add(new FormattedRace(race, DistanceUnit.KILOMETRES, UnitFilterValue.Imperial));
			formattedRaces.add(new FormattedRace(race, DistanceUnit.KILOMETRES, DistanceUnit.MILES, UnitFilterValue.Both));
			return;
		}
		switch (primUnit) {
		case KILOMETRES:
		case METRES:
			formattedRaces.add(new FormattedRace(race, primUnit, DistanceUnit.MILES, of(UnitFilterValue.Both, UnitFilterValue.Metric, UnitFilterValue.Imperial)));
			break;
		case MILES:
			formattedRaces.add(new FormattedRace(race, primUnit, of(UnitFilterValue.Imperial)));
			formattedRaces.add(new FormattedRace(race, primUnit, DistanceUnit.KILOMETRES, of(UnitFilterValue.Both, UnitFilterValue.Metric)));
			break;
		}

	}
}
