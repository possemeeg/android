package pmg.android.showmileagetracker.util;

import pmg.android.showmileagetracker.R;
import pmg.android.showmileagetracker.db.Shoes.ShoeColumns;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ColourHelper {
	
	public ColourHelper(Context context)
	{
		this.context = context;
	}

	private static int mixPart(int p1, int p2, int weigtingOn2)
	{
		if(weigtingOn2 <= 0)
			weigtingOn2 = 1;
		return (p1+p2*weigtingOn2)/(1+1*weigtingOn2);
	}
	private static int mixWhitePart(int p1)
	{
		return mixPart(p1,0xFF,4);
	}
	public static int setLighterOfColour(int colour) {
		return Color.rgb(mixWhitePart(Color.red(colour)), mixWhitePart(Color.green(colour)), mixWhitePart(Color.blue(colour)));
	}

	public String getColourName(int colour) {
		String ret = getColourMap().get(colour);
		return ret != null ? ret : "";
	}

	private final Context context;
	private Map<Integer, String> colourMap;
	private int[] colourList; // ordered
	private Map<Integer, String> getColourMap()
	{
		if(colourMap != null)
			return colourMap; 

		colourMap = new HashMap<Integer, String>();

		Resources resources = context.getResources();
		String[] colours = resources.getStringArray(R.array.colours);
		String[] colour_names = resources.getStringArray(R.array.colour_names);
		colourList = new int [colours.length];

		for (int i = 0, imax = Math.min(colours.length, colour_names.length); i < imax; ++i) {
			try {
				int colour = Color.class.getField(colours[i]).getInt(null);
				colourList[i] = colour;
				colourMap.put(colour, colour_names[i]);
			} catch (Exception e) {
			}
		}
		return colourMap;
	}
	
	private int[] getColourList()
	{
		if(colourList== null)
			getColourMap();
		return colourList;
	}
	
	public int getNextFreeColour()
	{
		HashSet<Integer> existingColours = getUsedColours();
		int [] colourList = getColourList();
		for(int i = 0; i < getColourList().length; ++i)
		{
			if(!existingColours.contains(colourList[i]))
				return colourList[i];
		}
		return colourList[0];
	}
	
	private HashSet<Integer> getUsedColours()
	{
		Cursor cursor = context.getContentResolver().query(ShoeColumns.CONTENT_URI, new String[] { ShoeColumns.COLOUR }, null, null, null);
		HashSet<Integer> colours = new HashSet<Integer>();
		if(cursor == null || !cursor.moveToFirst())
			return colours;
		do
		{
			colours.add(cursor.getInt(0));
		}
		while(cursor.moveToNext());
		return colours;
	}

}
