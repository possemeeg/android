package pmg.android.trackrecorder.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Tracks {

	public static final String AUTHORITY = "pmg.android.trackrecorder.data.Tracks";

	private Tracks() {
	}

	public static class TrackTable implements BaseColumns {
		private TrackTable() {
		}
		public static final String TABLE_NAME = "track";

		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/pmg.android.trackrecorder." + TABLE_NAME;
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/pmg.android.trackrecorder." + TABLE_NAME;

		public static final String NAME = "name";
		public static final String CREATED_DATE = "created";
		public static final String MODIFIED_DATE = "modified";
	}

	public static class TrackPointTable implements BaseColumns {
		private TrackPointTable() {
		}
		public static final String TABLE_NAME = "track_point";

		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/pmg.android.trackrecorder." + TABLE_NAME;
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/pmg.android.trackrecorder." + TABLE_NAME;

		public static final String TRACK_ID = "track_id";
		public static final String TIMESTAMP = "timestamp";
		public static final String LATITUDE = "latitude";
		public static final String LONGITUDE = "longitude";
		public static final String ALTITUDE = "altitude";

	}

	public static class JobTable implements BaseColumns {
		private JobTable() {
		}
		public static final String TABLE_NAME = "job";
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/pmg.android.trackrecorder." + TABLE_NAME;
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/pmg.android.trackrecorder." + TABLE_NAME;

		public static final String TRACK_ID = "track_id";
	}
}
