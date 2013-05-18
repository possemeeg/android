package pmg.android.runningcalculator.data;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.SimpleAdapter;

public class CalculatedItemAdapter extends SimpleAdapter {

	public CalculatedItemAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}
