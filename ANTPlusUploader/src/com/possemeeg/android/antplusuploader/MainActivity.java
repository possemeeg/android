package com.possemeeg.android.antplusuploader;

import java.io.IOException;

import com.dsi.ant.AntInterface;
import com.dsi.ant.AntInterface.ServiceListener;
import com.dsi.ant.AntInterfaceIntent;
import com.dsi.ant.exception.AntInterfaceException;
import com.possemeeg.lib.garmin.ConnectServiceClient;
import com.possemeeg.lib.garmin.dto.ActivityContainer;
import com.possemeeg.lib.garmin.dto.Results;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	private final AntInterface antReceiver;
	private TextView consoleTextView;
	private boolean claimedInterface;

	public MainActivity() {
		this.antReceiver = new AntInterface();

		// ChannelConfiguration channelConfig = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		consoleTextView = (TextView) findViewById(R.id.console);

		IntentFilter statusIntentFilter = new IntentFilter();
		statusIntentFilter.addAction(AntInterfaceIntent.ANT_ENABLED_ACTION);
		statusIntentFilter.addAction(AntInterfaceIntent.ANT_ENABLING_ACTION);
		statusIntentFilter.addAction(AntInterfaceIntent.ANT_DISABLED_ACTION);
		statusIntentFilter.addAction(AntInterfaceIntent.ANT_DISABLING_ACTION);
		statusIntentFilter.addAction(AntInterfaceIntent.ANT_RESET_ACTION);
		statusIntentFilter.addAction(AntInterfaceIntent.ANT_INTERFACE_CLAIMED_ACTION);
		statusIntentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);

		registerReceiver(antStatusReceiver, statusIntentFilter);


		if (AntInterface.hasAntSupport(this)) {
			appendConsole("turd - ant support");
			initANT();
		} else
			appendConsole("bollocks - no ant support");

		new Thread(new Runnable() {

			@Override
			public void run() {
				final String out = initGarmin();
				consoleTextView.post(new Runnable() {

					@Override
					public void run() {
						appendConsole(out);
					}
				});
			}
		}).start();
	}

	private String initGarmin() {
		StringBuilder sb = new StringBuilder();

		ConnectServiceClient client = new ConnectServiceClient();
		try {
			sb.append("Logging on!");

			if (!client.logon("pete@possemeeg.com", "b@Ncly607")) {
				sb.append("Failed to log on.");
				return sb.toString();
			}

			sb.append("Retrieving activities");
			Results results = client.getActivities();
			if (results == null) {
				sb.append("Failed to get activities.");
				return sb.toString();
			}

			for (ActivityContainer activity : results.getActivities()) {
				sb.append(activity.toString());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private void initANT() {
		antReceiver.initService(this, new ServiceListener() {
			@Override
			public void onServiceConnected() {
				appendConsole("connected");

				if (!antReceiver.isServiceConnected()) {
					appendConsole("Expecting connected service");
					return;
				}

				try {
					claimedInterface = antReceiver.hasClaimedInterface();
					if (claimedInterface) {
						appendConsole("Interface claimed");
					} else {
						antReceiver.claimInterface();
					}
				} catch (AntInterfaceException e) {
					showError(e);
					return;
				}

			}

			@Override
			public void onServiceDisconnected() {
				appendConsole("disconnected");
			}

		});
	}

	private void showError(AntInterfaceException e) {
		appendConsole("Error: " + e.getMessage());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void appendConsole(String line) {
		consoleTextView.setText(new StringBuilder(consoleTextView.getText()).append('\n').append(line).toString());
	}

	private final BroadcastReceiver antStatusReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			appendConsole("Status action received: " + intent.getAction());

			if (intent.getAction().equals(AntInterfaceIntent.ANT_INTERFACE_CLAIMED_ACTION)) {
				claimedInterface = true;
				registerReceiver(antMessageReceiver, new IntentFilter(AntInterfaceIntent.ANT_RX_MESSAGE_ACTION));
			}
		}
	};

	private final BroadcastReceiver antMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			appendConsole("Message action received: " + intent.getAction());

		}

	};

}
