package pmg.android.runningcalculator;

import pmg.android.runningcalculator.data.CalculatedItemList;
import pmg.android.runningcalculator.data.InputValue;
import pmg.android.runningcalculator.data.ItemGenerator;
import pmg.android.runningcalculator.R;
import android.app.ListActivity;
import android.os.Bundle;

public class HomeActivity extends ListActivity {

	private CalculatedItemList calculatedItemList;
	private ItemGenerator itemGenerator;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		itemGenerator = new ItemGenerator(this);

		calculatedItemList = new CalculatedItemList(this);
		setListAdapter(calculatedItemList.getAdapter());
		
		InputValue value = (InputValue) getIntent().getSerializableExtra(CoreApplication.ACTIVITY_EXTRA_INPUT_VALUE);
		calculatedItemList.refresh(itemGenerator.generateItems(value));
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}