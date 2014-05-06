package com.relaxisapp.relaxis.models;

import com.relaxisapp.relaxis.events.EventDispatcher;
import com.relaxisapp.relaxis.events.SimpleEvent;

import java.util.ArrayList;

public class UserModel extends EventDispatcher {

    public static class ChangeEvent extends SimpleEvent {
        public static final String FB_USER_ID_CHANGE = "fbUserIdChange";
        public static final String FB_USER_NAME_CHANGE = "fbUserNameChange";
        public static final String USER_ID_CHANGE = "userIdChange";
        public static final String BREATHING_SCORES_CHANGE = "breathingScoresChange";
        public static final String STRESS_SCORES_CHANGE = "stressScoresChange";
        public ChangeEvent(String type) {
            super(type);
        }
    }

    private static UserModel instance;
    public static UserModel getInstance() {
        if (instance == null) {
            instance = new UserModel();
        }
        return instance;
    }

    private String fbUserId = "";
    public synchronized String getFbUserId() {
        return fbUserId;
    }
    public synchronized void setFbUserId(String fbUserId) {
        this.fbUserId = fbUserId;
        notifyChange(ChangeEvent.FB_USER_ID_CHANGE);
    }

    private String fbUserName = "";
    public synchronized String getFbUserName() {
        return fbUserName;
    }
    public synchronized void setFbUserName(String fbUserName) {
        this.fbUserName = fbUserName;
        notifyChange(ChangeEvent.FB_USER_NAME_CHANGE);
    }

    private int userId = 0;
    public synchronized int getUserId() {
        return userId;
    }
    public synchronized void setUserId(int userId) {
        this.userId = userId;
        notifyChange(ChangeEvent.USER_ID_CHANGE);
    }

    private ArrayList<BreathingScore> breathingScores = null;
    public synchronized ArrayList<BreathingScore> getBreathingScores() {
        return breathingScores;
    }
    public synchronized void setBreathingScores(BreathingScore[] breathingScores) {
        this.breathingScores = new ArrayList<BreathingScore>();
        for (BreathingScore score : breathingScores) {
            this.breathingScores.add(score);
        }
        notifyChange(ChangeEvent.BREATHING_SCORES_CHANGE);
    }

    private ArrayList<StressScore> stressScores = null;
    public synchronized ArrayList<StressScore> getStressScores() {
        return stressScores;
    }
    public synchronized void setStressScores(StressScore[] stressScores) {
        this.stressScores = new ArrayList<StressScore>();
        for (StressScore score : stressScores) {
            this.stressScores.add(score);
        }
        notifyChange(ChangeEvent.STRESS_SCORES_CHANGE);
    }

    private void notifyChange(String type) {
        dispatchEvent(new ChangeEvent(type));
    }
}
