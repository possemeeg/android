package pmg.android.showmileagetracker.db;

import android.content.Context;
import android.database.Cursor;
import pmg.android.showmileagetracker.db.Shoes.ShoeAggColumns;

public class ShoeAggQuery {
	private Context context;

	private static final String[] PROJECTION = new String[] { ShoeAggColumns._ID, // 0
			ShoeAggColumns.NAME, // 1
			ShoeAggColumns.COLOUR, // 2
			ShoeAggColumns.TOTAL_DISTANCE, // 3
			ShoeAggColumns.RETIRED_DATE //4 
			};

	private static int COLUMN_INDEX_ID = 0;
	private static int COLUMN_INDEX_NAME = 1;
	private static int COLUMN_INDEX_COLOUR = 2;
	private static int COLUMN_INDEX_TOTAL_DISTANCE = 3;
	private static int COLUMN_INDEX_RETIRED_DATE = 4;

	public ShoeAggQuery(Context context) {
		this.context = context;
	}
	
	public class ShoeDetails {
		public final long id;
		public final String name;
		public final int colour;
		public final int totalDistance;
		public final long retiredDate;
		
		private ShoeDetails(long id, String name, int colour, int totalDistance, long retiredDate){
			this.id = id;
			this.name = name;
			this.colour = colour;
			this.totalDistance = totalDistance;
			this.retiredDate = retiredDate;
		}
	}
	
	public ShoeDetails getNextShoe() {
		
		Cursor cursor = context.getContentResolver().query(ShoeAggColumns.CONTENT_URI, PROJECTION,
				String.format("%s IS NULL", ShoeAggColumns.RETIRED_DATE), null, ShoeAggColumns.TOTAL_DISTANCE);

		try {
			if (!cursor.moveToFirst())
				return null;

			return new ShoeDetails(cursor.getLong(COLUMN_INDEX_ID), cursor.getString(COLUMN_INDEX_NAME),
					cursor.getInt(COLUMN_INDEX_COLOUR),cursor.getInt(COLUMN_INDEX_TOTAL_DISTANCE), cursor.getLong(COLUMN_INDEX_RETIRED_DATE)); 
		}
		finally {
			cursor.close();
		}
	}
	
}
