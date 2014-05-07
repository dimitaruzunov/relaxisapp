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

    private ArrayList<GraphView.GraphViewData> breathingScores = null;
    public synchronized GraphView.GraphViewData getLastBreathingScore() {
        return breathingScores.get(breathingScores.size()-1);
    }
    public synchronized GraphView.GraphViewData[] getBreathingScores() {
        GraphView.GraphViewData[] data = breathingScores.toArray(new GraphView.GraphViewData[0]);
        return data;
    }
    public synchronized void addBreathingScore(GraphView.GraphViewData data) {
//        if (breathingScores.size() > Const.VIEWPORT_WIDTH + 1) {
//            breathingScores.remove(0);
//        }
        breathingScores.add(data);
        notifyChange(ChangeEvent.BREATHING_SCORE_ADDED);
    }
    public synchronized void setBreathingScores(BreathingScore[] breathingScores) {
        this.breathingScores = new ArrayList<GraphView.GraphViewData>();
        for (BreathingScore score : breathingScores) {
            this.breathingScores.add(new GraphView.GraphViewData(breathingScores.length, score.getScore()));
        }
        notifyChange(ChangeEvent.BREATHING_SCORES_RESET);
    }

    private ArrayList<GraphView.GraphViewData> stressScores = null;
    public synchronized GraphView.GraphViewData getLastStressScore() {
        return stressScores.get(stressScores.size()-1);
    }
    public synchronized GraphView.GraphViewData[] getStressScores() {
        GraphView.GraphViewData[] data = stressScores.toArray(new GraphView.GraphViewData[0]);
        return data;
    }
    public synchronized void addStressScore(GraphView.GraphViewData data) {
//        if (stressScores.size() > Const.VIEWPORT_WIDTH + 1) {
//            stressScores.remove(0);
//        }
        stressScores.add(data);
        notifyChange(ChangeEvent.STRESS_SCORE_ADDED);
    }
    public synchronized void setStressScores(StressScore[] stressScores) {
        this.stressScores = new ArrayList<GraphView.GraphViewData>();
        for (StressScore score : stressScores) {
            this.stressScores.add(new GraphView.GraphViewData(stressScores.length, score.getScore()));
        }
        notifyChange(ChangeEvent.STRESS_SCORES_RESET);
    }

    private void notifyChange(String type) {
        dispatchEvent(new ChangeEvent(type));
    }
}
