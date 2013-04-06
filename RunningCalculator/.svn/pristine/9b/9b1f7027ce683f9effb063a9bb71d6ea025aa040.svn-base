package pmg.android.runningcalculator.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;

public class InputValueList implements Serializable {
	private static final long serialVersionUID = 8906957380420957236L;

	private String seed;
	private List<InputValue> values;
	
	public InputValueList(String seed){
		this(seed,new ArrayList<InputValue>());
	}
	public InputValueList(String seed, InputValue value){
		this(seed,new ArrayList<InputValue>());
		values.add(value);
	}
	public InputValueList(String seed, List<InputValue> values){
		this.seed = seed;
		this.values = values;
	}

	public void add(InputValue inputValue){
		values.add(inputValue);
	}
	
	public String getSeed() {
		return seed;
	}
	public List<InputValue> getValues() {
		return values;
	}

	private static final String EXTRA_INPUT_VALUE_LIST = "input_value";

	public static Intent addAsIntentExtra(Intent intent, InputValueList list){
		if(list != null)
			intent.putExtra(EXTRA_INPUT_VALUE_LIST, list );
		return intent;
	}
	public static InputValueList getFromIntent(Intent intent){
		return (InputValueList)intent.getSerializableExtra(EXTRA_INPUT_VALUE_LIST);
	}

}
