package com.relaxisapp.relaxis.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.relaxisapp.relaxis.R;
import com.relaxisapp.relaxis.events.Event;
import com.relaxisapp.relaxis.events.EventListener;
import com.relaxisapp.relaxis.models.UserModel;
import com.relaxisapp.relaxis.widgets.BreathingScoreResultsListAdapter;
import com.relaxisapp.relaxis.widgets.StressScoreResultsListAdapter;

public class UserView extends RelativeLayout {

    private ProfilePictureView profilePictureView;
    private TextView userName;

    private TextView breathingScoreListDesc;
    private TextView stressScoreListDesc;

    private BreathingScoreResultsListAdapter breathingScoreResultsListAdapter;
    private ListView breathingScoreResultsListView;

    private StressScoreResultsListAdapter stressScoreResultsListAdapter;
    private ListView stressScoreResultsListView;

    private UserModel model;

    public UserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        model = UserModel.getInstance();
    }

    public void toggleViewsVisibility(int visibility) {
        userName.setVisibility(visibility);
        profilePictureView.setVisibility(visibility);
        breathingScoreResultsListView.setVisibility(visibility);
        stressScoreResultsListView.setVisibility(visibility);
        breathingScoreListDesc.setVisibility(visibility);
        stressScoreListDesc.setVisibility(visibility);
    }

    private void updateFbUserInfo() {
        Log.i("UPDATEFBUSERINFO", model.getFbUserName());
        userName.setText(model.getFbUserName());
        profilePictureView.setProfileId(model.getFbUserId());
    }

    private void updateBreathingScores() {
        // run it on the UI thread
        this.post(new Runnable() {
            @Override
            public void run() {
                if (model.getBreathingScores() != null) {
                    breathingScoreResultsListAdapter = new BreathingScoreResultsListAdapter(
                            getContext(),
                            model.getBreathingScores());

                    breathingScoreResultsListView.setAdapter(breathingScoreResultsListAdapter);
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
                    stressScoreResultsListAdapter = new StressScoreResultsListAdapter(
                            getContext(),
                            model.getStressScores());

                    stressScoreResultsListView.setAdapter(stressScoreResultsListAdapter);
                };
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePic);
        profilePictureView.setCropped(true);

        userName = (TextView) findViewById(R.id.userName);

        stressScoreResultsListView = (ListView) findViewById(R.id.stressScoreResults);
        breathingScoreResultsListView = (ListView) findViewById(R.id.breathingScoreResults);

        breathingScoreListDesc = (TextView) findViewById(R.id.breathingScoreListDesc);
        stressScoreListDesc = (TextView) findViewById(R.id.stressScoreListDesc);

        model.addListener(UserModel.ChangeEvent.FB_USER_NAME_CHANGE, fbUserInfoListener);
        model.addListener(UserModel.ChangeEvent.BREATHING_SCORES_CHANGE, breathingScoresListener);
        model.addListener(UserModel.ChangeEvent.STRESS_SCORES_CHANGE, stressScoresListener);

        updateFbUserInfo();
        updateBreathingScores();
        updateStressScores();
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
