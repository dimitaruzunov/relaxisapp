package com.relaxisapp.relaxis.vos;

import java.util.ArrayList;

/**
 * Created by zdravko on 14-5-1.
 */
public class SimpleObservable<T> implements EasyObservable<T> {

    private final ArrayList<OnChangeListener<T>> listeners = new ArrayList<OnChangeListener<T>>();

    @Override
    public void addListener(OnChangeListener<T> listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(OnChangeListener<T> listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    protected void notifyObservers(final T model) {
        synchronized (listeners) {
            for (OnChangeListener<T> listener : listeners) {
                listener.onChange(model);
            }
        }
    }
}
