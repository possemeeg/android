package pmg.android.showmileagetracker.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pmg.android.showmileagetracker.EditShoeActivity;
import pmg.android.showmileagetracker.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class ColourPicker {
	public interface SetColour
	{
		public void setColour(String name, int colour);
	}
	
	public ColourPicker(Context context)
	{
		this.context = context;
	}

	public AlertDialog pickColour(final SetColour setter)
	{
	    final SimpleAdapter adapter = getColourSimpleAdapter();

	    LayoutInflater inflater = LayoutInflater.from(context);
	    final GridView grid = (GridView)inflater.inflate(R.layout.colourlist, null);
	    grid.setAdapter(adapter);
		
	    final AlertDialog dialog = new AlertDialog.Builder(context)
				.setTitle(R.string.select_colour_dialog)
				.setView(grid)
				.create();		

		grid.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Map<String, Comparable> item = (Map<String, Comparable>)adapter.getItem(position);
				setter.setColour((String)item.get(COLOUR_TEXT_KEY), (Integer)item.get(VALUE_KEY));
				dialog.dismiss();
			}
		});
		
		return dialog;
	}

	private static final String TAG = EditShoeActivity.class.getName();
	private static final String COLOUR_TEXT_KEY = "colour";
	private static final String VALUE_KEY = "value";
	
	private final Context context;
	
	private SimpleAdapter getColourSimpleAdapter() {
		SimpleAdapter adapter = new SimpleAdapter(context, getColours(), R.layout.colourlist_item, new String[] { VALUE_KEY }, new int[] {
				R.id.colour });
		adapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Object data, String textRepresentation) {
				if (data instanceof Integer) {
					view.setBackgroundColor((Integer) data);
					return true;
				}
				return false;
			}
		});

		return adapter;
	}

	@SuppressWarnings("rawtypes")
	private List<Map<String, Comparable>> getColours() {
		ColourHelper colourHelper = new ColourHelper(context);
		List<Map<String, Comparable>> coloursForDialog = new ArrayList<Map<String, Comparable>>();
		Resources resources = context.getResources();
		
		for(String colour : resources.getStringArray(R.array.colours))
		{
			Map<String, Comparable> map1 = new HashMap<String, Comparable>();
			try {
				int colourValue = Color.class.getField(colour).getInt(null);
				map1.put(COLOUR_TEXT_KEY, colourHelper.getColourName(colourValue));
				map1.put(VALUE_KEY, colourValue);
				coloursForDialog.add(map1);
			} catch (IllegalArgumentException e) {
				Log.e(TAG, e.toString());
			} catch (SecurityException e) {
				Log.e(TAG, e.toString());
			} catch (IllegalAccessException e) {
				Log.e(TAG, e.toString());
			} catch (NoSuchFieldException e) {
				Log.e(TAG, e.toString());
			}
		}
		return coloursForDialog;
	}

}
