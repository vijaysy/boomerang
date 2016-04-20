package com.vijaysy.boomerang.enums;

/**
 * Created by vijaysy on 01/04/16.
 */
public enum  HttpMethod {
    POST ("POST"), PUT("PUT") , GET("GET");
    private String method;
    HttpMethod(String method) {
        this.method=method;
    }

    @Override
    public String toString() {
        return method;
    }
}
