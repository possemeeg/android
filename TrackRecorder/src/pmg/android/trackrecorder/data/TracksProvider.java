package pmg.android.trackrecorder.data;

import java.util.HashMap;
import java.util.Map;

import pmg.android.trackrecorder.data.Tracks.JobTable;
import pmg.android.trackrecorder.data.Tracks.TrackPointTable;
import pmg.android.trackrecorder.data.Tracks.TrackTable;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class TracksProvider extends ContentProvider {
	private static final String DATABASE_NAME = "tracks";
	private static final int DATABASE_VERSION = 1;

	private static final int TRACK = 1;
	private static final int TRACK_ID = 2;
	private static final int TRACK_POINT = 3;
	private static final int TRACK_POINT_ID = 4;
	private static final int JOB = 5;
	private static final int JOB_ID = 6;

	private static final UriMatcher uriMatcher;
	private static HashMap<String, String> trackProjectionMap;
	private static HashMap<String, String> trackPointProjectionMap;
	private static HashMap<String, String> jobProjectionMap;

	private DatabaseHelper openHelper;
	private final HashMap<String, SQLiteQueryBuilder> queryBuilders = new HashMap<String, SQLiteQueryBuilder>();

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
			if (db.isReadOnly())
				return;
			db.execSQL("PRAGMA foreign_keys=ON;");
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// @formatter:off

			db.execSQL(String.format("CREATE TABLE %s (" + 
					"%s INTEGER PRIMARY KEY," + 
					"%s TEXT," + 
					"%s INTEGER NOT NULL," + 
					"%s INTEGER);", 
					TrackTable.TABLE_NAME, 
					TrackTable._ID,
					TrackTable.NAME, 
					TrackTable.CREATED_DATE, 
					TrackTable.MODIFIED_DATE));

			db.execSQL(String.format("CREATE TABLE %s (" + // TABLE_NAME 
					"%s INTEGER PRIMARY KEY," + // _ID 
					"%s INTEGER NOT NULL," + // TRACK_ID 
					"%s INTEGER NOT NULL," + // TIMESTAMP
					"%s INTEGER NOT NULL," + // LATITUDE
					"%s INTEGER NOT NULL," + // LONGITUDE
					"%s INTEGER NOT NULL," + // ALTITUDE
					"FOREIGN KEY(%s) REFERENCES %s(%s) ON DELETE CASCADE);", 
					TrackPointTable.TABLE_NAME, 
					TrackPointTable._ID, 
					TrackPointTable.TRACK_ID,
					TrackPointTable.TIMESTAMP, 
					TrackPointTable.LATITUDE, 
					TrackPointTable.LONGITUDE, 
					TrackPointTable.ALTITUDE, 
					TrackPointTable.TRACK_ID, TrackTable.TABLE_NAME, TrackTable._ID));

			db.execSQL(String.format("CREATE TABLE %s (" + // TABLE_NAME 
					"%s INTEGER PRIMARY KEY," + // _ID 
					"%s INTEGER NOT NULL," + // TRACK_ID 
					"FOREIGN KEY(%s) REFERENCES %s(%s) ON DELETE CASCADE);",
					JobTable.TABLE_NAME,
					JobTable._ID, 
					JobTable.TRACK_ID, 
					JobTable.TRACK_ID, TrackTable.TABLE_NAME, TrackTable._ID));

			// @formatter:on
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
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case TRACK:
			return TrackTable.CONTENT_TYPE;
		case TRACK_ID:
			return TrackTable.CONTENT_ITEM_TYPE;
		case TRACK_POINT:
			return TrackPointTable.CONTENT_TYPE;
		case TRACK_POINT_ID:
			return TrackPointTable.CONTENT_ITEM_TYPE;
		case JOB:
			return JobTable.CONTENT_TYPE;
		case JOB_ID:
			return JobTable.CONTENT_ITEM_TYPE;
		}
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		switch (uriMatcher.match(uri)) {
		case TRACK:
			return queryTrack(projection, selection, selectionArgs, sortOrder);
		case TRACK_POINT:
			return queryTrackPoint(projection, selection, selectionArgs, sortOrder);
		case JOB:
			return queryJob(projection, selection, selectionArgs, sortOrder);
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (uriMatcher.match(uri)) {
		case TRACK:
			return insertTrack(values);
		case TRACK_POINT:
			return insertTrackPoint(values);
		case JOB:
			return insertJob(values);
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
		case TRACK:
			return deleteTrack(selection, selectionArgs);
		case TRACK_POINT:
			return deleteTrackPoint(selection, selectionArgs);
		case JOB:
			return deleteJob(selection, selectionArgs);
		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	private Cursor queryTrack(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		return db.query(TrackTable.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
	}

	private Cursor queryTrackPoint(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	private Cursor queryJob(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		return db.query(JobTable.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
	}

	private Uri insertJob(ContentValues values) {
		SQLiteDatabase db = openHelper.getWritableDatabase();

		Cursor cur = db.query(JobTable.TABLE_NAME, new String[] { JobTable.TRACK_ID }, null, null, null, null, null);
		int count = cur.getCount();
		cur.close();
		if (count > 0)
			throw new SQLException(String.format("Could not inset job. The table %s is not empty. Only one job can exist at a time.", JobTable.TABLE_NAME));

		long rowId = db.insert(JobTable.TABLE_NAME, null, values);

		return ContentUris.withAppendedId(JobTable.CONTENT_URI, rowId);
	}

	private Uri insertTrackPoint(ContentValues values) {
		if (values.containsKey(TrackPointTable.TIMESTAMP) == false) {
			values.put(TrackPointTable.TIMESTAMP, Long.valueOf(System.currentTimeMillis()));
		}

		SQLiteDatabase db = openHelper.getWritableDatabase();
		long rowId = db.insert(TrackPointTable.TABLE_NAME, null, values);
		return ContentUris.withAppendedId(TrackPointTable.CONTENT_URI, rowId);
	}

	private Uri insertTrack(ContentValues values) {
		Long now = Long.valueOf(System.currentTimeMillis());

		if (values.containsKey(TrackTable.CREATED_DATE) == false) {
			values.put(TrackTable.CREATED_DATE, now);
		}

		if (values.containsKey(TrackTable.MODIFIED_DATE) == false) {
			values.put(TrackTable.MODIFIED_DATE, now);
		}

		SQLiteDatabase db = openHelper.getWritableDatabase();
		long rowId = db.insert(TrackTable.TABLE_NAME, null, values);
		return ContentUris.withAppendedId(TrackTable.CONTENT_URI, rowId);
	}

	private int deleteTrack(String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int deleteTrackPoint(String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int deleteJob(String selection, String[] selectionArgs) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.delete(JobTable.TABLE_NAME, selection, selectionArgs);
		return 0;
	}

	private SQLiteQueryBuilder getTableQueryBuilder(String table) {
		SQLiteQueryBuilder queryBuilder = queryBuilders.get(table);
		if (queryBuilder != null)
			return queryBuilder;

		if (table.equals(TrackTable.TABLE_NAME))
			return createTableQueryBuilder(TrackTable.TABLE_NAME, trackProjectionMap);
		if (table.equals(TrackPointTable.TABLE_NAME))
			return createTableQueryBuilder(TrackPointTable.TABLE_NAME, trackPointProjectionMap);
		if (table.equals(JobTable.TABLE_NAME))
			return createTableQueryBuilder(JobTable.TABLE_NAME, jobProjectionMap);

		return null;

	}

	private SQLiteQueryBuilder createTableQueryBuilder(String table, Map<String, String> jobProjectionMap) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(table);
		qb.setProjectionMap(jobProjectionMap);
		queryBuilders.put(table, qb);
		return qb;

	}

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Tracks.AUTHORITY, TrackTable.TABLE_NAME, TRACK);
		uriMatcher.addURI(Tracks.AUTHORITY, TrackTable.TABLE_NAME + "/#", TRACK_ID);
		uriMatcher.addURI(Tracks.AUTHORITY, TrackPointTable.TABLE_NAME, TRACK_POINT);
		uriMatcher.addURI(Tracks.AUTHORITY, TrackPointTable.TABLE_NAME + "/#", TRACK_POINT_ID);
		uriMatcher.addURI(Tracks.AUTHORITY, JobTable.TABLE_NAME, JOB);
		uriMatcher.addURI(Tracks.AUTHORITY, JobTable.TABLE_NAME + "/#", JOB_ID);

		trackProjectionMap = new HashMap<String, String>();
		trackProjectionMap.put(TrackTable._ID, TrackTable._ID);
		trackProjectionMap.put(TrackTable.NAME, TrackTable.NAME);
		trackProjectionMap.put(TrackTable.CREATED_DATE, TrackTable.CREATED_DATE);
		trackProjectionMap.put(TrackTable.MODIFIED_DATE, TrackTable.MODIFIED_DATE);

		trackPointProjectionMap = new HashMap<String, String>();
		trackPointProjectionMap.put(TrackPointTable._ID, TrackPointTable._ID);
		trackPointProjectionMap.put(TrackPointTable.TRACK_ID, TrackPointTable.TRACK_ID);
		trackPointProjectionMap.put(TrackPointTable.TIMESTAMP, TrackPointTable.TIMESTAMP);
		trackPointProjectionMap.put(TrackPointTable.LATITUDE, TrackPointTable.LATITUDE);
		trackPointProjectionMap.put(TrackPointTable.LONGITUDE, TrackPointTable.LONGITUDE);
		trackPointProjectionMap.put(TrackPointTable.ALTITUDE, TrackPointTable.ALTITUDE);

		jobProjectionMap = new HashMap<String, String>();
		jobProjectionMap.put(JobTable._ID, JobTable._ID);
		jobProjectionMap.put(JobTable.TRACK_ID, JobTable.TRACK_ID);
	}

}
