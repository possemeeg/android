package pmg.android.showmileagetracker.layouts;

import pmg.android.showmileagetracker.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class OkCancelLayout extends LinearLayout implements OnClickListener {

	public interface ClickHandler {
		void onOk();
		void onCancel();
	}
	public interface SaveHandler {
		void onSave();
		void finish();
	}
	
	private ClickHandler clickHandler;
	private SaveHandler saveHandler;

	public OkCancelLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater.from(context).inflate(R.layout.ok_cancel, this, true);

		((Button) findViewById(R.id.ok)).setOnClickListener(this);
		((Button) findViewById(R.id.cancel)).setOnClickListener(this);
	}

	public void setClickHandler(ClickHandler handler) {
		this.clickHandler = handler;
	}
	public void setSaveHandler(SaveHandler handler) {
		this.saveHandler = handler;
		this.clickHandler = new ClickHandler()
		{
			public void onOk()
			{
				saveHandler.onSave();
				saveHandler.finish();
			}
			public void onCancel()
			{
				saveHandler.finish();
			}
		};
	}

	@Override
	public void onClick(View arg0) {
		if (clickHandler == null)
			return;

		switch (arg0.getId()) {
		case R.id.ok:
			clickHandler.onOk();
			break;
		case R.id.cancel:
			clickHandler.onCancel();
			break;
		}

	}
}
