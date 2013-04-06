//package pmg.android.runningcalculator;
//
//import pmg.android.runningcalculator.data.db.RunningEvent;
//import pmg.android.runningcalculator.data.db.RunningEvent.RacesColumns;
//import pmg.android.runningcalculator.view.DisplayStringFormatter;
//import android.app.ListActivity;
//import android.app.LoaderManager.LoaderCallbacks;
//import android.content.CursorLoader;
//import android.content.Intent;
//import android.content.Loader;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.CheckBox;
//import android.widget.SimpleCursorAdapter;
//import android.widget.TextView;
//import android.widget.SimpleCursorAdapter.ViewBinder;
//
//public class RaceListActivity extends ListActivity implements LoaderCallbacks<Cursor> {
//
//	private static final String[] PROJECTION = new String[] { RacesColumns._ID, RacesColumns.NAME, RacesColumns.DISTANCE, RacesColumns.SHOW};
//	private SimpleCursorAdapter adapter;
//	private static final int COLUMN_INDEX_NAME= 1;
//	private static final int COLUMN_INDEX_DISTANCE= 2;
//	private static final int COLUMN_INDEX_SHOW = 3;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//
//		//Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null, null, null);
//		
//		adapter = new SimpleCursorAdapter(this, R.layout.race_item, null, new String[] { RacesColumns.NAME, RacesColumns.DISTANCE, RacesColumns.SHOW }, 
//				new int[] { R.id.name, R.id.distance, R.id.show }, 0 );
//
//		adapter.setViewBinder(new ViewBinder() {
//			
//			@Override
//			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//				switch(columnIndex){
//				case COLUMN_INDEX_NAME:
//					String name = cursor.getString(columnIndex);
//					if(name != null && !name.isEmpty())
//						((TextView) view).setText( name);
//					else
//						((TextView) view).setText("");
//					return true;
//				case COLUMN_INDEX_DISTANCE:
//					String namex = cursor.getString(COLUMN_INDEX_NAME);
//					if(namex == null || namex.isEmpty())
//						((TextView) view).setText( DisplayStringFormatter.getFormattedDistance(getResources(),cursor.getInt(columnIndex)));
//					else
//						((TextView) view).setText("");
//					return true;
//				case COLUMN_INDEX_SHOW:
//					((CheckBox)view).setChecked(cursor.getInt(columnIndex) != 0);
//					return true;
//				}
//				return false;
//			}
//		});
//		
//		
//		setListAdapter(adapter);
//		
//		getLoaderManager().initLoader(0, null, this);
//
//		getListView().setOnCreateContextMenuListener(this);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.race_options, menu);
//		return true;
//	}
//	@Override
//	public boolean onPrepareOptionsMenu (Menu menu) {
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.add_race:
//			startActivity(new Intent(Intent.ACTION_INSERT, RunningEvent.RacesColumns.CONTENT_URI));
//			return true;
//		case R.id.reset_races:
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
//
//	// Overrides for LoaderCallbacks<Cursor>
//	
//	@Override
//	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//		return new CursorLoader(this, getIntent().getData(), PROJECTION, null, null, null);
//	}
//	
//	@Override
//	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//		adapter.swapCursor(data);
//		
//	}
//	
//	@Override
//	public void onLoaderReset(Loader<Cursor> loader) {
//		adapter.swapCursor(null);				
//	}
//	
//}