package pmg.android.showmileagetracker.widget;

import pmg.android.showmileagetracker.EditEntryActivity;
import pmg.android.showmileagetracker.R;
import pmg.android.showmileagetracker.StatusActivity;
import pmg.android.showmileagetracker.db.ShoeAggQuery;
import pmg.android.showmileagetracker.db.Shoes.EntryColumns;
import pmg.android.showmileagetracker.db.Shoes.EntryXRefColumns;
import pmg.android.showmileagetracker.db.ShoesProvider;
import pmg.android.showmileagetracker.util.DistanceHelper;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class ShowTrackerWidgetProvider extends AppWidgetProvider {
	private static final String TAG = EditEntryActivity.class.getName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, String.format("on receive %s", intent.getAction()));
		super.onReceive(context, intent);
		if (intent.getAction().equals(ShoesProvider.UPDATE_BROADCAST_ACTION)) {

			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			int[] widgitIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, this.getClass()));

			Log.i(TAG, String.format("Updating %d widgits", widgitIds.length));

			update(context, appWidgetManager, widgitIds);
		}
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.i(TAG, "on update");
		update(context, appWidgetManager, appWidgetIds);
	}

	private void update(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.i(TAG, "updating");

		RemoteViews views = getRemoteViews(context);
		if (views == null)
			return;

		final int imax = appWidgetIds.length;
		for (int i = 0; i < imax; i++) {
			int appWidgetId = appWidgetIds[i];

			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

	private RemoteViews getRemoteViews(Context context) {

		ShoeAggQuery.ShoeDetails shoeDetails = new ShoeAggQuery(context).getNextShoe();
		Intent addLatestShoeIntent = new Intent(context, EditEntryActivity.class);
		PendingIntent pendingLaunchShoeIntent = PendingIntent.getActivity(context, 0, addLatestShoeIntent, 0);
		Intent launchAppIntent = new Intent(context, StatusActivity.class);
		PendingIntent pendingLaunchAppIntent = PendingIntent.getActivity(context, 0, launchAppIntent, 0);

		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.status_widget);
		views.setOnClickPendingIntent(R.id.newActivityButton, pendingLaunchShoeIntent);
		views.setOnClickPendingIntent(R.id.launchAppButton, pendingLaunchAppIntent);
		views.setInt(R.id.newActivityButton, "setBackgroundColor", shoeDetails.colour);
		views.setTextViewText(R.id.distanceEditText, new DistanceHelper(context).getDistanceDisplay(shoeDetails.totalDistance));

		return views;
	}
}
