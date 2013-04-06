package pmg.android.runningcalculator.data.cooper;

import java.util.List;

import pmg.android.runningcalculator.PreferenceValues;
import pmg.android.runningcalculator.R;
import pmg.android.runningcalculator.data.CalculatedItem;
import pmg.android.runningcalculator.data.InputValue;
import pmg.android.runningcalculator.data.RunningConstants;
import pmg.android.runningcalculator.data.splits.FormattedLap;
import pmg.android.runningcalculator.view.DisplayStringFormatter;
import android.content.Context;

public abstract class CooperTestCalculatedItem extends CalculatedItem {

	private final int synopsisStringId;

	public CooperTestCalculatedItem(Context context, InputValue inputValue, int synopsisStringId) {
		super(context,inputValue);
		this.synopsisStringId = synopsisStringId;
	}

	@Override
	public String getSynopsis() {
		return getResources().getString(synopsisStringId, DisplayStringFormatter.formatDistance(getInputValue().getValue()));
	}

	private static final FormattedLap[] laps = new FormattedLap[]{ 
		FormattedLap.L_TRACK_LAP,
		FormattedLap.L_TRACK_HALF_LAP,
		FormattedLap.L_1km,
		FormattedLap.L_1mi
	};
	
	public static void generateItems(Context context, InputValue value, PreferenceValues preferenceValues, List<CalculatedItem> list) {
		if(!value.isDistance()) return;
			
		double distanceInMetres = value.getDistanceInMetres();
		if(distanceInMetres < RunningConstants.MIN_COOPERS_METRES || distanceInMetres > RunningConstants.MAX_COOPERS_METRES) return;

		int synopsisStringId;
		
		switch(value.getValueType()){
		case DISTANCE_KILOMETRES: synopsisStringId = R.string.cooper_test_km_synopsis; break;
		case DISTANCE_MILES: synopsisStringId = R.string.cooper_test_mi_synopsis; break;
		case DISTANCE_TRACKLAPS: synopsisStringId = R.string.cooper_test_laps_synopsis; break;
		case DISTANCE_METRES: 
		default:
			synopsisStringId = R.string.cooper_test_m_synopsis; break;
		}
		
		for(FormattedLap lap : laps){
			if(lap.isViewableWithPreferences(preferenceValues.getUnitFilterPreference()))
				list.add(new CooperTestConversionCalculatedItem(context, value, synopsisStringId, distanceInMetres, lap));
		}
		list.add(new CooperTestVO2MaxCalculatedItem(context, value, synopsisStringId, distanceInMetres));
	}
}
