package com.vijaysy.boomerang.models;

import com.sun.istack.internal.NotNull;
import com.vijaysy.boomerang.enums.HttpMethod;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by vijaysy on 01/04/16.
 */
@Entity
@ToString
@Table(name="retry_item",indexes ={
        @Index(columnList = "message_id", name = "message_id")
})
public class RetryItem implements Serializable{

    //TODO: need to add headers,timestamps
    // TODO: 04/04/16  use primitive datatypes

    public RetryItem(){}

    public RetryItem(String messageId, String message , HttpMethod httpMethod, String httpUri, Integer nextRetry, Integer[] retryPattern,String fHttpUri,HttpMethod fHttpMethod){
        this.messageId=messageId;
        this.message=message;
        this.httpMethod=httpMethod;
        this.httpUri=httpUri;
        this.nextRetry=nextRetry;
        setRetryPattern(Arrays.toString(retryPattern).replaceAll("\\[|\\]||\\s", "").replace(" ",""));
        setMaxRetry(retryPattern.length);
        this.fHttpMethod=fHttpMethod;
        this.fHttpUri=fHttpUri;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true)
    private Integer id;

    @Column(name = "message_id",unique = true)
    @NotNull
    private String messageId;

    @Column(name = "message")
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

    public String getMessageId() {
        return messageId;
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

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Objects.isNull(o)) return false;
        if (!(o instanceof RetryItem)) return false;

        RetryItem retryItem = (RetryItem) o;

        if (!getMessageId().equals(retryItem.getMessageId())) return false;
        return getMessage() != null ? getMessage().equals(retryItem.getMessage()) : retryItem.getMessage() == null;

    }

    @Override
    public int hashCode() {
        int result = getMessageId().hashCode();
        result = 31 * result + (getMessage() != null ? getMessage().hashCode() : 0);
        return result;
    }
}
