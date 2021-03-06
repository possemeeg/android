package pmg.android.runningcalculator.data.converters;

import pmg.android.runningcalculator.data.InputValue;
import pmg.android.runningcalculator.view.DisplayStringFormatter;
import android.content.Context;

public class PaceSpeedConverterCalculatedItem extends ConverterCalculatedItem {

	public PaceSpeedConverterCalculatedItem(Context context, InputValue inputValue, double to, int synopsisStringId, int calculationStringId) {
		super(context, inputValue, to, synopsisStringId, calculationStringId);
	}

	@Override
	protected String getFormattedFrom() {
		return DisplayStringFormatter.formatPace(getResources(), getFrom());
	}

	@Override
	protected String getFormattedTo() {
		return DisplayStringFormatter.formatSpeed(getTo());
	}

}
