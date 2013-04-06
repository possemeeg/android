package pmg.android.runningcalculator.data.db;

import android.net.Uri;
import android.provider.BaseColumns;

public final class RunningEvent {
	public static final String AUTHORITY = "pmg.android.runningcalculator.db.RunningEvent";
	
	private RunningEvent() {}

	public static class RacesColumns implements BaseColumns {
		private RacesColumns() {}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/races");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/pmg.android.runningcalculator.race";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/pmg.android.runningcalculator.race";
		
		public static final String CREATED_DATE = "created";
        public static final String MODIFIED_DATE = "modified";
        public static final String NAME = "name";
        public static final String DISTANCE = "distance";
        public static final String SHOW = "show";
        public static final String BUILT_IN = "built_in";
	}

}
