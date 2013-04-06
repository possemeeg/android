package pmg.android.runningcalculator.view;

import pmg.android.runningcalculator.R;
import pmg.android.runningcalculator.data.CalculatedItem;
import android.view.View;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class CalculatedItemViewBinder implements ViewBinder {

	@Override
	public boolean setViewValue(View view, Object data, String textRepresentation) {
		if(!(data instanceof CalculatedItem))
			return false;
		
		CalculatedItem calculatedItem = (CalculatedItem)data;
		
		((TextView)view.findViewById(R.id.synopsis_view)).setText(calculatedItem.getSynopsis());
		((TextView)view.findViewById(R.id.calculation_view)).setText(calculatedItem.getCalculation());
				
		return true;
	}

}
