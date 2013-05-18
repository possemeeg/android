package pmg.android.runningcalculator.data.cooper;

import pmg.android.runningcalculator.data.InputValue;
import pmg.android.runningcalculator.data.splits.FormattedLap;
import android.content.Context;

public class CooperTestConversionCalculatedItem extends CooperTestCalculatedItem {

	private final double distanceInMetres;
	private final FormattedLap lap;

	public CooperTestConversionCalculatedItem(Context context, InputValue inputValue, int synopsisStringId, double distanceInMetres, FormattedLap lap) {
		super(context, inputValue, synopsisStringId);
		this.distanceInMetres = distanceInMetres;
		this.lap = lap;
	}

	@Override
	public String getCalculation() {
		return lap.getCalculationAsString(getResources(), distanceInMetres, 12 * 60);
	}
}
