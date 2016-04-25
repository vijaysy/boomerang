package com.vijaysy.boomerang.enums;

/**
 * Created by vijaysy on 20/04/16.
 */
public enum  FallBackReasons {
    MAX_RETRY ("Retry count crossed"),NOT_RETRY_STATUS_CODE("Retry status code did not matched"),SUCCESSFULL("20X ok");
    private String fallBackReason;
    FallBackReasons(String msg) {
        this.fallBackReason=msg;
    }

    @Override
    public String toString(){ return fallBackReason;}

}
