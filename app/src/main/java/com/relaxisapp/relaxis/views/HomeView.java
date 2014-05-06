package com.relaxisapp.relaxis.views;

import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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

        void onPrevButtonClick();

        void onPauseButtonClick();

        void onStopButtonClick();

        void onNextButtonClick();
    }

    private static boolean DEBUG = false;
    private static final String TAG = HomeView.class.getSimpleName();

    private TextView heartRateTextView;
    private TextView rrIntervalTextView;
    private TextView instantHeartRateTextView;
    private TextView instantSpeedTextView;

    Button connectButton, musicButton;
    ImageButton prevButton, pauseButton, stopButton, nextButton;
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

    /**
     * Does the work to update the view when the model changes.
     */
    private void updateConnectButton() {
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

    private void updatePauseButton() {
        boolean musicPlayed = model.getMusicPlayed();

        if (musicPlayed) {
            pauseButton.setImageResource(R.drawable.ic_action_pause);
        } else {
            pauseButton.setImageResource(R.drawable.ic_action_play);
        }
    }

    /**
     * Does the work to update the view when the model changes.
     */
    private void updateHeartRate() {
        int heartRate = model.getHeartRate();

        if (DEBUG) {
            Log.i(TAG, "Heart rate: " + model.getHeartRate());
        }

        String text = (heartRate > 0) ? String.valueOf(heartRate) : "HR";
        heartRateTextView.setText(text);
    }
    private void updateRrInterval() {
        int rrInterval = model.getRrInterval();

        if (DEBUG) {
            Log.i(TAG, "R-R interval: " + model.getRrInterval());
        }

        String text = (rrInterval > 0) ? String.valueOf(rrInterval) : "RR";
        rrIntervalTextView.setText(text);
    }
    private void updateInstantHeartRate() {
        int instantHeartRate = model.getInstantHeartRate();

        if (DEBUG) {
            Log.i(TAG, "Instant heart rate: " + model.getInstantHeartRate());
        }

        String text = (instantHeartRate > 0) ? String.valueOf(instantHeartRate) : "IHR";
        instantHeartRateTextView.setText(text);
    }
    private void updateInstantSpeed() {
        double instantSpeed = model.getInstantSpeed();

        if (DEBUG) {
            Log.i(TAG, "Instant speed: " + model.getInstantSpeed());
        }

        String text = (instantSpeed > 0) ? String.valueOf(instantSpeed) : "IS";
        instantSpeedTextView.setText(text);
    }

    /**
     * Find our references to the objects in the xml layout
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        heartRateTextView = (TextView) findViewById(R.id.heartRateTextView);
        rrIntervalTextView = (TextView) findViewById(R.id.rRIntervalTextView);
        instantHeartRateTextView = (TextView) findViewById(R.id.instantHeartRateTextView);
        instantSpeedTextView = (TextView) findViewById(R.id.instantSpeedTextView);

        connectButton = (Button) findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onConnectButtonClick();
            }
        });

        musicPlayerSetup();

        model.addListener(HomeModel.ChangeEvent.CONNECTION_STATE_CHANGED, connectionStateListener);
        model.addListener(HomeModel.ChangeEvent.HEART_RATE_CHANGED, heartRateListener);
        model.addListener(HomeModel.ChangeEvent.RR_INTERVAL_CHANGED, rrIntervalListener);
        model.addListener(HomeModel.ChangeEvent.INSTANT_HEART_RATE_CHANGED, instantHeartRateListener);
        model.addListener(HomeModel.ChangeEvent.INSTANT_SPEED_CHANGED, instantSpeedListener);
        model.addListener(HomeModel.ChangeEvent.MUSIC_PLAYED, musicPlayedListener);

        updateConnectButton();
        updateHeartRate();
        updateRrInterval();
        updateInstantHeartRate();
        updateInstantSpeed();
    }

    private void musicPlayerSetup() {
        musicButton = (Button) findViewById(R.id.musicButton);
        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onMusicButtonClick();
            }
        });

        prevButton = (ImageButton) findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onPrevButtonClick();
            }
        });

        pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onPauseButtonClick();
            }
        });

        stopButton = (ImageButton) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onStopButtonClick();
            }
        });

        nextButton = (ImageButton) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onNextButtonClick();
            }
        });
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
            updateHeartRate();
        }
    };
    private EventListener rrIntervalListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            updateRrInterval();
        }
    };
    private EventListener instantHeartRateListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            updateInstantHeartRate();
        }
    };
    private EventListener instantSpeedListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            updateInstantSpeed();
        }
    };
    private EventListener musicPlayedListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            updatePauseButton();
        }
    };

    /**
     * Remove the listener from the model
     */
    public void destroy() {
        model.removeListener(HomeModel.ChangeEvent.CONNECTION_STATE_CHANGED, connectionStateListener);
        model.removeListener(HomeModel.ChangeEvent.HEART_RATE_CHANGED, heartRateListener);
        model.removeListener(HomeModel.ChangeEvent.RR_INTERVAL_CHANGED, rrIntervalListener);
        model.removeListener(HomeModel.ChangeEvent.INSTANT_HEART_RATE_CHANGED, instantHeartRateListener);
        model.removeListener(HomeModel.ChangeEvent.INSTANT_SPEED_CHANGED, instantSpeedListener);
        model.removeListener(HomeModel.ChangeEvent.MUSIC_PLAYED, musicPlayedListener);
    }

}
