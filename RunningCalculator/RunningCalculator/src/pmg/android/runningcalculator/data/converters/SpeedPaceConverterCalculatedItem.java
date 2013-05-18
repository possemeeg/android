package pmg.android.runningcalculator.data.converters;

import pmg.android.runningcalculator.data.InputValue;
import pmg.android.runningcalculator.view.DisplayStringFormatter;
import android.content.Context;

public class SpeedPaceConverterCalculatedItem extends ConverterCalculatedItem {

	public SpeedPaceConverterCalculatedItem(Context context, InputValue inputValue, double to, int synopsisStringId, int calculationStringId) {
		super(context, inputValue, to, synopsisStringId, calculationStringId);
	}

	@Override
	protected String getFormattedFrom() {
		return DisplayStringFormatter.formatSpeed(getFrom());
	}

	@Override
	protected String getFormattedTo() {
		return DisplayStringFormatter.formatPace(getResources(), getTo());
	}

}
