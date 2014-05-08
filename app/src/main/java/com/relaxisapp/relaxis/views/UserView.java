package com.relaxisapp.relaxis.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.relaxisapp.relaxis.R;
import com.relaxisapp.relaxis.events.Event;
import com.relaxisapp.relaxis.events.EventListener;
import com.relaxisapp.relaxis.models.UserModel;

public class UserView extends ScrollView {

    private ProfilePictureView profilePictureView;
    private TextView userName;
    private View loginDivider;

    private static GraphViewSeries breathingScoresSeries;
    private static GraphViewSeries stressScoresSeries;
    private GraphView breathingGraphView;
    private GraphView stressGraphView;

    private UserModel model;

    public UserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        model = UserModel.getInstance();
    }

    public void toggleViewsVisibility(int visibility) {
        userName.setVisibility(visibility);
        profilePictureView.setVisibility(visibility);
        loginDivider.setVisibility(visibility);
        breathingGraphView.setVisibility(visibility);
        stressGraphView.setVisibility(visibility);
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
                    breathingScoresSeries.appendData(model.getLastBreathingScore(), false, 10);
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
                    stressScoresSeries.appendData(model.getLastStressScore(), false, 10);
                };
            }
        });
    }

    private void loadBreathingScores() {
        // run it on the UI thread
        this.post(new Runnable() {
            @Override
            public void run() {
                breathingScoresSeries.resetData(model.getBreathingScores());
            }
        });
    }

    private void loadStressScores() {
        // run it on the UI thread
        this.post(new Runnable() {
            @Override
            public void run() {
                stressScoresSeries.resetData(model.getStressScores());
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.loginFragmentRelativeLayout);

        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePic);
        profilePictureView.setCropped(true);

        userName = (TextView) findViewById(R.id.userName);

        loginDivider = findViewById(R.id.loginDivider);

        Resources resources = getResources();

        GraphViewSeriesStyle breathingSeriesStyle = new GraphViewSeriesStyle(resources.getColor(R.color.red), 1);
        breathingScoresSeries = new GraphViewSeries(
                "Breathing Scores", breathingSeriesStyle, new GraphView.GraphViewData[] {});
        breathingGraphView = new BarGraphView(getContext(), "Breathing Scores");
        breathingGraphView.setId(1);
        styleGraph(breathingGraphView, resources);
        breathingGraphView.addSeries(breathingScoresSeries);

        GraphViewSeriesStyle stressSeriesStyle = new GraphViewSeriesStyle(resources.getColor(R.color.red), 1);
        stressScoresSeries = new GraphViewSeries(
                "Stress Scores", stressSeriesStyle, new GraphView.GraphViewData[] {});
        stressGraphView = new BarGraphView(getContext(), "Stress Scores");
        styleGraph(stressGraphView, resources);
        stressGraphView.addSeries(stressScoresSeries);

        layout.addView(breathingGraphView, setupBreathingGraphPosition());
        layout.addView(stressGraphView, setupStressGraphPosition());

        model.addListener(UserModel.ChangeEvent.FB_USER_NAME_CHANGE, fbUserInfoListener);
        model.addListener(UserModel.ChangeEvent.BREATHING_SCORE_ADDED, breathingScoresAddListener);
        model.addListener(UserModel.ChangeEvent.STRESS_SCORE_ADDED, stressScoresAddListener);
        model.addListener(UserModel.ChangeEvent.BREATHING_SCORES_RESET, breathingScoresResetListener);
        model.addListener(UserModel.ChangeEvent.STRESS_SCORES_RESET, stressScoresResetListener);

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
        graphView.getGraphViewStyle().setVerticalLabelsColor(resources.getColor(R.color.text_dark));
        graphView.getGraphViewStyle().setHorizontalLabelsColor(resources.getColor(R.color.text_dark));
        graphView.getGraphViewStyle().setNumHorizontalLabels(1);
        graphView.getGraphViewStyle().setNumVerticalLabels(4);
        graphView.getGraphViewStyle().setVerticalLabelsWidth(50);
    }

    private ViewGroup.LayoutParams setupBreathingGraphPosition() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.loginDivider);

        return layoutParams;
    }

    private ViewGroup.LayoutParams setupStressGraphPosition() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        layoutParams.addRule(RelativeLayout.BELOW, breathingGraphView.getId());

        return layoutParams;
    }

    private EventListener fbUserInfoListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            updateFbUserInfo();
        }
    };

    private EventListener breathingScoresResetListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            loadBreathingScores();
        }
    };

    private EventListener stressScoresResetListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            loadStressScores();
        }
    };

    private EventListener breathingScoresAddListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
//            updateBreathingScores();
            loadBreathingScores();
        }
    };

    private EventListener stressScoresAddListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
//            updateStressScores();
            loadStressScores();
        }
    };

    public void destroy() {
        model.removeListener(UserModel.ChangeEvent.FB_USER_NAME_CHANGE, fbUserInfoListener);
        model.removeListener(UserModel.ChangeEvent.BREATHING_SCORE_ADDED, breathingScoresAddListener);
        model.removeListener(UserModel.ChangeEvent.STRESS_SCORE_ADDED, stressScoresAddListener);
        model.removeListener(UserModel.ChangeEvent.BREATHING_SCORES_RESET, breathingScoresResetListener);
        model.removeListener(UserModel.ChangeEvent.STRESS_SCORES_RESET, stressScoresResetListener);
    }
}
