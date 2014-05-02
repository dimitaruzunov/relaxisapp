package com.relaxisapp.relaxis.vos;

/**
 * Created by zdravko on 14-5-1.
 */
public interface EasyObservable<T> {

    void addListener(OnChangeListener<T> listener);
    void removeListener(OnChangeListener<T> listener);
}
