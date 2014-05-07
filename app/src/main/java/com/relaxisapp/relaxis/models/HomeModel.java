package com.relaxisapp.relaxis.models;

import com.relaxisapp.relaxis.events.EventDispatcher;
import com.relaxisapp.relaxis.events.SimpleEvent;

/**
 * Created by zdravko on 14-5-2.
 */
public class HomeModel extends EventDispatcher {

    public static class ChangeEvent extends SimpleEvent {
        public static final String CONNECTION_STATE_CHANGED = "connectionStateChanged";
        public static final String NN_COUNT_CHANGED = "nnCountChanged";
        public static final String HEART_RATE_CHANGED = "heartRateChanged";
        public static final String RR_INTERVAL_CHANGED = "rrIntervalChanged";
        public static final String INSTANT_HEART_RATE_CHANGED = "instantHeartRateChanged";
        public static final String INSTANT_SPEED_CHANGED = "instantSpeedChanged";
        public static final String MUSIC_PLAYED = "musicPlayed";

        public ChangeEvent(String type) {
            super(type);
        }
    }

    private HomeModel() {
        super();
    }

    private static HomeModel instance;

    public static HomeModel getInstance() {
        if (instance == null) instance = new HomeModel();
        return instance;
    }

    private int connectionState = 0;
    public int getConnectionState() {
        return connectionState;
    }
    public void setConnectionState(int connectionState) {
        this.connectionState = connectionState;
        notifyChange(ChangeEvent.CONNECTION_STATE_CHANGED);
    }

    public int nnCount = 0;
    public int getNnCount() {
        return nnCount;
    }
    public void setNnCount(int nnCount) {
        this.nnCount = nnCount;
        notifyChange(ChangeEvent.NN_COUNT_CHANGED);
    }

    private int heartRate = 0;
    public int getHeartRate() {
        return heartRate;
    }
    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
        notifyChange(ChangeEvent.HEART_RATE_CHANGED);
    }

    private int rrInterval = 0;
    public int getRrInterval() {
        return rrInterval;
    }
    public void setRrInterval(int rrInterval) {
        this.rrInterval = rrInterval;
        notifyChange(ChangeEvent.RR_INTERVAL_CHANGED);
    }

    private int instantHeartRate = 0;
    public int getInstantHeartRate() {
        return instantHeartRate;
    }
    public void setInstantHeartRate(int instantHeartRate) {
        this.instantHeartRate = instantHeartRate;
        notifyChange(ChangeEvent.INSTANT_HEART_RATE_CHANGED);
    }

    private double instantSpeed = 0.0;
    public double getInstantSpeed() {
        return instantSpeed;
    }
    public void setInstantSpeed(double instantSpeed) {
        this.instantSpeed = instantSpeed;
        notifyChange(ChangeEvent.INSTANT_SPEED_CHANGED);
    }

    private boolean musicPlayed = false;
    public boolean getMusicPlayed() {
        return musicPlayed;
    }
    public void setMusicPlayed(boolean musicPlayed) {
        this.musicPlayed = musicPlayed;
        notifyChange(ChangeEvent.MUSIC_PLAYED);
    }

    private void notifyChange(String type) {
        dispatchEvent(new ChangeEvent(type));
    }
}
