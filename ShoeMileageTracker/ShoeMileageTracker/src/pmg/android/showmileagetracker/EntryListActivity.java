package pmg.android.showmileagetracker;

import pmg.android.showmileagetracker.db.Shoes.EntryColumns;
import pmg.android.showmileagetracker.db.Shoes.EntryXRefColumns;
import pmg.android.showmileagetracker.util.ActivityAlertDialog;
import pmg.android.showmileagetracker.util.DateHelper;
import pmg.android.showmileagetracker.util.DistanceHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter.ViewBinder;

public class EntryListActivity extends ListActivity implements OnSharedPreferenceChangeListener, FilterQueryProvider {
	private static final String[] PROJECTION = new String[] { EntryXRefColumns._ID, // 0
			EntryXRefColumns.SHOE, // 1
			EntryXRefColumns.DATE, // 2
			EntryXRefColumns.DISTANCE, // 3
			EntryXRefColumns.SHOE_NAME, // 4
			EntryXRefColumns.SHOE_COLOUR, // 5
			EntryXRefColumns.SHOE_RETIRED_DATE // 6
	};
	private static final int COLUMN_INDEX_DATE = 2;
	private static final int COLUMN_INDEX_DISTANCE = 3;
	private static final int COLUMN_INDEX_SHOE_NAME = 4;
	private static final int COLUMN_INDEX_SHOE_COLOUR = 5;
	private static final int COLUMN_INDEX_SHOE_RETIRED_DATE = 6;

	private final DistanceHelper distanceHelper;
	private String preferenceKeyShowRetired;
	private SharedPreferences preferences;

	public EntryListActivity() {
		distanceHelper = new DistanceHelper(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);

		preferenceKeyShowRetired = getResources().getString(R.string.preference_key_show_retired_activities);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		preferences.registerOnSharedPreferenceChangeListener(this);

		getListView().setOnCreateContextMenuListener(this);
		setupList();

	}

	private void setupList() {
		Cursor cursor = runQuery(getQuerySetlection());

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.entrylist_item, cursor, new String[] { EntryXRefColumns.SHOE_NAME,
				EntryXRefColumns.DISTANCE, EntryXRefColumns.DATE, EntryXRefColumns.SHOE_COLOUR, EntryXRefColumns.SHOE_RETIRED_DATE }, new int[] {
				R.id.shoeName, R.id.distance, R.id.date, R.id.shoeName, R.id.shoeRetiredText });

		adapter.setFilterQueryProvider(this);

		adapter.setViewBinder(new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				switch (columnIndex) {
				case COLUMN_INDEX_DISTANCE:
					((TextView) view).setText(distanceHelper.getDistanceDisplay(cursor.getInt(COLUMN_INDEX_DISTANCE)));
					return true;
				case COLUMN_INDEX_DATE:
					((TextView) view).setText(DateHelper.getDisplayDate(getApplicationContext(), cursor.getLong(COLUMN_INDEX_DATE)));
					return true;
				case COLUMN_INDEX_SHOE_COLOUR:
					view.setBackgroundColor(getBackgroundForShoeText(cursor));
					return true;
				case COLUMN_INDEX_SHOE_RETIRED_DATE:
					if (!cursor.isNull(COLUMN_INDEX_SHOE_RETIRED_DATE)) {
						((TextView) view).setText(getResources().getString(R.string.retired_shoe_display_pattern,
								DateHelper.getDisplayDate(getApplicationContext(), cursor.getLong(COLUMN_INDEX_SHOE_RETIRED_DATE))));
					}
					return true;
				}
				return false;
			}
		});
		setListAdapter(adapter);
	}

	@Override
	public Cursor runQuery(CharSequence filter) {

		return managedQuery(getIntent().getData(), PROJECTION, filter instanceof String ? (String) filter : null, null, EntryXRefColumns.DATE
				+ " DESC");
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String key) {

		if (key.equals(preferenceKeyShowRetired)) {
			((SimpleCursorAdapter) getListView().getAdapter()).getFilter().filter(getQuerySetlection());
		}
	}

	private String getQuerySetlection() {
		return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(preferenceKeyShowRetired, false) ? null : String
				.format("%s IS NULL", EntryXRefColumns.SHOE_RETIRED_DATE);
	}

	private int getBackgroundForShoeText(Cursor cursor) {
		return cursor.isNull(COLUMN_INDEX_SHOE_RETIRED_DATE) ? cursor.getInt(COLUMN_INDEX_SHOE_COLOUR) : Color.LTGRAY;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

		Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
		if (cursor == null) {
			return;
		}

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.entrylist_context, menu);

		menu.setHeaderTitle(String.format("%s - %s", cursor.getString(COLUMN_INDEX_SHOE_NAME),
				DateHelper.getDisplayDate(this, cursor.getLong(COLUMN_INDEX_DATE))));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

		Uri uri = ContentUris.withAppendedId(getIntent().getData(), info.id);

		switch (item.getItemId()) {
		case R.id.context_open_entry:
			// Launch activity to view/edit the currently selected item
			startActivity(new Intent(Intent.ACTION_EDIT, uri));
			return true;
		case R.id.context_delete_entry:
			// Delete the note that the context menu is for
			Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
			deleteItem(info.id, cursor.getLong(COLUMN_INDEX_DATE));
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		startActivity(new Intent(Intent.ACTION_EDIT, ContentUris.withAppendedId(getIntent().getData(), id)));
	}
	
	private class ConfirmDeleteDialog extends ActivityAlertDialog {

		private long activeId;
		private long activeDate;

		public ConfirmDeleteDialog(Activity activity) {
			super(activity);
		}

		public void show(long id, long date) {
			activeId = id;
			activeDate = date;
			create().show();
		}

		private Dialog create() {

			return new AlertDialog.Builder(getActivity()).setTitle(R.string.title_confirm_delete_entry)
					.setMessage(getResources().getString(R.string.message_confirm_delete_entry, DateHelper.getDisplayDate(getActivity(), activeDate)))
					.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							deleteItemNoConfirm(activeId);

						}
					}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						}
					}).create();
		}
	}
	private ConfirmDeleteDialog confirmDeleteDialog;
	private ConfirmDeleteDialog getConfirmDeleteDialog() {
		return confirmDeleteDialog != null ? confirmDeleteDialog : (confirmDeleteDialog = new ConfirmDeleteDialog(this));
	}
	private void deleteItem(long id, long date) {
		getConfirmDeleteDialog().show(id, date);
	}
	private void deleteItemNoConfirm(long id)	{
		getContentResolver().delete(ContentUris.withAppendedId(EntryColumns.CONTENT_URI, id), null, null);
	}
}
