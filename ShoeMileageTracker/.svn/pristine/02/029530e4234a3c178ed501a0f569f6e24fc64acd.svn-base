package pmg.android.showmileagetracker.util;

import pmg.android.showmileagetracker.EditEntryActivity;
import pmg.android.showmileagetracker.db.ShoeQuery;
import pmg.android.showmileagetracker.db.Shoes.EntryColumns;
import pmg.android.showmileagetracker.db.Shoes.EntryXRefColumns;
import pmg.android.showmileagetracker.db.Shoes.ShoeColumns;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class ActivityHelper {
	private static final String TAG = EditEntryActivity.class.getName();

	public static void editShoe(Context context, long id)	{
		context.startActivity(new Intent(Intent.ACTION_EDIT, ContentUris.withAppendedId(ShoeColumns.CONTENT_URI, id)));
	}

	
	
	public static void addEntryOnShoe(Context context, long shoeId){
		Cursor cursor = new ShoeQuery(context).queryShoe(shoeId);
		
		context.startActivity(getAddEntryIntent(shoeId,cursor.getString(ShoeQuery.COLUMN_INDEX_NAME), cursor.getInt(ShoeQuery.COLUMN_INDEX_COLOUR)));
	}
	
	public static Intent getAddEntryIntent(long shoeId, String shoeName, int shoeColour	){
		Log.i(TAG,String.format("getAddEntryIntent %d, %s, %d", shoeId, shoeName, shoeColour));
		Intent entryIntent = new Intent(Intent.ACTION_INSERT, EntryXRefColumns.CONTENT_URI);
		
		entryIntent.putExtra(EntryColumns.SHOE, shoeId);
		entryIntent.putExtra(EntryXRefColumns.SHOE_NAME, shoeName);
		entryIntent.putExtra(EntryXRefColumns.SHOE_COLOUR, shoeColour);
		
		return entryIntent;
	}
}
