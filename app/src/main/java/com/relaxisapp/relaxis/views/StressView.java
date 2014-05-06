package com.relaxisapp.relaxis.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.relaxisapp.relaxis.R;
import com.relaxisapp.relaxis.events.Event;
import com.relaxisapp.relaxis.events.EventListener;
import com.relaxisapp.relaxis.models.StressModel;

public class StressView extends ScrollView {

    private TextView stressLevelTextView;

    private StressModel model;

    /**
     * Constructor for xml layouts
     */
    public StressView (Context context, AttributeSet attrs) {
        super(context, attrs);
        model = StressModel.getInstance();
    }

    private void updateStressLevel() {
        Log.i("STRESS", String.valueOf(model.getStressLevel()));
        stressLevelTextView.setText(String.valueOf(model.getStressLevel()));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        stressLevelTextView = (TextView) findViewById(R.id.stressLevelTextView);

        model.addListener(StressModel.ChangeEvent.STRESS_LEVEL_CHANGED, stressLevelListener);

        updateStressLevel();
    }

    private EventListener stressLevelListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            updateStressLevel();
        }
    };

    public void destroy() {
        model.removeListener(StressModel.ChangeEvent.STRESS_LEVEL_CHANGED, stressLevelListener);
    }
}
