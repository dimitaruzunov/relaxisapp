package com.relaxisapp.relaxis.models;

import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.relaxisapp.relaxis.events.EventDispatcher;
import com.relaxisapp.relaxis.events.SimpleEvent;
import com.relaxisapp.relaxis.utils.Const;

import java.util.ArrayList;

public class BreathingModel extends EventDispatcher {

    public static class ChangeEvent extends SimpleEvent {
        public static final String STARTED_STATE_CHANGED = "startedStateChanged";
        public static final String GRAPH_UPDATE_STARTED_STATE_CHANGED = "graphUpdateStartedStateChanged";
        public static final String TIME_LEFT_CHANGED = "timeLeftChanged";
        public static final String SCORE_CHANGED = "scoreChanged";
        public static final String IDEAL_GRAPH_DATA_ADDED = "idealGraphDataAdded";
        public static final String HR_GRAPH_DATA_ADDED = "hrGraphDataAdded";

        public ChangeEvent(String type) {
            super(type);
        }
    }

    private static BreathingModel instance;

    public static BreathingModel getInstance() {
        if (instance == null) instance = new BreathingModel();
        return instance;
    }

    private boolean startedState = false;
    public boolean getStartedState() {
        return startedState;
    }
    public void setStartedState(boolean startedState) {
        this.startedState = startedState;
        notifyChange(ChangeEvent.STARTED_STATE_CHANGED);
    }

    private boolean graphUpdateStartedState = false;
    public boolean getGraphUpdateStartedState() {
        return graphUpdateStartedState;
    }
    public void setGraphUpdateStartedState(boolean graphUpdateStartedState) {
        this.graphUpdateStartedState = graphUpdateStartedState;
        notifyChange(ChangeEvent.GRAPH_UPDATE_STARTED_STATE_CHANGED);
    }

    private int timeLeft = Const.TIME_BREATHING_SECONDS;
    public int getTimeLeft() {
        return timeLeft;
    }
    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
        notifyChange(ChangeEvent.TIME_LEFT_CHANGED);
    }

    private int score = 0;
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
        notifyChange(ChangeEvent.SCORE_CHANGED);
    }

    private ArrayList<GraphView.GraphViewData> idealGraphData = new ArrayList<GraphView.GraphViewData>();
    public GraphView.GraphViewData getLastIdealGraphData() {
        return idealGraphData.get(idealGraphData.size()-1);
    }
    public GraphView.GraphViewData[] getIdealGraphData() {
        GraphView.GraphViewData[] data = idealGraphData.toArray(new GraphView.GraphViewData[0]);
        return data;
    }
    public void addIdealGraphData(GraphView.GraphViewData data) {
        if (idealGraphData.size() > Const.VIEWPORT_WIDTH * Const.TIMER_TICKS_PER_SECOND) {
            idealGraphData.remove(0);
        }
        idealGraphData.add(data);
        notifyChange(ChangeEvent.IDEAL_GRAPH_DATA_ADDED);
    }

    private ArrayList<GraphView.GraphViewData> hrGraphData = new ArrayList<GraphView.GraphViewData>();
    public GraphView.GraphViewData getLastHrGraphData() {
        return hrGraphData.get(hrGraphData.size()-1);
    }
    public GraphView.GraphViewData[] getHrGraphData() {
        GraphView.GraphViewData[] data = hrGraphData.toArray(new GraphView.GraphViewData[0]);
        return data;
    }
    public void addHrGraphData(GraphView.GraphViewData data) {
        if (hrGraphData.size() > Const.VIEWPORT_WIDTH + 1) {
            hrGraphData.remove(0);
        }
        hrGraphData.add(data);
        notifyChange(ChangeEvent.HR_GRAPH_DATA_ADDED);
    }

    private void notifyChange(String type) {
        dispatchEvent(new ChangeEvent(type));
    }
}
