package pmg.android.trackrecorder;

import pmg.android.trackrecorder.data.Tracks.TrackTable;
import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class TracksListActivity extends ListActivity implements LoaderCallbacks<Cursor> {

	private static final String[] PROJECTION = new String[] { TrackTable._ID, TrackTable.NAME, TrackTable.CREATED_DATE};
	private SimpleCursorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		adapter = new SimpleCursorAdapter(this, R.layout.track_item, null, new String[] { TrackTable._ID }, 
				new int[] { R.id.track_id }, 0);
		setListAdapter(adapter);

		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(this, getIntent().getData(), PROJECTION, null, null, null);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
		
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);				
	}
}