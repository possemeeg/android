package com.possemeeg.android.programabletimer.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TimersOpenHelper extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 1;
	
	private static final String TIMERS_TABLE_NAME = "timer";

	public static class TimersColumns {
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String DURATION = "duration";
		public static final String END_ACTION = "end_action";
	}
	
// @formatter:off
	private static final String DICTIONARY_TABLE_CREATE = "CREATE TABLE "
			+ TIMERS_TABLE_NAME + 
			" (" 
			+ TimersColumns.ID + " ID, "
			+ TimersColumns.NAME + " TEXT, "
			+ TimersColumns.DURATION + " INT,"
			+ TimersColumns.END_ACTION + " INT;"
			+ ");";
// @formatter:on

	TimersOpenHelper(Context context, final String databaseName) {
		super(context, databaseName, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DICTIONARY_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}
