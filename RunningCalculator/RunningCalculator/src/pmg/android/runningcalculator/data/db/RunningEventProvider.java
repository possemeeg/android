package pmg.android.runningcalculator.data.db;

import java.util.HashMap;

import pmg.android.runningcalculator.R;
import pmg.android.runningcalculator.data.DistanceConverter;
import pmg.android.runningcalculator.data.RunningConstants;
import pmg.android.runningcalculator.data.db.RunningEvent.RacesColumns;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

public class RunningEventProvider extends ContentProvider {

	private static final String DATABASE_NAME = "running.event";
	private static final int DATABASE_VERSION = 1;
	private static final String RACES_TABLE_NAME = "races";

	private static final int RACES = 1;
	private static final int RACE_ID = 2;

	private static final UriMatcher uriMatcher;
	private static HashMap<String, String> racesProjectionMap;

	private DatabaseHelper openHelper;

	private static class DatabaseHelper extends SQLiteOpenHelper {
		private final Context context;

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(String.format("CREATE TABLE %s (" + "%s INTEGER PRIMARY KEY," + "%s TEXT," + "%s INTEGER NOT NULL," + "%s INTEGER NOT NULL," + "%s INTEGER NOT NULL," + "%s INTEGER NOT NULL,"
					+ "%s INTEGER);", RACES_TABLE_NAME, RacesColumns._ID, RacesColumns.NAME, RacesColumns.DISTANCE, RacesColumns.SHOW, RacesColumns.BUILT_IN, RacesColumns.CREATED_DATE,
					RacesColumns.MODIFIED_DATE));

			loadRaces(db);
		}
		
		private void loadRaces(SQLiteDatabase db){
			long now = Long.valueOf(System.currentTimeMillis());
			addRace(db, 200, now);
			addRace(db, 400, now);
			addRace(db, 800, now);
			addRace(db, 1000, now);
			addRace(db, 1500, now);
			addRace(db, (int) DistanceConverter.getMilesInMetres(1), now);
			addRace(db, 2000, now);
			addRace(db, 3000, now);
			addRace(db, (int) DistanceConverter.getMilesInMetres(2), now);
			addRace(db, (int) DistanceConverter.getMilesInMetres(3), now);
			addRace(db, 5000, now);
			addRace(db, 8000, now);
			addRace(db, (int) DistanceConverter.getMilesInMetres(5), now);
			addRace(db, 10000, now);
			addRace(db, 12000, now);
			addRace(db, (int) DistanceConverter.getMilesInMetres(8), now);
			addRace(db, 15000, now);
			addRace(db, (int) DistanceConverter.getMilesInMetres(10), now);
			addRace(db, 20000, now);
			addRace(db, (int) RunningConstants.HALF_MARATHON, now, context.getString(R.string.half_marathon_name));
			addRace(db, (int) RunningConstants.MARATHON_DIST, now, context.getString(R.string.marathon_name));
		}

		private void addRace(SQLiteDatabase db, int distance, long now) {
			addRace(db, distance, now, null);
		}

		private void addRace(SQLiteDatabase db, int distance, long now, String name) {
			ContentValues values = new ContentValues();
			values.put(RacesColumns.NAME, name);
			values.put(RacesColumns.DISTANCE, distance);
			values.put(RacesColumns.SHOW, 1);
			values.put(RacesColumns.BUILT_IN, 1);
			values.put(RacesColumns.CREATED_DATE, now);
			db.insert(RACES_TABLE_NAME, null, values);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}

	@Override
	public boolean onCreate() {
		openHelper = new DatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		switch (uriMatcher.match(uri)) {
		case RACES:
			SQLiteDatabase db = openHelper.getReadableDatabase();
			Cursor c = getRacesQueryBuilder().query(db, projection, selection, selectionArgs, null, null, sortOrder);
			c.setNotificationUri(getContext().getContentResolver(), uri);
			return c;
		}
		return null;
	}

	private SQLiteQueryBuilder getRacesQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(RACES_TABLE_NAME);
		qb.setProjectionMap(racesProjectionMap);
		return qb;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case RACES:
			return RacesColumns.CONTENT_TYPE;
		case RACE_ID:
			return RacesColumns.CONTENT_ITEM_TYPE;
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (uriMatcher.match(uri) != RACES)
			return null;

		SQLiteDatabase db = openHelper.getWritableDatabase();
		if (!values.containsKey(RacesColumns.CREATED_DATE))
			values.put(RacesColumns.CREATED_DATE, Long.valueOf(System.currentTimeMillis()));
		if (!values.containsKey(RacesColumns.SHOW))
			values.put(RacesColumns.SHOW, 1);
		values.put(RacesColumns.BUILT_IN, 0);
		long rowId = db.insert(RACES_TABLE_NAME, null, values);
		if(rowId > 0){
			getContext().getContentResolver().notifyChange(uri, null);
			return ContentUris.withAppendedId(RacesColumns.CONTENT_URI, rowId);
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int count = 0;
		switch (uriMatcher.match(uri)) {
		case RACES:
			count = db.delete(RACES_TABLE_NAME, selection, selectionArgs);
			break;
		case RACE_ID:
			count = db.delete(RACES_TABLE_NAME, BaseColumns._ID + "=" + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
			break;
		}
		if(count > 0)
			getContext().getContentResolver().notifyChange(uri, null);
		
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(RunningEvent.AUTHORITY, "races", RACES);
		uriMatcher.addURI(RunningEvent.AUTHORITY, "races" + "/#", RACE_ID);

		racesProjectionMap = new HashMap<String, String>();
		racesProjectionMap.put(RacesColumns._ID, RacesColumns._ID);
		racesProjectionMap.put(RacesColumns.NAME, RacesColumns.NAME);
		racesProjectionMap.put(RacesColumns.DISTANCE, RacesColumns.DISTANCE);
		racesProjectionMap.put(RacesColumns.SHOW, RacesColumns.SHOW);
		racesProjectionMap.put(RacesColumns.CREATED_DATE, RacesColumns.CREATED_DATE);
		racesProjectionMap.put(RacesColumns.MODIFIED_DATE, RacesColumns.MODIFIED_DATE);
	}

}
