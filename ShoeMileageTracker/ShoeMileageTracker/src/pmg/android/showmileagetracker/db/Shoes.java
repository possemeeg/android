package pmg.android.showmileagetracker.db;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Shoes {
	public static final String AUTHORITY = "pmg.android.showmileagetracker.db.Shoes";
	
	private Shoes() {}

	public static interface CommonColumns extends BaseColumns {
        public static final String CREATED_DATE = "created";
        public static final String MODIFIED_DATE = "modified";
	}
	
	
    public static class ShoeColumns implements CommonColumns {
        // This class cannot be instantiated
        private ShoeColumns() {}

		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/shoes");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/shoe";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/shoe";

        public static final String NAME = "name";
        public static final String INITIAL_DISTANCE = "initial_distance";
		public static final String MAX_DISTANCE = "max_distance";
		public static final String COLOUR = "colour";
		public static final String RETIRED_DATE = "retired_date";

    }
    
    public static class EntryColumns implements CommonColumns {
        // This class cannot be instantiated
        private EntryColumns() {}

		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/entries");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/entry";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/entry";

        public static final String SHOE = "shoe";
        public static final String DATE = "date";
        public static final String DISTANCE = "distance";
    }

    public static final class ShoeAggColumns extends ShoeColumns {
    	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/shoes_agg");
    	
    	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/shoe_agg";
    	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/shoe_agg";
    	
    	public static final String TOTAL_DISTANCE = "total_distance";
    	public static final String LAST_DATE = "last_date";
    	public static final String ACTIVITY_COUNT = "activity_count";

    }
    public static final class EntryXRefColumns extends EntryColumns
    {
    	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/entries_xref");

    	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/entry_xref";
    	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/entry_xref";

		public static final String SHOE_NAME = "shoe_name";
		public static final String SHOE_COLOUR = "shoe_colour";
		public static final String SHOE_RETIRED_DATE = "shoe_retired_date";
    	
    }
}
