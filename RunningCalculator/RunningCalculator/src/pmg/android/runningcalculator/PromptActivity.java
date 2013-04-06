package pmg.android.runningcalculator;

import java.util.HashMap;

import pmg.android.runningcalculator.data.InputValue;
import pmg.android.runningcalculator.data.db.RunningEvent.RacesColumns;
import pmg.android.runningcalculator.view.GeneratedItemListHandler;
import pmg.android.runningcalculator.view.KeyboardButtonHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;

public class PromptActivity extends Activity implements KeyboardButtonHandler.NotifyTextChange {

	private View promptMessageView;

	private final KeyboardButtonHandler keyboardButtonHandler = new KeyboardButtonHandler();
	private final GeneratedItemListHandler generatedItemListHandler = new GeneratedItemListHandler();

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);

		setContentView(R.layout.input_prompt);

		promptMessageView = findViewById(R.id.prompt_message);

		setupChoiceList();

		keyboardButtonHandler.initialise((EditText) findViewById(android.R.id.edit), (ViewGroup) findViewById(R.id.button_parent_view),
				findViewById(R.id.back_button), this);
		attachButtons();
	}

	private void setupChoiceList() {
		// list of available choices once input has been made
		generatedItemListHandler.initList(getBaseContext(), (AbsListView)findViewById(android.R.id.list), R.layout.input_value_item_view, new GeneratedItemListHandler.InputValuelistHandler() {
			
			@Override
			public void itemClicked(InputValue inputValue, String text) {
				Intent intent = new Intent(CoreApplication.ACTION_SHOW_DETAILS);
				intent.putExtra(CoreApplication.ACTIVITY_EXTRA_INPUT_VALUE, inputValue);
				startActivity(intent);
			}

			@Override
			public void listItemsChanged(HashMap<String, InputValue>[] items) {
				promptMessageView.setVisibility(items.length <= 0 ? View.VISIBLE : View.GONE);
			}
		});
	}

	private void attachButtons() {
		findViewById(R.id.races_button).setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, RacesColumns.CONTENT_URI));
			}

		});

		findViewById(R.id.settings_button).setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), MainPreferenceActivity.class));
			}

		});
	}

	@Override
	public void onTextChanged(String newText) {
		generatedItemListHandler.setText(newText);
	}
}
