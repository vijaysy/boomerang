package com.vijaysy.boomerang.exception;

import lombok.ToString;

/**
 * Created by vijaysy on 22/04/16.
 */
@ToString
public class DBException extends Exception {

    private final Exception e;

    public DBException(String msg, Exception e) {
        super(msg);
        this.e = e;
    }
}
