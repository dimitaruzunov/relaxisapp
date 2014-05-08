package com.relaxisapp.relaxis.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.ValueDependentColor;
import com.relaxisapp.relaxis.R;
import com.relaxisapp.relaxis.events.Event;
import com.relaxisapp.relaxis.events.EventListener;
import com.relaxisapp.relaxis.models.UserModel;

public class UserView extends ScrollView {

    private ProfilePictureView profilePictureView;
    private TextView userName;

    private GraphViewSeries breathingScoresSeries;
    private GraphViewSeries stressScoresSeries;

    private UserModel model;

    public UserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        model = UserModel.getInstance();
    }

    public void toggleViewsVisibility(int visibility) {
        userName.setVisibility(visibility);
        profilePictureView.setVisibility(visibility);
    }

    private void updateFbUserInfo() {
        userName.setText(model.getFbUserName());
        profilePictureView.setProfileId(model.getFbUserId());
    }

    private void updateBreathingScores() {
        // run it on the UI thread
        this.post(new Runnable() {
            @Override
            public void run() {
                if (model.getBreathingScores() != null) {
                    breathingScoresSeries.appendData(model.getLastBreathingScore(), false, 5);
                }
            }
        });
    }

    private void updateStressScores() {
        // run it on the UI thread
        this.post(new Runnable() {
            @Override
            public void run() {
                if (model.getStressScores() != null) {
                    stressScoresSeries.appendData(model.getLastStressScore(), false, 5);
                };
            }
        });
    }

    private void loadBreathingScores() {
        stressScoresSeries.resetData(model.getBreathingScores());
    }

    private void loadStressScores() {
        stressScoresSeries.resetData(model.getStressScores());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.loginFragmentRelativeLayout);

        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePic);
        profilePictureView.setCropped(true);

        userName = (TextView) findViewById(R.id.userName);

        Resources resources = getResources();

        breathingScoresSeries = new GraphViewSeries(
                "Breathing Scores", new GraphViewSeries.GraphViewSeriesStyle(
                Color.rgb(20, 20, 255), 5), new GraphView.GraphViewData[] {});

        GraphView breathingGraphView = new BarGraphView(getContext(), "Breathing Scores");
        styleGraph(breathingGraphView, resources);
        breathingGraphView.addSeries(breathingScoresSeries);

        GraphViewSeriesStyle stressSeriesStyle = new GraphViewSeriesStyle(resources.getColor(R.color.red), 1);
        stressScoresSeries = new GraphViewSeries(
                "Stress Scores", stressSeriesStyle, new GraphView.GraphViewData[] {});

        GraphView stressGraphView = new BarGraphView(getContext(), "Stress Scores");
        styleGraph(stressGraphView, resources);
        stressGraphView.addSeries(stressScoresSeries);

        layout.addView(stressGraphView, setupLayoutParams());

        model.addListener(UserModel.ChangeEvent.FB_USER_NAME_CHANGE, fbUserInfoListener);
        model.addListener(UserModel.ChangeEvent.BREATHING_SCORE_ADDED, breathingScoresListener);
        model.addListener(UserModel.ChangeEvent.STRESS_SCORE_ADDED, stressScoresListener);

        loadBreathingScores();
        loadStressScores();
        updateFbUserInfo();
        //updateBreathingScores();
        //updateStressScores();
    }

    private void styleGraph(GraphView graphView, Resources resources) {
        graphView.setScrollable(true);
        graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return "";
                }
                return null;
            }
        });
        graphView.getGraphViewStyle().setGridColor(resources.getColor(R.color.grid));
        graphView.getGraphViewStyle().setVerticalLabelsColor(resources.getColor(R.color.graph_vertical_labels));
        graphView.getGraphViewStyle().setNumHorizontalLabels(1);
        graphView.getGraphViewStyle().setNumVerticalLabels(4);
        graphView.getGraphViewStyle().setVerticalLabelsWidth(50);
    }

    private ViewGroup.LayoutParams setupLayoutParams() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.loginDivider);

        return layoutParams;
    }

    private EventListener fbUserInfoListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            updateFbUserInfo();
        }
    };

    private EventListener breathingScoresListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            updateBreathingScores();
        }
    };

    private EventListener stressScoresListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            updateStressScores();
        }
    };

    public void destroy() {
        model.removeListener(UserModel.ChangeEvent.FB_USER_NAME_CHANGE, fbUserInfoListener);
        model.removeListener(UserModel.ChangeEvent.BREATHING_SCORE_ADDED, breathingScoresListener);
        model.removeListener(UserModel.ChangeEvent.STRESS_SCORE_ADDED, stressScoresListener);
    }
}
