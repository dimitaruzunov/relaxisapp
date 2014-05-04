package com.relaxisapp.relaxis.models;

import com.relaxisapp.relaxis.events.EventDispatcher;
import com.relaxisapp.relaxis.events.SimpleEvent;

import java.util.ArrayList;

public class UserModel extends EventDispatcher {

    public static class ChangeEvent extends SimpleEvent {
        public static String FB_USER_ID_CHANGE = "fbUserIdChange";
        public static String FB_USER_NAME_CHANGE = "fbUserNameChange";
        public static String USER_ID_CHANGE = "userIdChange";
        public static String BREATHING_SCORES_CHANGE = "breathingScoresChange";
        public static String STRESS_SCORES_CHANGE = "stressScoresChange";
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

    private String fbUserId;
    public String getFbUserId() {
        return fbUserId;
    }
    public void setFbUserId(String fbUserId) {
        this.fbUserId = fbUserId;
        notifyChange(ChangeEvent.FB_USER_ID_CHANGE);
    }

    private String fbUserName;
    public String getFbUserName() {
        return fbUserName;
    }
    public void setFbUserName(String fbUserName) {
        this.fbUserName = fbUserName;
        notifyChange(ChangeEvent.FB_USER_NAME_CHANGE);
    }

    private int userId = 0;
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
        notifyChange(ChangeEvent.USER_ID_CHANGE);
    }

    private ArrayList<BreathingScore> breathingScores = null;
    public ArrayList<BreathingScore> getBreathingScores() {
        return breathingScores;
    }
    public void setBreathingScores(BreathingScore[] breathingScores) {
        this.breathingScores = new ArrayList<BreathingScore>();
        for (BreathingScore score : breathingScores) {
            this.breathingScores.add(score);
        }
        notifyChange(ChangeEvent.BREATHING_SCORES_CHANGE);
    }

    private ArrayList<StressScore> stressScores = null;
    public ArrayList<StressScore> getStressScores() {
        return stressScores;
    }
    public void setStressScores(StressScore[] stressScores) {
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
