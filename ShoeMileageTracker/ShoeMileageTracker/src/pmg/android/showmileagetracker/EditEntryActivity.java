package pmg.android.showmileagetracker;

import java.util.Calendar;

import pmg.android.showmileagetracker.R;
import pmg.android.showmileagetracker.db.ShoeAggQuery;
import pmg.android.showmileagetracker.db.ShoeQuery;
import pmg.android.showmileagetracker.db.Shoes.EntryColumns;
import pmg.android.showmileagetracker.db.Shoes.EntryXRefColumns;
import pmg.android.showmileagetracker.layouts.OkCancelLayout;
import pmg.android.showmileagetracker.util.ColourHelper;
import pmg.android.showmileagetracker.util.DistanceHelper;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

public class EditEntryActivity extends Activity implements OkCancelLayout.SaveHandler {
	private static final String TAG = EditEntryActivity.class.getName();

	private Cursor cursor;
	private long shoe;

	private static final String[] PROJECTION = new String[] { EntryXRefColumns._ID, // 0
			EntryXRefColumns.SHOE, // 1
			EntryXRefColumns.DATE, // 2
			EntryXRefColumns.DISTANCE, // 3
			EntryXRefColumns.SHOE_NAME, // 4
			EntryXRefColumns.SHOE_COLOUR // 5
	};

	private static final int COLUMN_INDEX_ID = 0;
	private static final int COLUMN_INDEX_SHOE = 1;
	private static final int COLUMN_INDEX_DATE = 2;
	private static final int COLUMN_INDEX_DISTANCE = 3;
	private static final int COLUMN_INDEX_SHOE_NAME = 4;
	private static final int COLUMN_INDEX_SHOE_COLOUR = 5;

	// fields
	private EditText distanceEditText;
	private DatePicker datePicker;

	static final int DATE_DIALOG_ID = 1;

	private final DistanceHelper distanceHelper;

	public EditEntryActivity() {
		distanceHelper = new DistanceHelper(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_entry);

		Intent intent = getIntent();
		String action = intent.getAction();
		if (Intent.ACTION_EDIT.equals(action)) {
			cursor = managedQuery(intent.getData(), PROJECTION, null, null, null);
		} else {
			if (!Intent.ACTION_INSERT.equals(action) || !intent.hasExtra(EntryColumns.SHOE)
					|| (shoe = intent.getLongExtra(EntryColumns.SHOE, -1)) == -1)
				shoe = getNextShoeId();

			Log.i(TAG, String.format("Adding entry for shoe %d", shoe));
			cursor = null;
		}

		setContentView(R.layout.new_entry);

		((OkCancelLayout) findViewById(R.id.okCancelLayout)).setSaveHandler(this);

		setupFields();
	}

	private long getNextShoeId() {
		return new ShoeAggQuery(this).getNextShoe().id;
	}

	private void setupFields() {
		// fields
		datePicker = (DatePicker) findViewById(R.id.datePicker);
		setDateToday();
		distanceEditText = (EditText) findViewById(R.id.distanceEditText);

	}

	private void setDateToday() {
		Calendar date = Calendar.getInstance();
		setDate(date);
	}

	private void setDate(long milliseconds) {
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(milliseconds);
		setDate(date);
	}

	private void setDate(Calendar date) {
		datePicker.updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
	}

	private long getDateInMilliseconds() {
		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, datePicker.getYear());
		date.set(Calendar.MONTH, datePicker.getMonth());
		date.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
		return date.getTimeInMillis();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

		int shoeColour;

		if (cursor != null) {
			cursor.requery();
			cursor.moveToFirst();

			shoe = cursor.getInt(COLUMN_INDEX_SHOE);
			setDate(cursor.getLong(COLUMN_INDEX_DATE));
			distanceEditText.setText(distanceHelper.getDistanceForDisplay(cursor.getInt(COLUMN_INDEX_DISTANCE)));
			shoeColour = cursor.getInt(COLUMN_INDEX_SHOE_COLOUR);
			setTitle(getResources().getString(R.string.edit_entry_title, cursor.getString(COLUMN_INDEX_SHOE_NAME)));
		} else {
			Cursor cursor = new ShoeQuery(this).queryShoe(shoe);
			shoeColour = cursor.getInt(ShoeQuery.COLUMN_INDEX_COLOUR);
			setTitle(getResources().getString(R.string.new_entry_title, cursor.getString(ShoeQuery.COLUMN_INDEX_NAME)));
		}
		findViewById(R.id.background).setBackgroundColor(ColourHelper.setLighterOfColour(shoeColour));
	}

	@Override
	public void onSave() {
		if (cursor == null)
			insertEntry();
		else
			saveEntry();
	}

	private void saveEntry() {
		if (cursor == null)
			return;

		final ContentValues values = getContentValues();
		if (values == null)
			return;

		try {
			getContentResolver().update(ContentUris.withAppendedId(EntryColumns.CONTENT_URI, cursor.getLong(COLUMN_INDEX_ID)), values, null, null);

		} catch (NullPointerException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private void insertEntry() {
		final ContentValues values = getContentValues();
		if (values == null)
			return;
		final Intent intent = getIntent();

		Uri uri = getContentResolver().insert(EntryColumns.CONTENT_URI, values);

		if (uri == null) {
			Log.e(TAG, "Failed to insert new entry into " + intent.getData());
			return;
		}
	}

	private ContentValues getContentValues() {
		final ContentValues values = new ContentValues();

		values.put(EntryColumns.SHOE, shoe);
		values.put(EntryColumns.DATE, getDateInMilliseconds());
		values.put(EntryColumns.DISTANCE, getDistanceInMetres());

		return values;
	}

	private int getDistanceInMetres() {
		return distanceHelper.convertFromEntry(distanceEditText.getText().toString());
	}
}
