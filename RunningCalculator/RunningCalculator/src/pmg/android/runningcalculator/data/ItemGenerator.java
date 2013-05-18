package pmg.android.runningcalculator.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import pmg.android.runningcalculator.PreferenceValues;
import pmg.android.runningcalculator.data.converters.ConverterCalculatedItem;
import pmg.android.runningcalculator.data.cooper.CooperTestCalculatedItem;
import pmg.android.runningcalculator.data.race_predict.RacePredicterCalculatedItem;
import pmg.android.runningcalculator.data.splits.RaceBreakdownCalculatedItem;

public class ItemGenerator {
	private final Context context;
	private final PreferenceValues preferenceValues;
	
	public ItemGenerator(Context context){
		this.context = context;
		this.preferenceValues = new PreferenceValues(context);
	}
	
	
	public List<CalculatedItem> generateItems(List<InputValue> inputValues){
		List<CalculatedItem> list = new ArrayList<CalculatedItem>();
		for (InputValue value : inputValues) {
			list.addAll(generateItems(value));
		}
		
		return list;
	}
	
	public List<CalculatedItem> generateItems(InputValue value){
		List<CalculatedItem> list = combineDuplicateSynopises(generateFilteredItems(value));

		Collections.sort(list, new Comparator<CalculatedItem>() {
			@Override
			public int compare(CalculatedItem lhs, CalculatedItem rhs) {
				return lhs.compareTo(rhs);
			}
		});
		//value.setGeneratedItemsList(list);
		return list;
	}
	
	private List<CalculatedItem> generateFilteredItems(InputValue value) {
		List<CalculatedItem> list = new ArrayList<CalculatedItem>();

		ConverterCalculatedItem.generateItems(context, value, preferenceValues, list);
		RaceBreakdownCalculatedItem.generateItems(context, value, preferenceValues, list);
		CooperTestCalculatedItem.generateItems(context, value, preferenceValues, list);
		RacePredicterCalculatedItem.generateItems(context, value, preferenceValues, list);

		return list;
	}

	private static class DuplicateCalculatedItems {
		public final int location;
		public final CombinedCalculatedItem items;

		public DuplicateCalculatedItems(int location, CombinedCalculatedItem items) {
			this.location = location;
			this.items = items;
		}
	}
	private static class DuplicateCalculatedItemKey implements Comparable<DuplicateCalculatedItemKey> {
		private final String synopsis;
		private final InputValue inputValue;
		public DuplicateCalculatedItemKey(String synopsis, InputValue inputValue){
			this.synopsis = synopsis;
			this.inputValue = inputValue;
		}

		public InputValue getInputValue() {
			return inputValue;
		}

		@Override
		public int hashCode() {
			return synopsis.hashCode();
		}

		@Override
		public boolean equals(Object other) {
			DuplicateCalculatedItemKey o = (DuplicateCalculatedItemKey)other; 
			return synopsis.equals(o.synopsis) && inputValue.compareTo(o.inputValue) == 0;
		}

		@Override
		public int compareTo(DuplicateCalculatedItemKey another) {
			int sct = synopsis.compareTo(another.synopsis);
			return sct != 0 ? sct : inputValue.compareTo(another.inputValue);
		}
	}


	private List<CalculatedItem> combineDuplicateSynopises(List<CalculatedItem> calculatedItems) {
		HashMap<DuplicateCalculatedItemKey, DuplicateCalculatedItems> combinedItemsMap = new HashMap<DuplicateCalculatedItemKey, DuplicateCalculatedItems>();

		int max = calculatedItems.size();
		for (int pos = 0; pos < max;) {
			CalculatedItem calculatedItem = calculatedItems.get(pos);
			DuplicateCalculatedItemKey key = new DuplicateCalculatedItemKey(calculatedItem.getSynopsis(),calculatedItem.getInputValue());
			if (combinedItemsMap.containsKey(key)) {
				combinedItemsMap.get(key).items.add(calculatedItem);
				calculatedItems.remove(pos);
				--max;
			} else {
				combinedItemsMap.put(key, new DuplicateCalculatedItems(pos, new CombinedCalculatedItem(context, key.getInputValue(), calculatedItem)));
				++pos;
			}
		}
		for (DuplicateCalculatedItems duplicatedItems : combinedItemsMap.values()) {
			if (duplicatedItems.items.getCount() > 1) {
				calculatedItems.set(duplicatedItems.location, duplicatedItems.items);
			}
		}

		return calculatedItems;
	}

}
