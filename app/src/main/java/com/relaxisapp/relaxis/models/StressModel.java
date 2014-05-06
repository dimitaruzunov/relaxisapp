package com.relaxisapp.relaxis.models;

import com.relaxisapp.relaxis.events.EventDispatcher;
import com.relaxisapp.relaxis.events.SimpleEvent;

public class StressModel extends EventDispatcher {

    public static class ChangeEvent extends SimpleEvent {
        public static final String STRESS_LEVEL_CHANGED = "stressLevelChanged";

        public ChangeEvent(String type) {
            super(type);
        }
    }

    private static StressModel instance;
    public static StressModel getInstance() {
        if (instance == null) {
            instance = new StressModel();
        }
        return instance;
    }

    private double stressLevel = 0;
    public double getStressLevel() {
        return stressLevel;
    }
    public void setStressLevel(double stressLevel) {
        this.stressLevel = stressLevel;
        notifyChange(ChangeEvent.STRESS_LEVEL_CHANGED);
    }

    private void notifyChange(String type) {
        dispatchEvent(new ChangeEvent(type));
    }

}
