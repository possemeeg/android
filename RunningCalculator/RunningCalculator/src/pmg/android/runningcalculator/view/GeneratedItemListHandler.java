package pmg.android.runningcalculator.view;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import pmg.android.runningcalculator.R;
import pmg.android.runningcalculator.data.InputValue;
import pmg.android.runningcalculator.data.InputValueGenerator;
import pmg.android.runningcalculator.data.InputValueList;

public class GeneratedItemListHandler {
	private final ArrayList<HashMap<String, InputValue>> inputValueAdapterMapList = new ArrayList<HashMap<String, InputValue>>();
	private SimpleAdapter valueListAdapter;
	private static final String VALUE_KEY = "value";

	public static interface InputValuelistHandler {
		void itemClicked(InputValue inputValue, String name);
		void listItemsChanged(HashMap<String, InputValue> [] items);
	}
	InputValuelistHandler inputValueListHandler;
	InputValueGenerator.AddFilter inputValueFilter;
	
	public void initList(final Context context, AbsListView listView, int layoutView, InputValuelistHandler inputValueListHandler){
		initList(context, listView, layoutView, inputValueListHandler, null);
	}
	public void initList(final Context context, AbsListView listView, int layoutView, InputValuelistHandler inputValueListHandler, InputValueGenerator.AddFilter inputValueFilter ){

		this.inputValueListHandler = inputValueListHandler;
		this.inputValueFilter = inputValueFilter;
		valueListAdapter = new SimpleAdapter(context, inputValueAdapterMapList, layoutView, new String[] { VALUE_KEY }, new int[] { R.id.input_value });

		valueListAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
			@Override
			public boolean setViewValue(View view, Object data, String textRepresentation) {
				InputValue inputValue = (InputValue) data;
				((TextView) view).setText(DisplayStringFormatter.formatInputValue(context.getResources(), inputValue));
				return true;
			}
		});
		
		listView.setAdapter(valueListAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				@SuppressWarnings("unchecked")
				HashMap<String, InputValue> item = (HashMap<String, InputValue>) valueListAdapter.getItem(position);
				
				GeneratedItemListHandler.this.inputValueListHandler.itemClicked(item.get(VALUE_KEY), ((android.widget.TextView)view).getText().toString());
			}
		});

	}

	public void setText(String newText) {
		if (newText.isEmpty()) {
			inputValueAdapterMapList.clear();
			notifyListItemsChanged();
		} else
			populateListFromInputSeed(newText);
	}

	@SuppressWarnings("unchecked")
	private void notifyListItemsChanged() {
		valueListAdapter.notifyDataSetChanged();
		inputValueListHandler.listItemsChanged(inputValueAdapterMapList.toArray((HashMap<String, InputValue>[])Array.newInstance(HashMap.class, inputValueAdapterMapList.size())));
		//promptMessageView.setVisibility(inputValueAdapterMapList.size() <= 0 ? View.VISIBLE : View.GONE);
	}

	public static class GeneratedItems {
		private InputValueList inputValues;

		public GeneratedItems(InputValueList inputValues) {
			this.inputValues = inputValues;
		}

		public GeneratedItems() {
		}

		public InputValueList getInputValues() {
			return inputValues;
		}

	}

	private void populateListFromInputSeed(final String newSeed) {
		new AsyncTask<String, Void, GeneratedItems>() {

			@Override
			protected GeneratedItems doInBackground(String... arg0) {
				GeneratedItems ret = new GeneratedItems();
				ret.inputValues = InputValueGenerator.generateValuesFromSeed(arg0[0], inputValueFilter);
				return ret;
			}

			@Override
			protected void onPostExecute(GeneratedItems generatedItems) {
				refresh(newSeed, generatedItems);
			}

		}.execute(newSeed);
	}

	private void refresh(String newSeed, GeneratedItems generatedItems) {
		InputValueList inputValueList = new InputValueList(newSeed);
		inputValueAdapterMapList.clear();
		for (InputValue value : generatedItems.inputValues.getValues()) {
			inputValueList.add(value);
			HashMap<String, InputValue> map = new HashMap<String, InputValue>();
			map.put(VALUE_KEY, value);
			inputValueAdapterMapList.add(map);
		}
		notifyListItemsChanged();
	}


}
