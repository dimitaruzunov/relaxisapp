package com.relaxisapp.relaxis.events;

/**
 * Created by zdravko on 14-5-2.
 */
public interface Dispatcher {

    void addListener(String type, EventListener listener);
    void removeListener(String type, EventListener listener);
    boolean hasListener(String type, EventListener listener);
    void dispatchEvent(Event event);
}
