package pmg.android.showmileagetracker;

import pmg.android.showmileagetracker.R;
import pmg.android.showmileagetracker.db.ChangeObserver;
import pmg.android.showmileagetracker.db.ShoeQuery;
import pmg.android.showmileagetracker.db.Shoes.EntryColumns;
import pmg.android.showmileagetracker.db.Shoes.EntryXRefColumns;
import pmg.android.showmileagetracker.db.Shoes.ShoeAggColumns;
import pmg.android.showmileagetracker.db.Shoes.ShoeColumns;
import pmg.android.showmileagetracker.util.ActivityAlertDialog;
import pmg.android.showmileagetracker.util.ActivityHelper;
import pmg.android.showmileagetracker.util.ColourHelper;
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
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class StatusActivity extends ListActivity implements OnSharedPreferenceChangeListener, FilterQueryProvider {
	// @formatter:off
	private static final String[] PROJECTION = new String[] { 
			ShoeAggColumns._ID, // 0
			ShoeAggColumns.NAME, // 1
			ShoeAggColumns.COLOUR, // 2
			ShoeAggColumns.MAX_DISTANCE, // 3
	        ShoeAggColumns.ACTIVITY_COUNT, // 4
			ShoeAggColumns.TOTAL_DISTANCE, // 5
			ShoeAggColumns.LAST_DATE, // 6
			ShoeAggColumns.RETIRED_DATE // 7
	};
	// @formatter:on
	// private Cursor cursor;
	private ChangeObserver changeObserver;

	// column indexes
	private static final int COLUMN_INDEX_NAME = 1;
	private static final int COLUMN_INDEX_COLOUR = 2;
	private static final int COLUMN_INDEX_MAX_DISTANCE = 3;
	private static final int COLUMN_INDEX_ACTIVITY_COUNT = 4;
	private static final int COLUMN_TOTAL_DISTANCE = 5;
	private static final int COLUMN_LAST_DATE = 6;
	private static final int COLUMN_INDEX_RETIRE_DATE = 7;

	// dialogs
	private RetireShoeDialog retireShoeDialog;
	private ConfirmDeleteDialog confirmDeleteDialog;
	private DeleteRetiredDialog deleteRetiredDialog;

	// fields
	private TextView lastShoeDisplay;

	private final DistanceHelper distanceHelper;

	// prefs
	private String preferenceKeyShowRetired;
	private SharedPreferences preferences;

	public StatusActivity() {
		distanceHelper = new DistanceHelper(this);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);

		preferenceKeyShowRetired = getResources().getString(R.string.preference_key_show_retired_shoes);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		preferences.registerOnSharedPreferenceChangeListener(this);

		Intent intent = getIntent();
		if (intent.getData() == null) {
			intent.setData(ShoeAggColumns.CONTENT_URI);
		}

		setupFields();
		setupList();
		populate();

		changeObserver = new ChangeObserver(getContentResolver(), new Uri[] { ShoeColumns.CONTENT_URI, EntryColumns.CONTENT_URI },
				new ChangeObserver.ChangeObserverHandler() {

					@Override
					public void onChange(boolean selfChange) {
						populate();
					}
				});
	}

	protected void onStart() {
		super.onStart();
		changeObserver.startObserving();
	}

	protected void onStop() {
		super.onStop();
		changeObserver.stopObserving();
	}

	private void setupFields() {
		lastShoeDisplay = (TextView) findViewById(R.id.next_shoe);

	}

	private void setupList() {

		Cursor cursor = runQuery(getQuerySetlection());

		// @formatter:off
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.statuslist_item, cursor, 
				new String[] { /*ShoeAggColumns._ID,*/ ShoeAggColumns.NAME, ShoeAggColumns.COLOUR, ShoeAggColumns.TOTAL_DISTANCE, 
								ShoeAggColumns.MAX_DISTANCE, ShoeAggColumns.LAST_DATE,
								ShoeAggColumns.RETIRED_DATE}, 
				new int[] { /*R.id.id,*/ R.id.name, R.id.itemTableLayout, R.id.total_distance, 
								R.id.progress, R.id.last_date,
								R.id.shoeRetiredText});
		
		adapter.setFilterQueryProvider(this);
		
		// @formatter:on
		adapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

				switch (columnIndex) {
				case COLUMN_TOTAL_DISTANCE:
					int current = cursor.getInt(COLUMN_TOTAL_DISTANCE);
					((TextView) view).setText(distanceHelper.getDistanceDisplay(current));
					return true;

				case COLUMN_LAST_DATE:
					if (cursor.isNull(COLUMN_LAST_DATE))
						((TextView) view).setText(getText(R.string.null_last_activity_date));
					else
						((TextView) view).setText(DateHelper.getDisplayDate(getApplicationContext(), cursor.getLong(COLUMN_LAST_DATE)));
					return true;

				case COLUMN_INDEX_MAX_DISTANCE:
					int max = cursor.isNull(COLUMN_INDEX_MAX_DISTANCE) ? 0 : cursor.getInt(COLUMN_INDEX_MAX_DISTANCE);
					if (max <= 0)
						view.setVisibility(View.GONE);
					else {
						view.setVisibility(View.VISIBLE);
						int currentDis = cursor.getInt(COLUMN_TOTAL_DISTANCE);
						((ProgressBar) view).setMax(max);
						((ProgressBar) view).setProgress(currentDis);
					}

					return true;
				case COLUMN_INDEX_COLOUR:
					if (cursor.isNull(COLUMN_INDEX_COLOUR))
						return true;
					int shoeColour = cursor.getInt(COLUMN_INDEX_COLOUR);
					GradientDrawable bk = (GradientDrawable)view.getBackground();
					bk.setColor(ColourHelper.setLighterOfColour(shoeColour));
					bk.setStroke(2, shoeColour);
					View header = view.findViewById(R.id.nameTableRow);
					bk = (GradientDrawable)header.getBackground();
					bk.setColor(shoeColour);
					return true;
				case COLUMN_INDEX_RETIRE_DATE:
					if (!cursor.isNull(COLUMN_INDEX_RETIRE_DATE)) {
						((TextView) view).setVisibility(View.VISIBLE);
						((TextView) view).setText(getResources().getString(R.string.retired_shoe_display_pattern,
								DateHelper.getDisplayDate(getApplicationContext(), cursor.getLong(COLUMN_INDEX_RETIRE_DATE))));
					} else
						((TextView) view).setVisibility(View.GONE);
					return true;

				}
				return false;
			}
		});

		setListAdapter(adapter);
		getListView().setOnCreateContextMenuListener(this);
	}

	@Override
	public Cursor runQuery(CharSequence filter) {

		return managedQuery(getIntent().getData(), PROJECTION, filter instanceof String ? (String) filter : null, null, 
				String.format( "CASE WHEN %s IS NULL THEN 0 ELSE 1 END, %s", ShoeAggColumns.RETIRED_DATE, ShoeAggColumns.TOTAL_DISTANCE));
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String key) {

		if (key.equals(preferenceKeyShowRetired)) {
			((SimpleCursorAdapter) getListView().getAdapter()).getFilter().filter(getQuerySetlection());
		}
	}

	private String getQuerySetlection() {
		return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(preferenceKeyShowRetired, false) ? null : String.format("%s IS NULL",
				ShoeAggColumns.RETIRED_DATE);
	}

	private void populate() {
		Cursor cursor = ((SimpleCursorAdapter) getListAdapter()).getCursor();
		if (cursor == null)
			return;
		cursor.requery();
		if (!cursor.moveToFirst())
			return;
		lastShoeDisplay.setText(cursor.getString(COLUMN_INDEX_NAME));
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.statuslist_options, menu);
		return true;
	}
	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {
		menu.findItem(R.id.delete_retired).setVisible(getRetiredCount() > 0);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_shoe:
			startActivity(new Intent(Intent.ACTION_INSERT, ShoeColumns.CONTENT_URI));
			return true;
		case R.id.show_entries:
			startActivity(new Intent(Intent.ACTION_VIEW, EntryXRefColumns.CONTENT_URI));
			return true;
		case R.id.settings:
			startActivity(new Intent(getBaseContext(), ShoeTrackerPreferenceActivity.class));
			return true;
		case R.id.delete_retired:
			deleteRetired();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// handle list menu items
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

		Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
		if (cursor == null) {
			return;
		}

		getMenuInflater().inflate(R.menu.statuslist_context, menu);
		menu.setHeaderTitle(cursor.getString(COLUMN_INDEX_NAME));
		if (!cursor.isNull(COLUMN_INDEX_RETIRE_DATE)) {
			menu.removeItem(R.id.context_retire_shoe);
			menu.removeItem(R.id.context_add_entry);
		} else {
			menu.removeItem(R.id.context_unretire_shoe);
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.context_add_entry:
			ActivityHelper.addEntryOnShoe(this, info.id);
			return true;
		case R.id.context_open_shoe:
			ActivityHelper.editShoe(this, info.id);
			return true;
		case R.id.context_retire_shoe:
			Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
			getRetireShoeDialog().show(info.id, cursor.getString(COLUMN_INDEX_NAME));
			return true;
		case R.id.context_delete_shoe:
			deleteItem(info.id, (Cursor) getListAdapter().getItem(info.position));
			return true;
		case R.id.context_unretire_shoe:
			new ShoeQuery(this).unretireShoe(info.id);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Cursor cursor = (Cursor) getListAdapter().getItem(position);
		if (cursor == null) {
			return;
		}
		if (!cursor.isNull(COLUMN_INDEX_RETIRE_DATE)) {
			String shoeName = cursor.getString(COLUMN_INDEX_NAME);
			Dialog dialog = new AlertDialog.Builder(this).setTitle(shoeName)
				.setMessage(getResources().getString(R.string.message_shoe_retired, shoeName, DateHelper.getDisplayDate(this, cursor.getLong(COLUMN_INDEX_RETIRE_DATE))))
				.setNeutralButton(R.string.alert_dialog_ok, null).create();
			dialog.show();
		} else
			ActivityHelper.addEntryOnShoe(this, id);
	}

	private void deleteItem(long id, Cursor cursor) {
		getConfirmDeleteDialog().show(id, cursor.getString(COLUMN_INDEX_NAME), cursor.getInt(COLUMN_INDEX_ACTIVITY_COUNT));
	}

	private void deleteItemNoConfirm(long id) {
		getContentResolver().delete(ContentUris.withAppendedId(ShoeColumns.CONTENT_URI, id), null, null);
	}

	private static final String RETIRED_SELECTION_STRING = ShoeAggColumns.RETIRED_DATE + " IS NOT NULL";

	
	private long getRetiredCount() {
		Cursor c = getContentResolver().query(ShoeColumns.CONTENT_URI, new String[] { ShoeAggColumns._ID }, RETIRED_SELECTION_STRING, null, null);
		long count = c.getCount();
		c.close();
		return count;
	}
	
	private void deleteRetired() {
		getDeleteRetiredDialog().show(getRetiredCount());
	}

	private void deleteRetiredNoConfirm() {
		getContentResolver().delete(ShoeColumns.CONTENT_URI, RETIRED_SELECTION_STRING, null);
	}

	private RetireShoeDialog getRetireShoeDialog() {
		return retireShoeDialog == null ? (retireShoeDialog = new RetireShoeDialog(this)) : retireShoeDialog;
	}

	private ConfirmDeleteDialog getConfirmDeleteDialog() {
		return confirmDeleteDialog == null ? (confirmDeleteDialog = new ConfirmDeleteDialog(this)) : confirmDeleteDialog;
	}

	private DeleteRetiredDialog getDeleteRetiredDialog() {
		return deleteRetiredDialog == null ? (deleteRetiredDialog = new DeleteRetiredDialog(this)) : deleteRetiredDialog;
	}

	private class DeleteRetiredDialog extends ActivityAlertDialog {
		public DeleteRetiredDialog(Activity activity) {
			super(activity);
		}

		public void show(long count) {

			new AlertDialog.Builder(getActivity()).setTitle(R.string.title_confirm_delete_retired)
					.setMessage(getResources().getString(R.string.message_confirm_delete_retired, count))
					.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							deleteRetiredNoConfirm();

						}
					}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						}
					}).create().show();
		}
	}

	private class RetireShoeDialog extends ActivityAlertDialog {

		private long activeId;
		private String activeName;

		public RetireShoeDialog(Activity activity) {
			super(activity);
		}

		public void show(long id, String name) {
			activeId = id;
			activeName = name;
			create().show();
		}

		private Dialog create() {

			return new AlertDialog.Builder(getActivity()).setTitle(R.string.title_retire_create_new_shoe)
					.setMessage(getActivity().getResources().getString(R.string.message_retire_create_new_shoe, activeName))
					.setPositiveButton(R.string.alert_dialog_yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							new ShoeQuery(getActivity()).retireShoe(activeId, true);
						}
					}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						}
					}).setNeutralButton(R.string.alert_dialog_no, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							new ShoeQuery(getActivity()).retireShoe(activeId, false);
						}
					}).create();
		}
	}

	private class ConfirmDeleteDialog extends ActivityAlertDialog {
		private long activeId;
		private String activeName;
		private int activeCount;

		public ConfirmDeleteDialog(Activity activity) {
			super(activity);
		}

		public void show(long id, String name, int count) {
			activeId = id;
			activeName = name;
			activeCount = count;
			create().show();
		}

		private Dialog create() {

			return new AlertDialog.Builder(getActivity())
					.setTitle(R.string.title_confirm_delete_shoe)
					.setMessage(
							activeCount > 1 ? getResources().getString(R.string.message_confirm_delete_shoe_many_activities, activeName, activeCount)
									: activeCount == 1 ? getResources().getString(R.string.message_confirm_delete_shoe_one_activity, activeName)
											: getResources().getString(R.string.message_confirm_delete_shoe, activeName))
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

}