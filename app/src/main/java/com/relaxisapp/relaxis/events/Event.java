package com.relaxisapp.relaxis.events;

/**
 * Created by zdravko on 14-5-2.
 */
public interface Event {

    public String getType();
    public Object getSource();
    public void setSource(Object source);
}
