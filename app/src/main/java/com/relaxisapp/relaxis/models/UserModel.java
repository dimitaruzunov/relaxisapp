package com.relaxisapp.relaxis.models;

import com.jjoe64.graphview.GraphView;
import com.relaxisapp.relaxis.events.EventDispatcher;
import com.relaxisapp.relaxis.events.SimpleEvent;

import java.util.ArrayList;

public class UserModel extends EventDispatcher {

    public static class ChangeEvent extends SimpleEvent {
        public static final String FB_USER_ID_CHANGE = "fbUserIdChange";
        public static final String FB_USER_NAME_CHANGE = "fbUserNameChange";
        public static final String USER_ID_CHANGE = "userIdChange";
        public static final String BREATHING_SCORE_ADDED = "breathingScoresAdded";
        public static final String BREATHING_SCORES_RESET = "breathingScoresReset";
        public static final String STRESS_SCORE_ADDED = "stressScoresAdded";
        public static final String STRESS_SCORES_RESET = "stressScoresReset";
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

    private ArrayList<GraphView.GraphViewData> breathingScores = new ArrayList<GraphView.GraphViewData>();
    public synchronized GraphView.GraphViewData getLastBreathingScore() {
        if (breathingScores.size() > 0) {
            return breathingScores.get(breathingScores.size() - 1);
        }
        return null;
    }
    public synchronized GraphView.GraphViewData[] getBreathingScores() {
        GraphView.GraphViewData[] data = breathingScores.toArray(new GraphView.GraphViewData[0]);
        return data;
    }
    public synchronized void addBreathingScore(int score) {
        if (breathingScores.size() > 10) {
            breathingScores.remove(0);
        }
        breathingScores.add(new GraphView.GraphViewData(breathingScores.size(), score));
        notifyChange(ChangeEvent.BREATHING_SCORE_ADDED);
    }
    public synchronized void setBreathingScores(BreathingScore[] breathingScores) {
        this.breathingScores = new ArrayList<GraphView.GraphViewData>();
        int itemsToAddCount = (breathingScores.length > 10) ? 10 : breathingScores.length;
        for (int i = itemsToAddCount - 1; i >= 0; i--) {
            this.breathingScores.add(new GraphView.GraphViewData(breathingScores.length, breathingScores[i].getScore()));
        }
        notifyChange(ChangeEvent.BREATHING_SCORES_RESET);
    }

    private ArrayList<GraphView.GraphViewData> stressScores = new ArrayList<GraphView.GraphViewData>();
    public synchronized GraphView.GraphViewData getLastStressScore() {
        if (stressScores.size() > 0) {
            return stressScores.get(stressScores.size() - 1);
        }
        return null;
    }
    public synchronized GraphView.GraphViewData[] getStressScores() {
        GraphView.GraphViewData[] data = stressScores.toArray(new GraphView.GraphViewData[0]);
        return data;
    }
    public synchronized void addStressScore(double score) {
        if (stressScores.size() > 10) {
            stressScores.remove(0);
        }
        stressScores.add(new GraphView.GraphViewData(stressScores.size(), score));
        notifyChange(ChangeEvent.STRESS_SCORE_ADDED);
    }
    public synchronized void setStressScores(StressScore[] stressScores) {
        this.stressScores = new ArrayList<GraphView.GraphViewData>();
        int itemsToAddCount = (stressScores.length > 10) ? 10 : stressScores.length;
        for (int i = itemsToAddCount - 1; i >= 0; i--) {
            this.stressScores.add(new GraphView.GraphViewData(stressScores.length, stressScores[i].getScore()));
        }
        notifyChange(ChangeEvent.STRESS_SCORES_RESET);
    }

    private void notifyChange(String type) {
        dispatchEvent(new ChangeEvent(type));
    }
}
