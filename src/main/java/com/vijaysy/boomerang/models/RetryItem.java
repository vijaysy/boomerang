package com.vijaysy.boomerang.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vijaysy.boomerang.enums.FallBackReasons;
import com.vijaysy.boomerang.enums.HttpMethod;
import com.vijaysy.boomerang.exception.InvalidRetryItemException;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * Created by vijaysy on 01/04/16.
 */
@Slf4j
@Entity
@ToString
public class RetryItem implements Serializable{

    public RetryItem(){}

    public RetryItem(String messageId, String message , HttpMethod httpMethod, String httpUri, int nextRetry, int[] retryPattern,String fHttpUri, String channel,String headers,Boolean needResponse){
        this.messageId=messageId;
        this.message=message;
        this.httpMethod=httpMethod;
        this.httpUri=httpUri;
        this.nextRetry=nextRetry;
        this.retryPattern=Arrays.toString(retryPattern).replaceAll("\\[|\\]||\\s", "").replace(" ","");
        this.maxRetry=retryPattern.length;
        this.fallbackHttpUri =fHttpUri;
        this.channel=channel;
        this.headers=headers;
        this.needResponse=needResponse;

    }

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true)
    private int id;

    @JsonProperty
    @Column(name = "message_id",unique = true)
    @NotNull
    private String messageId;

    @JsonProperty
    @Column(name = "message")
    private String message;

    @JsonProperty
    @Column(name = "response")
    private String response;

    @JsonProperty
    @Enumerated(EnumType.STRING)
    @Column(name = "http_method")
    private HttpMethod httpMethod;

    @JsonProperty
    @Column(name = "http_uri")
    private String httpUri;

    @JsonProperty
    @Column(name = "retry_pattern")
    private String retryPattern;

    @JsonProperty
    @Column(name = "next_retry")
    private int nextRetry;

    @JsonProperty
    @Column(name = "max_retry")
    private int maxRetry;

    @JsonProperty
    @NotNull
    @Column(name = "channel")
    private String channel;

    @JsonProperty
    @NotNull
    @Column(name = "fallback_http_uri")
    private String fallbackHttpUri;


    @JsonProperty
    @Column(name = "headers")
    private String headers;

    @JsonProperty
    @NotNull
    @Column(name = "need_response")
    private Boolean needResponse;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;

    @JsonProperty
    @Column(name = "processed")
    private boolean processed=false;

    @JsonProperty
    @Column(name = "return_flag")
    private boolean returnFlag=true;

    @JsonProperty
    @Enumerated(EnumType.STRING)
    @Column(name = "fall_back_reason")
    private FallBackReasons fallBackReasons;


    public int getId() {
        return id;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getFallbackHttpUri() {
        return fallbackHttpUri;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public String[] getRetryPattern() {
        return retryPattern.split(",");
    }

    public int getNextRetry() {
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

    public String getChannel() {
        return channel;
    }

    public String getHeaders() {
        return headers;
    }

    public Boolean getNeedResponse() {
        return needResponse;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public boolean isProcessed() {
        return processed;
    }

    public boolean isReturnFlag() {
        return returnFlag;
    }

    public String getResponse() {
        return response;
    }

    public FallBackReasons getFallBackReasons() {
        return fallBackReasons;
    }

    public void setNextRetry(int nextRetry) {
        this.nextRetry = nextRetry;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public void setReturnFlag(boolean returnFlag) {
        this.returnFlag = returnFlag;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setFallBackReasons(FallBackReasons fallBackReasons) {
        this.fallBackReasons = fallBackReasons;
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


    public static class RetryItemBuilder {
        private String messageId;
        private String message;
        private HttpMethod httpMethod;
        private String httpUri;
        private int nextRetry;
        private int [] retryPattern;
        private String channel;
        private String fallbackHttpUri;
        private String headers;
        private Boolean needResponse;
        private MultivaluedHashMap<String, String> multivaluedHashMap = new MultivaluedHashMap<String, String>();


        public RetryItemBuilder withMessageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public RetryItemBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public RetryItemBuilder withHttpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public RetryItemBuilder withHttpUri(String httpUri) {
            this.httpUri = httpUri;
            return this;
        }

        public RetryItemBuilder withNextRetry(int nextRetry) {
            this.nextRetry = nextRetry;
            return this;
        }

        public RetryItemBuilder withRetryPattern(int[] retryPattern) {
            this.retryPattern = retryPattern;
            return this;
        }

        public RetryItemBuilder withFallbackHttpUri(String fallbackHttpUri) {
            this.fallbackHttpUri = fallbackHttpUri;
            return this;
        }

        public RetryItemBuilder withChannel(String channel) {
            this.channel = channel;
            return this;
        }

        public RetryItemBuilder withHeaders(String name , String value) {
            this.multivaluedHashMap.putSingle(name,value);
            return this;
        }


        public RetryItemBuilder withNeedResponse(Boolean needResponse) {
            this.needResponse = needResponse;
            return this;
        }

        public RetryItem createRetryItem() throws InvalidRetryItemException{
            final ObjectMapper objectMapper=new ObjectMapper();
            try {
                headers=objectMapper.writeValueAsString(multivaluedHashMap);
            }catch (JsonProcessingException e){
                log.error(e.toString());
            }

            if(messageId==null||messageId.length()==0)
                throw new InvalidRetryItemException("MessageId is null");
            if(httpMethod==null)
                throw new InvalidRetryItemException("HttpMethod is null");
            if(isValidURI(httpUri))
                throw new InvalidRetryItemException("HttpUri is null");
            if(retryPattern.length==0)
                throw new InvalidRetryItemException("RetryPattern is null");
            if(channel==null||channel.length()==0)
                throw new InvalidRetryItemException("Channel is null");
            if(isValidURI(fallbackHttpUri))
                throw new InvalidRetryItemException("FallbackHttpUri is null");

            return new RetryItem(messageId, message, httpMethod, httpUri, nextRetry, retryPattern, fallbackHttpUri, channel, headers, needResponse);
        }

        static boolean isValidURI(String uriStr) {
            try {
                URI uri = new URI(uriStr);
                return true;
            }
            catch (URISyntaxException e) {
                return false;
            }
        }

    }

}
