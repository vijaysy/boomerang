package com.vijaysy.boomerang.models;

import com.vijaysy.boomerang.enums.HttpMethod;

import javax.persistence.*;
import java.util.Arrays;

/**
 * Created by vijay.yala on 01/04/16.
 */
@Entity
@Table(name="retry_item")
public class RetryItem {

    public RetryItem(){}

    public RetryItem(String message , HttpMethod httpMethod, String httpUri, Integer nextRetry, Integer[] retryPattern,String fHttpUri,HttpMethod fHttpMethod){
        this.message=message;
        this.httpMethod=httpMethod;
        this.httpUri=httpUri;
        this.nextRetry=nextRetry;
        setRetryPattern(Arrays.toString(retryPattern).replaceAll("\\[|\\]||\\s", ""));
        setMaxRetry(retryPattern.length);
        this.fHttpMethod=fHttpMethod;
        this.fHttpUri=fHttpUri;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "mesasge")
    private String message;

    @Column(name = "http_method")
    private HttpMethod httpMethod;

    @Column(name = "http_uri")
    private String httpUri;

    @Column(name = "next_retry")
    private Integer nextRetry;

    @Column(name = "retry_pattern")
    private String retryPattern;

    @Column(name = "max_retry")
    private Integer maxRetry;

    //Post max retry

    @Column(name = "f_http_uri")
    private String fHttpUri;

    @Column(name = "f_http_method")
    private HttpMethod fHttpMethod;


    public Integer getId() {
        return id;
    }

    public HttpMethod getfHttpMethod() {
        return fHttpMethod;
    }

    public String getfHttpUri() {
        return fHttpUri;
    }

    public Integer getMaxRetry() {
        return maxRetry;
    }

    public String[] getRetryPattern() {
        return retryPattern.split(",");
    }

    public Integer getNextRetry() {
        return nextRetry;
    }

    public String getHttpUri() {
        return httpUri;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getMessage() {
        return message;
    }

    public void setfHttpMethod(HttpMethod fHttpMethod) {
        this.fHttpMethod = fHttpMethod;
    }

    public void setfHttpUri(String fHttpUri) {
        this.fHttpUri = fHttpUri;
    }

    public void setMaxRetry(Integer maxRetry) {
        this.maxRetry = maxRetry;
    }

    public void setRetryPattern(String retryPattern) {
        this.retryPattern = retryPattern;
    }

    public void setNextRetry(Integer nextRetry) {
        this.nextRetry = nextRetry;
    }

    public void setHttpUri(String httpUri) {
        this.httpUri = httpUri;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
