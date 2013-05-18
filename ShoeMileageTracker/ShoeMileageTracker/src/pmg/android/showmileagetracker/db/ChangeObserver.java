package pmg.android.showmileagetracker.db;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

public class ChangeObserver extends ContentObserver{
	public interface ChangeObserverHandler	{
		void onChange(boolean selfChange);
	}
	
	private final ChangeObserverHandler handler;
	private final ContentResolver contentResolver;
	private final Uri [] uris;
	
	public ChangeObserver(ContentResolver contentResolver, Uri [] uris, ChangeObserverHandler changeObserverHandler){
		super(new Handler());
		this.handler = changeObserverHandler;
		this.contentResolver = contentResolver;
		this.uris = uris;
	}
	
	public void startObserving() {
		for(Uri uri : uris) {
			contentResolver.registerContentObserver(uri, true, this);
		}
	}
	public void stopObserving() {
		contentResolver.unregisterContentObserver(this);
	}

	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		handler.onChange(selfChange);
	
	}
}
