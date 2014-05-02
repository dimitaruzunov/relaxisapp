package com.relaxisapp.relaxis.vos;

/**
 * Created by zdravko on 14-5-2.
 */
public class HomeVo extends SimpleObservable<HomeVo> {

    private int heartRate;
    public int getHeartRate() {
        return heartRate;
    }
    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
        notifyObservers(this);
    }

    private int rrInterval;
    public int getRrInterval() {
        return rrInterval;
    }
    public void setRrInterval(int rrInterval) {
        this.rrInterval = rrInterval;
        notifyObservers(this);
    }

    private int instantHeartRate;
    public int getInstantHeartRate() {
        return instantHeartRate;
    }
    public void setInstantHeartRate(int instantHeartRate) {
        this.instantHeartRate = instantHeartRate;
        notifyObservers(this);
    }

    private int instantSpeed;
    public int getInstantSpeed() {
        return  instantSpeed;
    }
    public void setInstantSpeed(int instantSpeed) {
        this.instantSpeed = instantSpeed;
        notifyObservers(this);
    }

    synchronized public HomeVo clone() {
        HomeVo vo = new HomeVo();
        vo.setHeartRate(heartRate);
        vo.setRrInterval(rrInterval);
        vo.setInstantHeartRate(instantHeartRate);
        vo.setInstantSpeed(instantSpeed);
        return vo;
    }

    synchronized public void consume(HomeVo vo) {
        this.heartRate = vo.getHeartRate();
        this.rrInterval = vo.getRrInterval();
        this.instantHeartRate = vo.getInstantHeartRate();
        this.instantSpeed = vo.getInstantSpeed();
        notifyObservers(this);
    }
}
