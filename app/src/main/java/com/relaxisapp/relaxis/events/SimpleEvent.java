package com.relaxisapp.relaxis.events;

/**
 * Created by zdravko on 14-5-2.
 */
public class SimpleEvent implements Event {

    private String type;
    @Override public String getType() { return type; }
    public void setType(String type) {
        this.type = type;
    }

    protected Object source;
    @Override public Object getSource() {
        return source;
    }
    @Override public void setSource(Object source) {
        this.source = source;
    }

    public SimpleEvent(String type) {
        this.type = type;
    }
}
