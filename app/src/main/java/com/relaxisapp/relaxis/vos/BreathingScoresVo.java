package com.relaxisapp.relaxis.vos;

/**
 * Created by zdravko on 14-5-1.
 */
public class BreathingScoresVo extends SimpleObservable<BreathingScoresVo> {

    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
        notifyObservers(this);
    }

    synchronized public BreathingScoresVo clone() {
        BreathingScoresVo vo = new BreathingScoresVo();
        vo.setId(id);
        return vo;
    }

    synchronized public void consume(BreathingScoresVo vo) {
        this.id = vo.getId();
        notifyObservers(this);
    }
}
