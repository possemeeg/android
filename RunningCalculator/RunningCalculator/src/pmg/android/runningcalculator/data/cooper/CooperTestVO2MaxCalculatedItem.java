package pmg.android.runningcalculator.data.cooper;

import pmg.android.runningcalculator.R;
import pmg.android.runningcalculator.data.InputValue;
import pmg.android.runningcalculator.view.DisplayStringFormatter;
import android.content.Context;

public class CooperTestVO2MaxCalculatedItem extends CooperTestCalculatedItem {
	private final String vo2Max;

	public CooperTestVO2MaxCalculatedItem(Context context, InputValue value, int synopsisStringId, double distanceInMetres) {
		super(context, value, synopsisStringId);
		this.vo2Max = context.getResources().getString(R.string.cooper_test_vo2max_calculation,
				DisplayStringFormatter.formatVO2Max((distanceInMetres - 504.9) / 44.73));
	}

	@Override
	public String getCalculation() {
		return vo2Max;
	}
}
