package com.relaxisapp.relaxis.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.relaxisapp.relaxis.HintHelper;
import com.relaxisapp.relaxis.R;
import com.relaxisapp.relaxis.events.Event;
import com.relaxisapp.relaxis.events.EventListener;
import com.relaxisapp.relaxis.models.HomeModel;

/**
 * Created by zdravko on 14-5-2.
 */
public class HomeView extends ScrollView {

    /**
     * The interface to send events from the view to the controller
     */
    public static interface ViewListener {
        public void onConnectButtonClick();
        public void onMusicButtonClick();
    }

    private static boolean DEBUG = false;
    private static final String TAG = HomeView.class.getSimpleName();

    private static TextView heartRateTextView;
    private static TextView rrIntervalTextView;
    private static TextView instantHeartRateTextView;
    private static TextView instantSpeedTextView;

    Button connectButton, musicButton;
    private HomeModel model;

    /**
     * The listener reference for sending events
     */
    private ViewListener viewListener;
    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    /**
     * Constructor for xml layouts
     */
    public HomeView (Context context, AttributeSet attrs) {
        super(context, attrs);
        model = HomeModel.getInstance();
    }

    public void updateConnectButton() {
        switch (model.getConnectionState()) {
            case 0:
                connectButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_bluetooth, 0, 0, 0);
                connectButton.setText(R.string.action_bluetooth_connect);
                break;
            case 1:
                connectButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_bluetooth_searching, 0, 0, 0);
                connectButton.setText(R.string.action_bluetooth_connecting);
                break;
            case 2:
                connectButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_bluetooth_connected, 0, 0, 0);
                connectButton.setText(R.string.action_bluetooth_disconnect);
                break;
        }
    }

    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Remove the listener from the model
     */
    public void destroy() {
        //model.removeListener(HomeModel.ChangeEvent.ELAPSED_TIME_CHANGED, elapsedTimeListener);
    }

    /**
     * Does the work to update the view when the model changes.
     */
    private void bindHeartRate() {
        int heartRate = model.getHeartRate();

        if (DEBUG) {
            Log.i(TAG, "Heart rate: " + model.getHeartRate());
        }

        String text = (heartRate > 0) ? String.valueOf(heartRate) : getResources().getString(R.string.heartRate);
        heartRateTextView.setText(text);
    }
    private void bindRrInterval() {
        int rrInterval = model.getRrInterval();

        if (DEBUG) {
            Log.i(TAG, "R-R interval: " + model.getRrInterval());
        }

        String text = (rrInterval > 0) ? String.valueOf(rrInterval) : getResources().getString(R.string.rRInterval);
        rrIntervalTextView.setText(text);
    }
    private void bindInstantHeartRate() {
        int instantHeartRate = model.getInstantHeartRate();

        if (DEBUG) {
            Log.i(TAG, "Instant heart rate: " + model.getInstantHeartRate());
        }

        String text = (instantHeartRate > 0) ? String.valueOf(instantHeartRate) : getResources().getString(R.string.instantHeartRate);
        instantHeartRateTextView.setText(text);
    }
    private void bindInstantSpeed() {
        double instantSpeed = model.getInstantSpeed();

        if (DEBUG) {
            Log.i(TAG, "Instant speed: " + model.getInstantSpeed());
        }

        String text = (instantSpeed > 0) ? String.valueOf(instantSpeed) : getResources().getString(R.string.instantSpeed);
        instantSpeedTextView.setText(text);
    }

    /**
     * Find our references to the objects in the xml layout
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        heartRateTextView = (TextView) findViewById(R.id.heartRateTextView);
        heartRateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleHeartRateTextViewClick((TextView) view);
            }
        });

        rrIntervalTextView = (TextView) findViewById(R.id.rRIntervalTextView);
        rrIntervalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRRIntervalTextViewClick((TextView) view);
            }
        });

        instantHeartRateTextView = (TextView) findViewById(R.id.instantHeartRateTextView);
        instantHeartRateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleInstantHeartRateTextViewClick((TextView) view);
            }
        });

        instantSpeedTextView = (TextView) findViewById(R.id.instantSpeedTextView);
        instantSpeedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleInstantSpeedTextViewClick((TextView) view);
            }
        });


        connectButton = (Button) findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onConnectButtonClick();
            }
        });

        musicButton = (Button) findViewById(R.id.musicButton);
        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onMusicButtonClick();
            }
        });

        model.addListener(HomeModel.ChangeEvent.CONNECTION_STATE_CHANGED, connectionStateListener);
        model.addListener(HomeModel.ChangeEvent.HEART_RATE_CHANGED, heartRateListener);
        model.addListener(HomeModel.ChangeEvent.RR_INTERVAL_CHANGED, rrIntervalListener);
        model.addListener(HomeModel.ChangeEvent.INSTANT_HEART_RATE_CHANGED, instantHeartRateListener);
        model.addListener(HomeModel.ChangeEvent.INSTANT_SPEED_CHANGED, instantSpeedListener);
        updateConnectButton();
        bindHeartRate();
        bindRrInterval();
        bindInstantHeartRate();
        bindInstantSpeed();
    }

    private EventListener connectionStateListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            updateConnectButton();
        }
    };
    private EventListener heartRateListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            bindHeartRate();
        }
    };
    private EventListener rrIntervalListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            bindRrInterval();
        }
    };
    private EventListener instantHeartRateListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            bindInstantHeartRate();
        }
    };
    private EventListener instantSpeedListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            bindInstantSpeed();
        }
    };

    void handleHeartRateTextViewClick(TextView textView) {
        HintHelper.createAndPositionHint(getContext(), R.string.heartRate, textView).show();
    }

    void handleInstantHeartRateTextViewClick(TextView textView) {
        HintHelper.createAndPositionHint(getContext(), R.string.instantHeartRate, textView).show();
    }

    void handleRRIntervalTextViewClick(TextView textView) {
        HintHelper.createAndPositionHint(getContext(), R.string.rRInterval, textView).show();
    }

    void handleInstantSpeedTextViewClick(TextView textView) {
        HintHelper.createAndPositionHint(getContext(), R.string.instantSpeed, textView).show();
    }

}