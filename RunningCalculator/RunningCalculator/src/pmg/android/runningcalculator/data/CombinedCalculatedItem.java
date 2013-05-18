package pmg.android.runningcalculator.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class CombinedCalculatedItem extends CalculatedItem {
	private final List<CalculatedItem> calculatedItems = new ArrayList<CalculatedItem>();

	public CombinedCalculatedItem(Context context, InputValue inputValue, CalculatedItem calculatedItem) {
		super(context, inputValue);
		add(calculatedItem);
	}

	public void add(CalculatedItem calculatedItem) {
		this.calculatedItems.add(calculatedItem);
	}

	public int getCount() {
		return calculatedItems.size();
	}

	@Override
	public String getSynopsis() {
		return calculatedItems.get(0).getSynopsis();
	}

	@Override
	public String getCalculation() {
		StringBuilder sb = new StringBuilder();
		boolean empty = true;
		for (CalculatedItem calculatedItem : calculatedItems) {
			if (empty)
				empty = false;
			else
				sb.append('\n');
			sb.append(calculatedItem.getCalculation());
		}
		return sb.toString();
	}
}
