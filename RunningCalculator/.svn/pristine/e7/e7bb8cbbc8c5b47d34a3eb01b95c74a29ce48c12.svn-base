package pmg.android.runningcalculator.data;

import android.content.Context;
import android.content.res.Resources;

public abstract class CalculatedItem implements Comparable<CalculatedItem> {
	private final Context context;
	private final InputValue inputValue;
	
	public CalculatedItem(Context context, InputValue inputValue){
		this.context = context;
		this.inputValue = inputValue;
		
	}
	protected Resources getResources() {
		return context.getResources();
	}

	public InputValue getInputValue() {
		return inputValue;
	}

	public abstract String getSynopsis();
	public abstract String getCalculation();

	@Override
	public int compareTo(CalculatedItem rhs){
		return inputValue.compareTo(rhs.inputValue);
	}
}
