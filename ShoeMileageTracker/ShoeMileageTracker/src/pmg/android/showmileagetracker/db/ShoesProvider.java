package pmg.android.showmileagetracker.db;

import java.util.HashMap;

import pmg.android.showmileagetracker.db.Shoes.CommonColumns;
import pmg.android.showmileagetracker.db.Shoes.EntryColumns;
import pmg.android.showmileagetracker.db.Shoes.EntryXRefColumns;
import pmg.android.showmileagetracker.db.Shoes.ShoeAggColumns;
import pmg.android.showmileagetracker.db.Shoes.ShoeColumns;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

public class ShoesProvider extends ContentProvider {
	private static final String TAG = ShoesProvider.class.getName();

	public static final String UPDATE_BROADCAST_ACTION = "pmg.android.shoemileagetracker.DB_UPDATE"; 
	
	private static final String DATABASE_NAME = "shoes.db";
	private static final int DATABASE_VERSION = 1;
	private static final String SHOES_TABLE_NAME = "shoes";
	private static final String ENTRIES_TABLE_NAME = "entries";
	private static final String SHOES_AGG_VIEW_NAME = "shoes_agg_view";
	private static final String ENTRIES_XREF_VIEW_NAME = "entries_xref_view";

	private static HashMap<String, String> shoesProjectionMap;
	private static HashMap<String, String> entriesProjectionMap;
	private static HashMap<String, String> shoesAggProjectionMap;
	private static HashMap<String, String> entriesXRefProjectionMap;

	private static final int SHOES = 1;
	private static final int SHOE_ID = 2;
	private static final int ENTRIES = 3;
	private static final int ENTRY_ID = 4;
	private static final int SHOES_AGG = 5;
	private static final int SHOE_AGG_ID = 6;
	private static final int ENTRIES_XREF = 7;
	private static final int ENTRY_XREF_ID = 8;

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
			// shoes table
			db.execSQL(String.format("CREATE TABLE %s ("
					+ "%s INTEGER PRIMARY KEY," 
					+ "%s TEXT NOT NULL,"
					+ "%s INTEGER,"
					+ "%s INTEGER,"
					+ "%s INTEGER,"
					+ "%s INTEGER,"
					+ "%s INTEGER NOT NULL," 
					+ "%s INTEGER NOT NULL);",
					SHOES_TABLE_NAME, 
					ShoeColumns._ID, 
					ShoeColumns.NAME,
					ShoeColumns.MAX_DISTANCE, 
					ShoeColumns.INITIAL_DISTANCE, 
					ShoeColumns.COLOUR, 
					ShoeColumns.RETIRED_DATE, 
					ShoeColumns.CREATED_DATE, 
					ShoeColumns.MODIFIED_DATE));
			// @formatter:on
			// entries table
			// @formatter:off
			db.execSQL(String.format("CREATE TABLE %s ("
					+ "%s INTEGER PRIMARY KEY," 
					+ "%s INTEGER NOT NULL,"
					+ "%s INTEGER NOT NULL," 
					+ "%s INTEGER NOT NULL,"
					+ "%s INTEGER NOT NULL," 
					+ "%s INTEGER NOT NULL,"
					+ "FOREIGN KEY(%s) REFERENCES %s(%s) ON DELETE CASCADE);",
					ENTRIES_TABLE_NAME, 
					EntryColumns._ID, 
					EntryColumns.SHOE,
					EntryColumns.DATE, 
					EntryColumns.DISTANCE,
					EntryColumns.CREATED_DATE, 
					EntryColumns.MODIFIED_DATE,
					EntryColumns.SHOE, SHOES_TABLE_NAME, ShoeColumns._ID));
			// @formatter:on

			createViews(db);
		}

		private void createViews(SQLiteDatabase db) {
			db.execSQL(String.format("DROP VIEW IF EXISTS %s", SHOES_AGG_VIEW_NAME));

			db.execSQL(String.format("DROP VIEW IF EXISTS %s", ENTRIES_XREF_VIEW_NAME));

			// shoes aggregation view
			// @formatter:off
			db.execSQL(String.format("CREATE VIEW %s AS SELECT "
					+ "%s.%s AS %s," 
					+ "%s.%s AS %s,"
					+ "%s.%s AS %s,"
					+ "%s.%s AS %s,"
					+ "%s.%s AS %s,"
					+ "%s.%s AS %s,"
					+ "SUM(COALESCE(%s.%s,0)) + COALESCE(%s.%s,0) AS %s, "
					+ "MAX(%s.%s) AS %s, "
					+ "COUNT(%s.%s) AS %s " 
					+ "FROM %s LEFT JOIN %s ON %s.%s=%s.%s GROUP BY %s",
					SHOES_AGG_VIEW_NAME, 
					SHOES_TABLE_NAME, ShoeColumns._ID, ShoeAggColumns._ID, 
					SHOES_TABLE_NAME, ShoeColumns.NAME,ShoeAggColumns.NAME,
					SHOES_TABLE_NAME, ShoeColumns.INITIAL_DISTANCE,ShoeAggColumns.INITIAL_DISTANCE, 
					SHOES_TABLE_NAME, ShoeColumns.MAX_DISTANCE, ShoeAggColumns.MAX_DISTANCE, 
					SHOES_TABLE_NAME, ShoeColumns.COLOUR, ShoeAggColumns.COLOUR, 
					SHOES_TABLE_NAME, ShoeColumns.RETIRED_DATE, ShoeAggColumns.RETIRED_DATE, 
					ENTRIES_TABLE_NAME, EntryColumns.DISTANCE, SHOES_TABLE_NAME, ShoeColumns.INITIAL_DISTANCE, ShoeAggColumns.TOTAL_DISTANCE,
					ENTRIES_TABLE_NAME, EntryColumns.DATE, ShoeAggColumns.LAST_DATE,
					ENTRIES_TABLE_NAME, EntryColumns._ID, ShoeAggColumns.ACTIVITY_COUNT,
					SHOES_TABLE_NAME, ENTRIES_TABLE_NAME, SHOES_TABLE_NAME,
					ShoeColumns._ID, ENTRIES_TABLE_NAME, EntryColumns.SHOE,
					ShoeAggColumns._ID));
			// @formatter:on

			// @formatter:off
			db.execSQL(String.format("CREATE VIEW %s AS SELECT " +
					"%s.%s AS %s,"+
					"%s.%s AS %s,"+
					"%s.%s AS %s,"+
					"%s.%s AS %s,"+
					"%s.%s AS %s,"+
					"%s.%s AS %s,"+
					"%s.%s AS %s "+
					"FROM %s LEFT JOIN %s ON %s.%s=%s.%s",
					ENTRIES_XREF_VIEW_NAME,
					ENTRIES_TABLE_NAME, EntryColumns._ID, EntryXRefColumns._ID,
					ENTRIES_TABLE_NAME, EntryColumns.SHOE, EntryXRefColumns.SHOE,
					ENTRIES_TABLE_NAME, EntryColumns.DATE, EntryXRefColumns.DATE,
					ENTRIES_TABLE_NAME, EntryColumns.DISTANCE, EntryXRefColumns.DISTANCE,
					SHOES_TABLE_NAME, ShoeColumns.NAME, EntryXRefColumns.SHOE_NAME,
					SHOES_TABLE_NAME, ShoeColumns.COLOUR, EntryXRefColumns.SHOE_COLOUR,
					SHOES_TABLE_NAME, ShoeColumns.RETIRED_DATE, EntryXRefColumns.SHOE_RETIRED_DATE,
					ENTRIES_TABLE_NAME, SHOES_TABLE_NAME, 
					SHOES_TABLE_NAME, ShoeColumns._ID,
					ENTRIES_TABLE_NAME, EntryColumns.SHOE)			
			);
			// @formatter:on
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);

			createViews(db);
		}
	}

	private DatabaseHelper openHelper;
	private static final UriMatcher uriMatcher;

	@Override
	public boolean onCreate() {
		openHelper = new DatabaseHelper(getContext());
		return false;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int count;
		switch (uriMatcher.match(uri)) {
		case SHOES:
			count = db.delete(SHOES_TABLE_NAME, selection, selectionArgs);
			// db.e
			break;
		case SHOE_ID:
			count = deleteItem(uri, selection, selectionArgs, db, SHOES_TABLE_NAME);
			break;

		case ENTRY_ID:
			count = deleteItem(uri, selection, selectionArgs, db, ENTRIES_TABLE_NAME);
			break;

		case ENTRY_XREF_ID:
			count = deleteItem(uri, selection, selectionArgs, db, ENTRIES_TABLE_NAME);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		notifyChange(uri);
		return count;
	}

	private int deleteItem(Uri uri, String selection, String[] selectionArgs, SQLiteDatabase db, String table) {
		int count;
		String id = uri.getPathSegments().get(1);
		count = db.delete(table, BaseColumns._ID + "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case SHOES:
			return ShoeColumns.CONTENT_TYPE;

		case SHOE_ID:
			return ShoeColumns.CONTENT_ITEM_TYPE;

		case ENTRIES:
			return EntryColumns.CONTENT_TYPE;

		case ENTRY_ID:
			return EntryColumns.CONTENT_ITEM_TYPE;

		case SHOES_AGG:
			return ShoeColumns.CONTENT_TYPE;

		case SHOE_AGG_ID:
			return ShoeColumns.CONTENT_ITEM_TYPE;

		case ENTRIES_XREF:
			return EntryXRefColumns.CONTENT_TYPE;

		case ENTRY_XREF_ID:
			return EntryXRefColumns.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
		Long now = Long.valueOf(System.currentTimeMillis());

		if (values.containsKey(CommonColumns.CREATED_DATE) == false) {
			values.put(CommonColumns.CREATED_DATE, now);
		}

		if (values.containsKey(CommonColumns.MODIFIED_DATE) == false) {
			values.put(CommonColumns.MODIFIED_DATE, now);
		}

		int match = uriMatcher.match(uri);
		if (match == SHOES) {
			return insertShoe(uri, values);
		}

		if (match == ENTRIES) {
			return insertEntry(uri, values);
		}

		throw new IllegalArgumentException("Unknown URI " + uri);
	}

	private Uri insertShoe(Uri uri, ContentValues values) {
		// Make sure that the fields are all set
		SQLiteDatabase db = openHelper.getWritableDatabase();
		long rowId = db.insert(SHOES_TABLE_NAME, null, values);
		if (rowId > 0) {
			Uri insUri = ContentUris.withAppendedId(ShoeColumns.CONTENT_URI, rowId);
			notifyChange(insUri);
			return insUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	private Uri insertEntry(Uri uri, ContentValues values) {
		// Make sure that the fields are all set
		SQLiteDatabase db = openHelper.getWritableDatabase();
		long rowId = db.insert(ENTRIES_TABLE_NAME, null, values);
		if (rowId > 0) {
			Uri insUri = ContentUris.withAppendedId(ShoeColumns.CONTENT_URI, rowId);
			notifyChange(insUri);
			return insUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder qb = null;
		switch (uriMatcher.match(uri)) {
		case SHOES:
			return getCursor(uri, projection, selection, selectionArgs, sortOrder, getShoesQueryBuilder());

		case SHOE_ID:
			qb = getShoesQueryBuilder();
			qb.appendWhere(ShoeColumns._ID + "=" + uri.getPathSegments().get(1));
			return getCursor(uri, projection, selection, selectionArgs, sortOrder, qb);

		case ENTRIES:
			return getCursor(uri, projection, selection, selectionArgs, sortOrder, getEntriesQueryBuilder());

		case SHOES_AGG:
			Cursor cursor = getCursor(uri, projection, selection, selectionArgs, sortOrder, getShoesAggQueryBuilder());
			ContentResolver contentResolver = getContext().getContentResolver();
			cursor.setNotificationUri(contentResolver, EntryColumns.CONTENT_URI);
			cursor.setNotificationUri(contentResolver, ShoeColumns.CONTENT_URI);
			return cursor;

		case ENTRIES_XREF:
			Cursor xrefCursor = getCursor(uri, projection, selection, selectionArgs, sortOrder, getEntriesXRefQueryBuilder());
			xrefCursor.setNotificationUri(getContext().getContentResolver(), ShoeColumns.CONTENT_URI);
			xrefCursor.setNotificationUri(getContext().getContentResolver(), EntryColumns.CONTENT_URI);
			return xrefCursor;

		case ENTRY_XREF_ID:
			qb = getEntriesXRefQueryBuilder();
			qb.appendWhere(EntryXRefColumns._ID + "=" + uri.getPathSegments().get(1));
			Cursor xrefCursorId = getCursor(uri, projection, selection, selectionArgs, sortOrder, qb);
			xrefCursorId.setNotificationUri(getContext().getContentResolver(), ShoeColumns.CONTENT_URI);
			return xrefCursorId;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	private Cursor getCursor(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, SQLiteQueryBuilder qb) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	private SQLiteQueryBuilder getShoesQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(SHOES_TABLE_NAME);
		qb.setProjectionMap(shoesProjectionMap);
		return qb;
	}

	private SQLiteQueryBuilder getEntriesQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(ENTRIES_TABLE_NAME);
		qb.setProjectionMap(entriesProjectionMap);
		return qb;
	}

	private SQLiteQueryBuilder getShoesAggQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(SHOES_AGG_VIEW_NAME);
		qb.setProjectionMap(shoesAggProjectionMap);
		return qb;
	}

	private SQLiteQueryBuilder getEntriesXRefQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(ENTRIES_XREF_VIEW_NAME);
		qb.setProjectionMap(entriesXRefProjectionMap);
		return qb;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int count;
		switch (uriMatcher.match(uri)) {
		case SHOES:
			count = db.update(SHOES_TABLE_NAME, values, selection, selectionArgs);
			break;

		case SHOE_ID:
			count = updateItem(uri, values, selection, selectionArgs, db, SHOES_TABLE_NAME);
			break;
		case ENTRY_ID:
			count = updateItem(uri, values, selection, selectionArgs, db, ENTRIES_TABLE_NAME);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		notifyChange(uri);
		return count;
	}

	private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs, SQLiteDatabase db, String tableName) {
		int count;
		String id = uri.getPathSegments().get(1);
		count = db.update(tableName, values, BaseColumns._ID + "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
				selectionArgs);
		return count;
	}

	private void notifyChange(Uri uri) {
		Log.i(TAG,"sending update broadcast");
		Context context = getContext();
		context.getContentResolver().notifyChange(uri, null);
		context.getApplicationContext().sendBroadcast(new Intent(UPDATE_BROADCAST_ACTION));
	}

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Shoes.AUTHORITY, "shoes", SHOES);
		uriMatcher.addURI(Shoes.AUTHORITY, "shoes/#", SHOE_ID);
		uriMatcher.addURI(Shoes.AUTHORITY, "entries", ENTRIES);
		uriMatcher.addURI(Shoes.AUTHORITY, "entries/#", ENTRY_ID);
		uriMatcher.addURI(Shoes.AUTHORITY, "shoes_agg", SHOES_AGG);
		uriMatcher.addURI(Shoes.AUTHORITY, "shoes_agg/#", SHOE_AGG_ID);
		uriMatcher.addURI(Shoes.AUTHORITY, "entries_xref", ENTRIES_XREF);
		uriMatcher.addURI(Shoes.AUTHORITY, "entries_xref/#", ENTRY_XREF_ID);

		shoesProjectionMap = new HashMap<String, String>();
		shoesProjectionMap.put(ShoeColumns._ID, ShoeColumns._ID);
		shoesProjectionMap.put(ShoeColumns.NAME, ShoeColumns.NAME);
		shoesProjectionMap.put(ShoeColumns.MAX_DISTANCE, ShoeColumns.MAX_DISTANCE); // v2
		shoesProjectionMap.put(ShoeColumns.INITIAL_DISTANCE, ShoeColumns.INITIAL_DISTANCE); // v2
		shoesProjectionMap.put(ShoeColumns.COLOUR, ShoeColumns.COLOUR); // v2
		shoesProjectionMap.put(ShoeColumns.RETIRED_DATE, ShoeColumns.RETIRED_DATE); // v2
		shoesProjectionMap.put(ShoeColumns.CREATED_DATE, ShoeColumns.CREATED_DATE);
		shoesProjectionMap.put(ShoeColumns.MODIFIED_DATE, ShoeColumns.MODIFIED_DATE);

		entriesProjectionMap = new HashMap<String, String>();
		entriesProjectionMap.put(EntryColumns._ID, EntryColumns._ID);
		entriesProjectionMap.put(EntryColumns.SHOE, EntryColumns.SHOE);
		entriesProjectionMap.put(EntryColumns.DATE, EntryColumns.DATE);
		entriesProjectionMap.put(EntryColumns.DISTANCE, EntryColumns.DISTANCE);
		entriesProjectionMap.put(EntryColumns.CREATED_DATE, EntryColumns.CREATED_DATE);
		entriesProjectionMap.put(EntryColumns.MODIFIED_DATE, EntryColumns.MODIFIED_DATE);

		shoesAggProjectionMap = new HashMap<String, String>();
		shoesAggProjectionMap.put(ShoeAggColumns._ID, ShoeAggColumns._ID);
		shoesAggProjectionMap.put(ShoeAggColumns.NAME, ShoeAggColumns.NAME);
		shoesAggProjectionMap.put(ShoeAggColumns.MAX_DISTANCE, ShoeAggColumns.MAX_DISTANCE);
		shoesAggProjectionMap.put(ShoeAggColumns.INITIAL_DISTANCE, ShoeAggColumns.INITIAL_DISTANCE);
		shoesAggProjectionMap.put(ShoeAggColumns.COLOUR, ShoeAggColumns.COLOUR);
		shoesAggProjectionMap.put(ShoeAggColumns.TOTAL_DISTANCE, ShoeAggColumns.TOTAL_DISTANCE);
		shoesAggProjectionMap.put(ShoeAggColumns.LAST_DATE, ShoeAggColumns.LAST_DATE);
		shoesAggProjectionMap.put(ShoeAggColumns.ACTIVITY_COUNT, ShoeAggColumns.ACTIVITY_COUNT);
		shoesAggProjectionMap.put(ShoeAggColumns.RETIRED_DATE, ShoeAggColumns.RETIRED_DATE);

		entriesXRefProjectionMap = new HashMap<String, String>();
		entriesXRefProjectionMap.put(EntryXRefColumns._ID, EntryXRefColumns._ID);
		entriesXRefProjectionMap.put(EntryXRefColumns.SHOE, EntryXRefColumns.SHOE);
		entriesXRefProjectionMap.put(EntryXRefColumns.DATE, EntryXRefColumns.DATE);
		entriesXRefProjectionMap.put(EntryXRefColumns.DISTANCE, EntryXRefColumns.DISTANCE);
		entriesXRefProjectionMap.put(EntryXRefColumns.SHOE_NAME, EntryXRefColumns.SHOE_NAME);
		entriesXRefProjectionMap.put(EntryXRefColumns.SHOE_COLOUR, EntryXRefColumns.SHOE_COLOUR);
		entriesXRefProjectionMap.put(EntryXRefColumns.SHOE_RETIRED_DATE, EntryXRefColumns.SHOE_RETIRED_DATE);

	}
}
