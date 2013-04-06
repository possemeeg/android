package pmg.android.runningcalculator.data.converters;

import android.content.Context;
import pmg.android.runningcalculator.data.InputValue;
import pmg.android.runningcalculator.view.DisplayStringFormatter;

public class DistanceConverterCalculatedItem extends ConverterCalculatedItem {
	
	public DistanceConverterCalculatedItem(Context context, InputValue inputValue, double to, int synopsisStringId, int calculationStringId) {
		super(context, inputValue, to, synopsisStringId, calculationStringId);
	}

	@Override
	protected String getFormattedFrom() {
		return DisplayStringFormatter.formatDistance(getFrom());
	}

	@Override
	protected String getFormattedTo() {
		return DisplayStringFormatter.formatDistance(getTo());
	}

}
