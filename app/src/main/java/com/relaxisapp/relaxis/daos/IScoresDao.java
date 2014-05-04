package com.relaxisapp.relaxis.daos;

/**
 * Created by zdravko on 14-5-1.
 */
public interface IScoresDao <T> {

    // HTTP POST
    boolean create(T score);

    // HTTP GET - get all
    T[] read();

    // HTTP GET
    T read(int id);

    // HTTP PUT
    void update(T score);

    // HTTP DELETE
    void delete(T score);
}
