package com.relaxisapp.relaxis.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.relaxisapp.relaxis.R;
import com.relaxisapp.relaxis.activities.MainActivity;
import com.relaxisapp.relaxis.activities.StressEstimationFragment;
import com.relaxisapp.relaxis.events.Event;
import com.relaxisapp.relaxis.events.EventListener;
import com.relaxisapp.relaxis.models.StressModel;

public class StressView extends ScrollView {

    private TextView stressLevelTextView;

    private int sliderWidth;

    private StressModel model;

    /**
     * Constructor for xml layouts
     */
    public StressView (Context context, AttributeSet attrs) {
        super(context, attrs);
        model = StressModel.getInstance();

        setSliderWidth(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void setSliderWidth(Context context) {
        // Get the display
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        // Get the width and height of the display
        Point size = new Point();
        display.getSize(size);

        // Save the display width
        sliderWidth = size.x - ((int) getResources().getDimension(R.dimen.layout_padding)) * 2;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void updateStressLevel() {
        int stressLevel = (int) (model.getStressLevel() * 100);

        Log.d("Slash", String.valueOf((float) ((stressLevel * sliderWidth) / 100.0)));

        stressLevelTextView.setText(String.valueOf(stressLevel));
        stressLevelTextView.setX((float) ((stressLevel * sliderWidth) / 100.0));
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
