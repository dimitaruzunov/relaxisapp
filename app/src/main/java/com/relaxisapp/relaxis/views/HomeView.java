package com.relaxisapp.relaxis.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.relaxisapp.relaxis.HintHelper;
import com.relaxisapp.relaxis.R;
import com.relaxisapp.relaxis.activities.HomeFragment;
import com.relaxisapp.relaxis.events.Event;
import com.relaxisapp.relaxis.events.EventListener;
import com.relaxisapp.relaxis.models.AppModel;

/**
 * Created by zdravko on 14-5-2.
 */
public class HomeView extends RelativeLayout {

    /**
     * The interface to send events from the view to the controller
     */
    public static interface ViewListener {
        public void onToggleTimer();
        public void onAddTime(long amountToAdd);
    }

    private static boolean DEBUG = false;
    //private static final String TAG = MainView.class.getSimpleName();
    private static final long AMOUNT_TO_ADD = 1234 * 60 * 2;

    private static TextView heartRateTextView;
    private static TextView instantSpeedTextView;
    private static TextView rRIntervalTextView;
    private static TextView instantHeartRateTextView;

    private Button toggleBtn, addBtn;
    private AppModel model;

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
        model = AppModel.getInstance();
    }

    /**
     * Exposed method so the controller can set the button state.
     */
    public void setPausedState(boolean isTimerRunning) {
//        String txt = (isTimerRunning) ? getContext().getString(R.string.stop) : getContext().getString(R.string.start);
//        toggleBtn.setText(txt);
    }

    /**
     * Remove the listener from the model
     */
    public void destroy() {
        //model.removeListener(AppModel.ChangeEvent.ELAPSED_TIME_CHANGED, elapsedTimeListener);
    }

    /**
     * Does the work to update the view when the model changes.
     */
    private void bind() {
        int milli = (int)Math.floor((model.getElapsedTime() % 1000));
        int secs = (int)Math.floor((model.getElapsedTime() / 1000) % 60);
        int mins = (int)Math.floor((model.getElapsedTime() / 1000 / 60) % 60);

        if (DEBUG) {
//            Log.i(TAG, "elapsed: " + model.getElapsedTime());
//            Log.i(TAG, "secs: " + secs);
//            Log.i(TAG, "mins: " + mins);
        }

//        d1.showTime((int)Math.floor(mins/10));
//        d2.showTime(mins % 10);
//        d3.showTime((int)Math.floor(secs/10));
//        d4.showTime(secs % 10);
//        d5.showTime((int)Math.floor(milli/100));
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

        rRIntervalTextView = (TextView) findViewById(R.id.rRIntervalTextView);
        rRIntervalTextView.setOnClickListener(new View.OnClickListener() {
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


        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onToggleTimer();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onAddTime(AMOUNT_TO_ADD);
            }
        });
        model.addListener(AppModel.ChangeEvent.ELAPSED_TIME_CHANGED, elapsedTimeListener);
        bind();
    }

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

    /**
     * The listener for when the elapsed time property changes on the model
     */
    private EventListener elapsedTimeListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            bind();
        }
    };
}
