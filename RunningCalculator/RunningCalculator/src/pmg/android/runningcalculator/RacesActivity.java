package pmg.android.runningcalculator;

import java.util.HashMap;

import pmg.android.runningcalculator.data.InputValue;
import pmg.android.runningcalculator.data.InputValueGenerator.AddFilter;
import pmg.android.runningcalculator.data.db.RunningEvent;
import pmg.android.runningcalculator.data.db.RunningEvent.RacesColumns;
import pmg.android.runningcalculator.data.db.RunningEventProvider;
import pmg.android.runningcalculator.view.DisplayStringFormatter;
import pmg.android.runningcalculator.view.GeneratedItemListHandler;
import pmg.android.runningcalculator.view.KeyboardButtonHandler;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter.ViewBinder;

public class RacesActivity extends Activity implements KeyboardButtonHandler.NotifyTextChange {

	private static final String[] PROJECTION = new String[] { RacesColumns._ID, RacesColumns.NAME, RacesColumns.DISTANCE };
	private SimpleCursorAdapter mainListAdapter;

	private static final int COLUMN_INDEX_NAME = 1;
	private static final int COLUMN_INDEX_DISTANCE = 2;

	private final KeyboardButtonHandler keyboardButtonHandler = new KeyboardButtonHandler();
	private final GeneratedItemListHandler generatedItemListHandler = new GeneratedItemListHandler();

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);

		setContentView(R.layout.race_list);

		setupMainList();
		setupAddableList();
		attachButtons();

		keyboardButtonHandler.initialise((EditText) findViewById(android.R.id.edit), (ViewGroup) findViewById(R.id.button_parent_view), findViewById(R.id.back_button), this);

	}

	private void attachButtons() {
		findViewById(R.id.reset_button).setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getContentResolver().delete(RunningEvent.RacesColumns.CONTENT_URI, RacesColumns.BUILT_IN+"=0", null);
			}

		});
	}

	private void setupMainList() {
		mainListAdapter = new SimpleCursorAdapter(this, R.layout.race_item, null, new String[] { RacesColumns.NAME, RacesColumns.DISTANCE },
				new int[] { R.id.name, R.id.distance }, 0);

		mainListAdapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				switch (columnIndex) {
				case COLUMN_INDEX_NAME:
					String name = cursor.getString(columnIndex);
					if (name != null && !name.isEmpty())
						((TextView) view).setText(name);
					else
						((TextView) view).setText("");
					return true;
				case COLUMN_INDEX_DISTANCE:
					String namex = cursor.getString(COLUMN_INDEX_NAME);
					if (namex == null || namex.isEmpty())
						((TextView) view).setText(DisplayStringFormatter.getFormattedDistance(getResources(), cursor.getInt(columnIndex)));
					else
						((TextView) view).setText("");
					return true;
				}
				return false;
			}
		});

		GridView gridView = (GridView) findViewById(android.R.id.list);
		gridView.setAdapter(mainListAdapter);

		getLoaderManager().initLoader(0, null, new LoaderCallbacks<Cursor>() {
			@Override
			public Loader<Cursor> onCreateLoader(int id, Bundle args) {
				return new CursorLoader(RacesActivity.this, getIntent().getData(), PROJECTION, null, null, RacesColumns.DISTANCE);
			}

			@Override
			public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
				mainListAdapter.swapCursor(data);

			}

			@Override
			public void onLoaderReset(Loader<Cursor> loader) {
				mainListAdapter.swapCursor(null);
			}
		});
	}

	private void setupAddableList() {
		
		generatedItemListHandler.initList(getBaseContext(), (AbsListView)findViewById(R.id.listToAdd), R.layout.input_value_item_view, new GeneratedItemListHandler.InputValuelistHandler() {
			
			@Override
			public void itemClicked(InputValue inputValue, String text) {
				final ContentValues values = new ContentValues();
				values.put(RunningEvent.RacesColumns.DISTANCE,inputValue.getDistanceInMetres());
				String formatedName = DisplayStringFormatter.getFormattedDistance(getBaseContext().getResources(), (int)inputValue.getDistanceInMetres());
				if(!text.equals(formatedName) ){
//				switch(inputValue.getValueType()){
//				case DISTANCE_KILOMETRES:
//				case DISTANCE_METRES:
//				case DISTANCE_MILES:
//					break;
//				default:
					values.put(RunningEvent.RacesColumns.NAME, text);
				}
				getContentResolver().insert(RunningEvent.RacesColumns.CONTENT_URI, values);
				keyboardButtonHandler.resetText();
			}

			@Override
			public void listItemsChanged(HashMap<String, InputValue>[] items) {
			}
		}, new AddFilter() {
			
			@Override
			public boolean canAdd(InputValue inputValue) {
				return inputValue.isDistance();
			}
		});
		
	}

	@Override
	public void onTextChanged(String newText) {
		generatedItemListHandler.setText(newText);
	}

}
