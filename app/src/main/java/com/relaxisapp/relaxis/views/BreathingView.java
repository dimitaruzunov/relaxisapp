package com.relaxisapp.relaxis.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LineGraphView;
import com.relaxisapp.relaxis.R;
import com.relaxisapp.relaxis.events.Event;
import com.relaxisapp.relaxis.events.EventListener;
import com.relaxisapp.relaxis.models.BreathingModel;
import com.relaxisapp.relaxis.utils.BtConnection;
import com.relaxisapp.relaxis.utils.Const;

public class BreathingView extends ScrollView {

    /**
     * The interface to send events from the view to the controller
     */
    public interface ViewListener {
        public void onStartButtonClick();
    }

    private BreathingModel model;

    private GraphView graphView;

    private TextView timeLeftTextView;
    private TextView scoreTextView;
    private Button startBreathingButton;

    public BreathingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        model = BreathingModel.getInstance();
    }

    /**
     * The listener reference for sending events
     */
    private ViewListener viewListener;
    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    private void updateButton() {
        if (model.getStartedState()) {
            startBreathingButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_stop, 0, 0, 0);
            startBreathingButton.setText(R.string.stop);
        }
        else {
            startBreathingButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_play, 0, 0, 0);
            startBreathingButton.setText(R.string.start);
        }
    }

    private void updateTimeLeft() {
        timeLeftTextView.setText(String.valueOf(model.getTimeLeft()));
    }

    private void updateScore() {
        scoreTextView.setText(String.valueOf(model.getScore()));
    }

    private void loadIdealGraph() {
        BtConnection.idealBreathingCycle.resetData(model.getIdealGraphData());
    }

    private void loadHrGraph() {
        BtConnection.instantHRSeries.resetData(model.getHrGraphData());
    }

    private void updateIdealGraph() {
		BtConnection.idealBreathingCycle.appendData(model.getLastIdealGraphData(), false,
                Const.VIEWPORT_WIDTH * Const.TIMER_TICKS_PER_SECOND);
    }

    private void updateHrGraph() {
        BtConnection.instantHRSeries.appendData(model.getLastHrGraphData(), false,
                Const.VIEWPORT_WIDTH + 1);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        LinearLayout layout = (LinearLayout) findViewById(R.id.breathingFragmentLinearLayout);
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        timeLeftTextView = (TextView) findViewById(R.id.breathingTimeLeftTextView);

        startBreathingButton = (Button) findViewById(R.id.startBreathingButton);
        startBreathingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onStartButtonClick();
            }
        });

        graphView = new LineGraphView(getContext(), "Breathing");
        graphView.setScrollable(true);
        graphView.setScalable(true);
        graphView.setDisableTouch(true);
        graphView.setViewPort(0, Const.VIEWPORT_WIDTH);
        graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return "";
                } else {
                    return "";
                }
            }
        });
        graphView.getGraphViewStyle().setNumHorizontalLabels(1);
        graphView.getGraphViewStyle().setNumVerticalLabels(5);
        graphView.getGraphViewStyle().setVerticalLabelsWidth(1);
        graphView.getGraphViewStyle().setGridColor(getResources().getColor(R.color.grid));
        graphView.getGraphViewStyle().setHorizontalLabelsColor(getResources().getColor(R.color.text_dark));
        graphView.getGraphViewStyle().setTextSize(25);
        graphView.addSeries(BtConnection.idealBreathingCycle);
        graphView.addSeries(BtConnection.instantHRSeries);
        graphView.addSeries(BtConnection.dummySeries);
        // TODO show legend and customize it

        layout.addView(graphView, setupLayoutParams());

        model.addListener(BreathingModel.ChangeEvent.STARTED_STATE_CHANGED, startedStateListener);
        model.addListener(BreathingModel.ChangeEvent.TIME_LEFT_CHANGED, timeLeftListener);
        model.addListener(BreathingModel.ChangeEvent.SCORE_CHANGED, scoreListener);
        model.addListener(BreathingModel.ChangeEvent.IDEAL_GRAPH_DATA_ADDED, idealGraphListener);
        model.addListener(BreathingModel.ChangeEvent.HR_GRAPH_DATA_ADDED, hrGraphListener);

        loadHrGraph();
        loadIdealGraph();
        updateButton();
        updateTimeLeft();
        updateScore();
    }

    private ViewGroup.LayoutParams setupLayoutParams() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
    }

    private EventListener startedStateListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            updateButton();
        }
    };
    private EventListener timeLeftListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            updateTimeLeft();
        }
    };
    private EventListener scoreListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            updateScore();
        }
    };
    private EventListener idealGraphListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            updateIdealGraph();
        }
    };
    private EventListener hrGraphListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            updateHrGraph();
        }
    };


    /**
     * Remove the listener from the model
     */
    public void destroy() {
        model.removeListener(BreathingModel.ChangeEvent.STARTED_STATE_CHANGED, startedStateListener);
        model.removeListener(BreathingModel.ChangeEvent.TIME_LEFT_CHANGED, timeLeftListener);
        model.removeListener(BreathingModel.ChangeEvent.SCORE_CHANGED, scoreListener);
        model.removeListener(BreathingModel.ChangeEvent.IDEAL_GRAPH_DATA_ADDED, idealGraphListener);
        model.removeListener(BreathingModel.ChangeEvent.HR_GRAPH_DATA_ADDED, hrGraphListener);
    }
}
