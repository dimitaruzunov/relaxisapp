package com.relaxisapp.relaxis.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.relaxisapp.relaxis.R;
import com.relaxisapp.relaxis.events.Event;
import com.relaxisapp.relaxis.events.EventListener;
import com.relaxisapp.relaxis.models.UserModel;

public class UserView extends ScrollView {

    private ProfilePictureView profilePictureView;
    private TextView userName;

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
                    // update breathing scores
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
                    // stressScoresSeries.appendData(model.getStressScores(), false, 5);
                };
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

        stressScoresSeries = new GraphViewSeries(
                "Stress Scores", new GraphViewSeries.GraphViewSeriesStyle(
                Color.rgb(20, 20, 255), 5), new GraphView.GraphViewData[] {});

        GraphView graphView = new BarGraphView(getContext(), "Stress Scores");
        graphView.setScrollable(true);
        graphView.setScalable(true);
        graphView.addSeries(stressScoresSeries);

        layout.addView(graphView, setupLayoutParams());

        model.addListener(UserModel.ChangeEvent.FB_USER_NAME_CHANGE, fbUserInfoListener);
        model.addListener(UserModel.ChangeEvent.BREATHING_SCORES_CHANGE, breathingScoresListener);
        model.addListener(UserModel.ChangeEvent.STRESS_SCORES_CHANGE, stressScoresListener);

        updateFbUserInfo();
        updateBreathingScores();
        updateStressScores();
    }

    private ViewGroup.LayoutParams setupLayoutParams() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.stressScoreResults);

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
        model.removeListener(UserModel.ChangeEvent.BREATHING_SCORES_CHANGE, breathingScoresListener);
        model.removeListener(UserModel.ChangeEvent.STRESS_SCORES_CHANGE, stressScoresListener);
    }
}
