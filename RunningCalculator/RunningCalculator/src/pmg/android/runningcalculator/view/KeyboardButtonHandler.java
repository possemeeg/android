package pmg.android.runningcalculator.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class KeyboardButtonHandler {
	private TextView valueTextView;

	public static interface NotifyTextChange {
		void onTextChanged(String newText);
	}
	private NotifyTextChange notifyTextChanged;
	
	public void initialise(TextView valueTextView, ViewGroup buttonParent, View backButton, NotifyTextChange notifyTextChanged){
		this.valueTextView = valueTextView;
		this.notifyTextChanged = notifyTextChanged;
		attachButtons(buttonParent, backButton);
	}
	
	private android.view.View.OnClickListener buttonListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			CharSequence text = valueTextView.getText();
			String newText = text.toString() + ((Button) v).getText();
			setNewText(newText);
		}
	};
	
	private void attachButtons(ViewGroup parent, View backButton) {
		attachKeyboardButtons(parent);
		
		backButton.setOnClickListener(
				new android.view.View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						CharSequence text = valueTextView.getText();
						if(text.length() <=0 )
							return;
						setNewText(text.toString().substring(0, text.length() - 1));
					}
				});
	}
	private void attachKeyboardButtons(ViewGroup parent) {
		for (int i = 0, imax = parent.getChildCount(); i < imax; ++i) {
			View view = parent.getChildAt(i);
			if (view instanceof ViewGroup) {
				attachKeyboardButtons((ViewGroup) view);
			} else if (view instanceof Button) {
				((Button) view).setOnClickListener(buttonListener);
			}
		}
	}
	public void resetText() {
		setNewText("");
	}
	private void setNewText(String newText){
		valueTextView.setText(newText);
		notifyTextChanged.onTextChanged(newText);
	}

}
