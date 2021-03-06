package pmg.android.runningcalculator.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import pmg.android.runningcalculator.R;
import pmg.android.runningcalculator.view.CalculatedItemViewBinder;

public class CalculatedItemList {

	private final ArrayList<HashMap<String, CalculatedItem>> data = new ArrayList<HashMap<String, CalculatedItem>>();
	private final SimpleAdapter listAdapter;
	private final static String VALUE_KEY = "value";

	public CalculatedItemList(Context context) {
		listAdapter = new CalculatedItemAdapter(context, data, R.layout.calculated_item_view, new String[] { VALUE_KEY },
				new int[] { R.id.parent_view });
		listAdapter.setViewBinder(new CalculatedItemViewBinder());
	}

	public ListAdapter getAdapter() {
		return listAdapter;
	}

	public void refresh(List<CalculatedItem> items) {
		data.clear();
		for (CalculatedItem calculatedItem : items) {

			HashMap<String, CalculatedItem> item = new ListItem(calculatedItem);
			data.add(item);
		}

		listAdapter.notifyDataSetChanged();
	}

	public void clear() {
		data.clear();
		listAdapter.notifyDataSetChanged();
	}

	private static class ListItem extends HashMap<String, CalculatedItem> {
		private static final long serialVersionUID = -4923515952903234888L;

		public ListItem(CalculatedItem calculatedItem) {
			put(VALUE_KEY, calculatedItem);
		}
	}
}
