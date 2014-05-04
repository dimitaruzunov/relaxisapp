package com.relaxisapp.relaxis.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView.GraphViewData;
import com.relaxisapp.relaxis.utils.BTBondReceiver;
import com.relaxisapp.relaxis.utils.BTBroadcastReceiver;
import com.relaxisapp.relaxis.utils.ConnectionListener;
import com.relaxisapp.relaxis.widgets.NavigationDrawerItem;
import com.relaxisapp.relaxis.widgets.NavigationDrawerListAdapter;
import com.relaxisapp.relaxis.R;
import com.relaxisapp.relaxis.widgets.SectionsPagerAdapter;
import com.relaxisapp.relaxis.models.BreathingModel;
import com.relaxisapp.relaxis.models.HomeModel;
import com.relaxisapp.relaxis.utils.BtConnection;
import com.relaxisapp.relaxis.utils.Const;

import java.util.ArrayList;
import java.util.Set;

import zephyr.android.HxMBT.BTClient;

public class MainActivity extends ActionBarActivity implements ListView.OnItemClickListener {

    private HomeModel homeModel;
    private BreathingModel breathingModel;

	private NavigationDrawerListAdapter navigationDrawerListAdapter;
    private SectionsPagerAdapter sectionsPagerAdapter;
	static ViewPager viewPager;
    private BluetoothConnectTask connectTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

        homeModel = HomeModel.getInstance();
        breathingModel = BreathingModel.getInstance();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// Navigation drawer setup
		String[] navigationMenuTitles = getResources().getStringArray(R.array.navigation_drawer_options);
        int[] navigationMenuColors = getResources().getIntArray(R.array.navigation_drawer_colors);
		TypedArray navigationMenuIcons = getResources().obtainTypedArray(R.array.navigation_drawer_icons);

		ArrayList<NavigationDrawerItem> navigationDrawerItems = new ArrayList<NavigationDrawerItem>();

		for (int i = 0, len = navigationMenuTitles.length; i < len; i++) {
			navigationDrawerItems.add(new NavigationDrawerItem(navigationMenuTitles[i],
                    navigationMenuColors[i],
                    navigationMenuIcons.getResourceId(i, -1)));
		}

		// Recycle the typed arrays
		navigationMenuIcons.recycle();

		navigationDrawerListAdapter = new NavigationDrawerListAdapter(getApplicationContext(), navigationDrawerItems);

        ListView drawerListView = (ListView) findViewById(R.id.left_drawer);
		drawerListView.setAdapter(navigationDrawerListAdapter);

		navigationDrawerListAdapter.setup(this, this);
		navigationDrawerListAdapter.setSelection(NavigationDrawerListAdapter.HOME_OPTION_ITEM);

		// Sections pager setup
		sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(sectionsPagerAdapter);
		viewPager.setCurrentItem(SectionsPagerAdapter.HOME_FRAGMENT);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				navigationDrawerListAdapter.handleSelect(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		// TODO Try to put the following code out of the onCreate method

		/*
		 * Sending a message to android that we are going to initiate a pairing
		 * request
		 */
		IntentFilter filter = new IntentFilter(
				"android.bluetooth.device.action.PAIRING_REQUEST");
		/*
		 * Registering a new BTBroadcast receiver from the Main Activity context
		 * with pairing request event
		 */
		this.getApplicationContext().registerReceiver(
				new BTBroadcastReceiver(), filter);
		// Registering the BTBondReceiver in the application that the
		// status of the receiver has changed to Paired
		IntentFilter filter2 = new IntentFilter(
				"android.bluetooth.device.action.BOND_STATE_CHANGED");
		this.getApplicationContext().registerReceiver(new BTBondReceiver(),
				filter2);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		navigationDrawerListAdapter.syncState();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		navigationDrawerListAdapter.handleOnPrepareOptionsMenu(menu);

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		boolean handled = true;

		navigationDrawerListAdapter.handleOnOptionsItemSelected(item);

		return handled;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		navigationDrawerListAdapter.syncState();
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
		super.onActivityResult(requestCode, resultCode, resultIntent);
		switch (requestCode) {
		case Const.REQUEST_ENABLE_BT:
			handleBluetoothConnectResult(resultCode, resultIntent);
			break;
		}
	}

    // TODO remove all toasts from the activities and place them into the views

	private void handleBluetoothConnectResult(int resultCode, Intent resultIntent) {
		if (resultCode == RESULT_OK) {
			Toast.makeText(this, "Bluetooth is now enabled", Toast.LENGTH_LONG).show();
		} else {
//			setPreviousOnButtonClickListener(savedButton);
			Toast.makeText(this, "User cancelled the bluetooth enable intent", Toast.LENGTH_LONG).show();
			homeModel.setConnectionState(0);
		}
	}

	void executeConnect() {
        homeModel.setConnectionState(1);

		connectTask = new BluetoothConnectTask();
        connectTask.execute();
	}

	private class BluetoothConnectTask extends
			AsyncTask<Void, Void, Integer> {

		private final int CODE_CANCELLED = 3;
		private final int CODE_NO_BT = 2;
		private final int CODE_FAILURE = 1;
		private final int CODE_SUCCESS = 0;

		@Override
		protected Integer doInBackground(Void... voids) {

			int result;
			
			// do the work unless user cancel
			while (!isCancelled()) {
                // connection state - connecting
                //homeModel.setConnectionState(1);

				// Getting the Bluetooth adapter
				BtConnection.adapter = BluetoothAdapter.getDefaultAdapter();

				// Check for Bluetooth support
				if (BtConnection.adapter == null) {
					result = CODE_NO_BT;
					return result;
				}

				// Enable bluetooth if not enabled
				if (!BtConnection.adapter.isEnabled()) {
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableBtIntent, Const.REQUEST_ENABLE_BT);
				}

				// TODO try to write this better
				while (!BtConnection.adapter.isEnabled()) {
					// wait until the bluetooth is on
				}

				Set<BluetoothDevice> pairedDevices = BtConnection.adapter
						.getBondedDevices();
				if (pairedDevices.size() > 0) {
					for (BluetoothDevice device : pairedDevices) {
						if (device.getName().startsWith("HXM")) {
							BtConnection.BhMacID = device.getAddress();

							BluetoothDevice Device = BtConnection.adapter
									.getRemoteDevice(BtConnection.BhMacID);
							BtConnection.deviceName = Device.getName();

							BtConnection._bt = new BTClient(
									BtConnection.adapter, BtConnection.BhMacID);

							BtConnection._NConnListener = new ConnectionListener(
									SensorDataHandler, SensorDataHandler);
							BtConnection._bt
									.addConnectedEventListener(BtConnection._NConnListener);

							if (BtConnection._bt.IsConnected()) {
								BtConnection._bt.start();

								// TODO ? Reset all the values to 0s

								result = CODE_SUCCESS;
								return result;
							} else {
								result = CODE_FAILURE;
								return result;
							}
						}
					}
				}
			}

			result = CODE_CANCELLED;
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
//			setPreviousOnButtonClickListener(results.item);
			
			switch (result) {
			case CODE_NO_BT:
				homeModel.setConnectionState(0);
				Toast.makeText(MainActivity.this, "Bluetooth is not supported",
						Toast.LENGTH_LONG).show();
				break;
			case CODE_FAILURE:
                homeModel.setConnectionState(0);
				Toast.makeText(MainActivity.this, "Unable to connect",
						Toast.LENGTH_LONG).show();
				break;
			case CODE_SUCCESS:
                homeModel.setConnectionState(2);
				Toast.makeText(MainActivity.this,
						"Connected to HxM " + BtConnection.deviceName,
						Toast.LENGTH_LONG).show();

				// reset the stress score and time
				StressEstimationFragment.timeLeft = Const.TIME_STRESS_SECONDS;
				BtConnection.recentNn50 = new int[Const.SAVED_NN50_COUNT];
				for (int i = 0; i < BtConnection.recentNn50.length; i++) {
					BtConnection.recentNn50[i] = 0;
				}

				// reset the instant HR arr
				BtConnection.recentInstantHR = new int[Const.SAVED_HR_COUNT];
				for (int i = 0; i < BtConnection.recentInstantHR.length; i++) {
					BtConnection.recentInstantHR[i] = 0;

				}

                breathingModel.setGraphUpdateStartedState(true);

				break;
			}
		}

		protected void onCancelled() {
            // TODO check whether the connection has been established before the cancellation and terminate it if so
            homeModel.setConnectionState(0);
			Toast.makeText(MainActivity.this, "Connecting cancelled", Toast.LENGTH_LONG).show();
		}

	}

    void cancelConnecting() {
        while (!connectTask.isCancelled()) {
            connectTask.cancel(true);
        }
    }

	void executeDisconnect() {
        homeModel.setConnectionState(0);

		Toast.makeText(this, "Disconnected from HxM", Toast.LENGTH_LONG).show();

		// This disconnects listener from acting on received messages
		BtConnection._bt
				.removeConnectedEventListener(BtConnection._NConnListener);
		// Close the communication with the device & throw an exception if
		// failure
		BtConnection._bt.Close();

        breathingModel.setGraphUpdateStartedState(false);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int option, long id) {
		sectionsPagerAdapter.setFragment(option, viewPager);
		navigationDrawerListAdapter.closeDrawer();
	}

	final Handler SensorDataHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Const.HEART_RATE:
                if (msg.getData().getString("HeartRate") != null) {
                    int heartRate = Integer.parseInt(msg.getData().getString("HeartRate"));
                    if (heartRate > 0) {
                        homeModel.setHeartRate(heartRate);
                    }
                }
				break;

			case Const.INSTANT_SPEED:
                if (msg.getData().getString("InstantSpeed") != null) {
                    double instantSpeed = Double.parseDouble(msg.getData().getString("InstantSpeed"));
                    if (instantSpeed > 0) {
                        homeModel.setInstantSpeed(instantSpeed);
                    }
                }
				break;

			case Const.RR_INTERVAL:
                if (msg.getData().getString("RRInterval") != null) {
                    int rrInterval = Integer.parseInt(msg.getData().getString("RRInterval"));
                    if (rrInterval > 0) {
                        homeModel.setRrInterval(rrInterval);
                    }
                }
                break;

			case Const.INSTANT_HR:
                if (msg.getData().getString("InstantHR") != null) {
                    String instantHRString = msg.getData().getString("InstantHR");
                    int instantHR = Integer.parseInt(instantHRString);
                    if (instantHR > 0) {
                        homeModel.setInstantHeartRate(instantHR);
                    }

                    updateBreathingGraph(instantHR);
                }

				break;

			case Const.PNN50:
                if (msg.getData().getString("pNN50") != null) {
                    if (StressEstimationFragment.timeLeft > 0 && StressEstimationFragment.updateScore) {
                        String pNN50 = msg.getData().getString("pNN50");

                        if (pNN50 != null) {
                            StressEstimationFragment.stressLevel = Double.parseDouble(pNN50);
                        }
                    }
                }
				break;
			}
		}

	};

    private void updateBreathingGraph(int instantHR) {
        // update BreathingFragment
        breathingModel.addHrGraphData(new GraphViewData(
                        BreathingFragment.beatsCount, instantHR));
        BreathingFragment.beatsCount++;

        BtConnection.recentInstantHR[BreathingFragment.beatsCount
                % Const.SAVED_HR_COUNT] = instantHR;

        int tAvgMaxHR = 0,
                tAvgMinHR = 0,
                tAvgMaxCount = 0,
                tAvgMinCount = 0;
        int iHR,
                iHRPlus1,
                iHRPlus2,
                iHRMinus1,
                iHRMinus2;

        for (int i = 0; i < BtConnection.recentInstantHR.length; i++) {
            iHR = BtConnection.recentInstantHR[i];
            iHRPlus1 = BtConnection.recentInstantHR[(i + 1)
                    % Const.SAVED_HR_COUNT];
            iHRPlus2 = BtConnection.recentInstantHR[(i + 2)
                    % Const.SAVED_HR_COUNT];
            iHRMinus1 = BtConnection.recentInstantHR[(Const.SAVED_HR_COUNT
                    + i - 1)
                    % Const.SAVED_HR_COUNT];
            iHRMinus2 = BtConnection.recentInstantHR[(Const.SAVED_HR_COUNT
                    + i - 2)
                    % Const.SAVED_HR_COUNT];
            if (i < ((BreathingFragment.beatsCount - 1)
                    % Const.SAVED_HR_COUNT + Const.SAVED_HR_COUNT - 2)
                    % Const.SAVED_HR_COUNT
                    || i > ((BreathingFragment.beatsCount - 1)
                    % Const.SAVED_HR_COUNT + 2)
                    % Const.SAVED_HR_COUNT) {
                if (iHRPlus1 > 0 && iHRMinus1 > 0 && iHRPlus2 > 0
                        && iHRMinus2 > 0 && iHR >= iHRPlus1
                        && iHR >= iHRPlus2 && iHR >= iHRMinus1
                        && iHR >= iHRMinus2) {
                    tAvgMaxHR += iHR;
                    tAvgMaxCount++;
                }
                if (iHRPlus1 > 0 && iHRMinus1 > 0 && iHRPlus2 > 0
                        && iHRMinus2 > 0 && iHR <= iHRPlus1
                        && iHR <= iHRPlus2 && iHR <= iHRMinus1
                        && iHR <= iHRMinus2) {
                    tAvgMinHR += iHR;
                    tAvgMinCount++;
                }
            }
        }
        if (tAvgMaxCount > 0) { // => tAvgMaxHR > 0
            tAvgMaxHR /= tAvgMaxCount;
        }
        if (tAvgMinCount > 0) { // => tAvgMinHR > 0
            tAvgMinHR /= tAvgMinCount;
        }

        if (tAvgMaxCount > 0 && tAvgMinCount > 0) {
            if ((tAvgMaxHR - tAvgMinHR) / 2 > Const.IDEAL_HR_DEVIATION) {
                BreathingFragment.newMaxHR = tAvgMaxHR;
                BreathingFragment.newMinHR = tAvgMinHR;
            } else {
                if (tAvgMaxHR > BreathingFragment.newMaxHR) {
                    BreathingFragment.newMaxHR = tAvgMaxHR;
                }
                if (tAvgMinHR < BreathingFragment.newMinHR) {
                    BreathingFragment.newMinHR = tAvgMinHR;
                }
            }
        }

        if (BreathingFragment.updateScore) {
            if (Math.abs(BreathingFragment.tIdealHR - instantHR) <= Const.POINT_BARRIER) {
                BreathingFragment.consecutivePoints++;
                if (BreathingFragment.consecutivePoints >= 5 * BreathingFragment.multiplier) {
                    BreathingFragment.multiplier++;
                }
                breathingModel.setScore(breathingModel.getScore() + BreathingFragment.multiplier);
            } else {
                BreathingFragment.consecutivePoints = 0;
                BreathingFragment.multiplier = 1;
            }

        }
    }
}
