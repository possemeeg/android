package pmg.android.showmileagetracker;

import pmg.android.showmileagetracker.R;
import pmg.android.showmileagetracker.db.Shoes.ShoeColumns;
import pmg.android.showmileagetracker.layouts.OkCancelLayout;
import pmg.android.showmileagetracker.util.ColourHelper;
import pmg.android.showmileagetracker.util.ColourPicker;
import pmg.android.showmileagetracker.util.DateHelper;
import pmg.android.showmileagetracker.util.DistanceHelper;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditShoeActivity extends Activity implements OkCancelLayout.SaveHandler {
	private static final String TAG = EditShoeActivity.class.getName();

	private Cursor cursor;

	private static final String[] PROJECTION = new String[] { ShoeColumns._ID, // 0
			ShoeColumns.NAME, // 1
			ShoeColumns.INITIAL_DISTANCE, // 2
			ShoeColumns.MAX_DISTANCE, // 3
			ShoeColumns.COLOUR, // 4
			ShoeColumns.RETIRED_DATE // 5
	};

	private static final int COLUMN_INDEX_NAME = 1;
	private static final int COLUMN_INDEX_INITIAL_DISTANCE = 2;
	private static final int COLUMN_INDEX_MAX_DISTANCE = 3;
	private static final int COLUMN_INDEX_COLOUR = 4;
	private static final int COLUMN_INDEX_RETIRED_DATE = 5;

	// fields
	private EditText nameEditText;
	private EditText initialDistanceEditText;
	private EditText maxDistanceEditText;
	private View colourView;
	private int colour;
	private View shoeRetiredViewContainer;
	private TextView shoeRetiredTextView;

	private static final int PICK_COLOUR_DIALOG_ID = 1;

	private final DistanceHelper distanceHelper;
	private final ColourHelper colourHelper;

	public EditShoeActivity() {
		super();
		this.distanceHelper = new DistanceHelper(this);
		this.colourHelper = new ColourHelper(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		final String action = intent.getAction();
		if (Intent.ACTION_INSERT.equals(action)) {
			cursor = null;
		} else if (Intent.ACTION_EDIT.equals(action)) {
			cursor = managedQuery(intent.getData(), PROJECTION, null, null, null);
		}

		setContentView(R.layout.new_shoe);

		((OkCancelLayout) findViewById(R.id.okCancelLayout)).setSaveHandler(this);

		// fields
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		initialDistanceEditText = (EditText) findViewById(R.id.initialDistanceEditText);
		maxDistanceEditText = (EditText) findViewById(R.id.maxDistanceEditText);
		colourView = (View) findViewById(R.id.colourView);
		shoeRetiredViewContainer = (View) findViewById(R.id.shoeRetiredViewContainer);
		shoeRetiredTextView = (TextView) findViewById(R.id.shoeRetiredTextView);

		Button pickDate = (Button) findViewById(R.id.pickColourButton);
		pickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(PICK_COLOUR_DIALOG_ID);
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (cursor != null) {
			cursor.requery();
			cursor.moveToFirst();

			nameEditText.setText(cursor.getString(COLUMN_INDEX_NAME));
			initialDistanceEditText.setText(distanceHelper.getDistanceForDisplay(cursor.getInt(COLUMN_INDEX_INITIAL_DISTANCE)));
			maxDistanceEditText.setText(distanceHelper.getDistanceForDisplay(cursor.getInt(COLUMN_INDEX_MAX_DISTANCE)));
			updateColour(cursor.getInt(COLUMN_INDEX_COLOUR));
			if (!cursor.isNull(COLUMN_INDEX_RETIRED_DATE)) {
				shoeRetiredViewContainer.setVisibility(View.VISIBLE);
				shoeRetiredTextView.setText(getResources().getString(R.string.retired_shoe_display_pattern,
						DateHelper.getDisplayDate(this, cursor.getLong(COLUMN_INDEX_RETIRED_DATE))));
			} else {
				shoeRetiredViewContainer.setVisibility(View.GONE);
			}
		} else {
			updateColour(colourHelper.getNextFreeColour());
		}
	}

	@Override
	public void onSave() {
		if (cursor == null)
			insertShoe();
		else
			saveShoe();

	}

	private void saveShoe() {
		final ContentValues values = getContentValues();
		if (values == null)
			return;

		try {
			getContentResolver().update(getIntent().getData(), values, null, null);

		} catch (NullPointerException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private void insertShoe() {
		final ContentValues values = getContentValues();
		if (values == null)
			return;

		Uri uri = getContentResolver().insert(getIntent().getData(), values);
		if (uri == null) {
			Log.e(TAG, "Failed to insert new shoe into " + getIntent().getData());
			finish();
			return;
		}
	}

	private ContentValues getContentValues() {
		final ContentValues values = new ContentValues();

		values.put(ShoeColumns.NAME, nameEditText.getText().toString());
		putDistanceInMetres(values, ShoeColumns.INITIAL_DISTANCE, initialDistanceEditText);
		putDistanceInMetres(values, ShoeColumns.MAX_DISTANCE, maxDistanceEditText);
		values.put(ShoeColumns.COLOUR, colour);

		return values;
	}

	private void putDistanceInMetres(ContentValues values, String column, EditText editText) {
		Editable text = editText.getText();
		if (text.length() <= 0)
			return;
		values.put(column, distanceHelper.convertFromEntry(text.toString()));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case PICK_COLOUR_DIALOG_ID:
			return new ColourPicker(this).pickColour(new ColourPicker.SetColour() {
				@Override
				public void setColour(String name, int colour) {
					updateColour(colour);
				}

			});
		}
		return null;
	}

	private void updateColour(int colour) {
		this.colour = colour;
		colourView.setBackgroundColor(colour);
	}

}
