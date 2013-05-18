package pmg.android.showmileagetracker.db;

import java.util.Calendar;

import pmg.android.showmileagetracker.db.Shoes.ShoeColumns;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ShoeQuery {
	public static final String[] projection = new String[] {
		ShoeColumns._ID, // 0
		ShoeColumns.NAME, // 1
		ShoeColumns.COLOUR, // 2
		ShoeColumns.INITIAL_DISTANCE, // 3
		ShoeColumns.MAX_DISTANCE
	};
	public static final int COLUMN_INDEX_NAME = 1;
	public static final int COLUMN_INDEX_COLOUR = 2;
	public static final int COLUMN_INDEX_INITIAL_DISTANCE = 3;
	public static final int COLUMN_INDEX_MAX_DISTANCE = 4;

	public ShoeQuery(Context context) {
		this.context = context;
		this.contentResolver = context.getContentResolver();
	}

	public Cursor queryShoe(long id) {
		Cursor cursor = contentResolver.query(ContentUris.withAppendedId(ShoeColumns.CONTENT_URI, id), projection, null, null, null);
		if(!cursor.moveToFirst())
			return null;
		
		return cursor;
	}

	public void duplicateShoe(long id){
		Cursor cursor = queryShoe(id);
		if(cursor == null)
			return;
		
		final ContentValues values = new ContentValues();
		values.put(ShoeColumns.NAME, cursor.getString(COLUMN_INDEX_NAME));
		values.put(ShoeColumns.COLOUR, cursor.getInt(COLUMN_INDEX_COLOUR));
		values.put(ShoeColumns.INITIAL_DISTANCE, cursor.getInt(COLUMN_INDEX_INITIAL_DISTANCE));
		values.put(ShoeColumns.MAX_DISTANCE, cursor.getInt(COLUMN_INDEX_MAX_DISTANCE));
		
		contentResolver.insert( ShoeColumns.CONTENT_URI, values);
	}

	public void retireShoe(long id, boolean createDuplicate) {
		final ContentValues values = new ContentValues();
		values.put(ShoeColumns.RETIRED_DATE, Calendar.getInstance().getTimeInMillis());
		updateShoe(id, values);
		if(createDuplicate)
			duplicateShoe(id);
	}
	public void unretireShoe(long id) {
		final ContentValues values = new ContentValues();
		values.putNull(ShoeColumns.RETIRED_DATE);
		updateShoe(id, values);
	}
	
	
	private final Context context;
	private final ContentResolver contentResolver;

	private void updateShoe(long id, final ContentValues values) {
		context.getContentResolver().update(ContentUris.withAppendedId(ShoeColumns.CONTENT_URI, id), values, null, null);
	}
}

