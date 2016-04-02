package com.vijaysy.boomerang.utils;


import com.vijaysy.boomerang.models.RetryItem;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by vijaysy on 02/04/16.
 */
public class JerseyClient {
    private Client client;
    private RetryItem retryItem;
    private Response response;

    public JerseyClient(RetryItem retryItem){
        this.retryItem=retryItem;
        this.client= ClientBuilder.newClient();
    }

    //TODO: need to check overload in creation of WebTarget and Client

    public boolean execute(){
        WebTarget webTarget = client.target(retryItem.getHttpUri());
        System.out.printf("Execute: "+retryItem.getMessageId());
        switch (retryItem.getHttpMethod()){
            case POST: webTarget.request().post(Entity.entity(new JSONObject(retryItem.getMessage()), MediaType.APPLICATION_JSON_TYPE+ ";charset=UTF-8"));
                break;
            case GET: webTarget.request().buildGet().invoke();
                break;
            case PUT: webTarget.request().put(Entity.entity(new JSONObject(retryItem.getMessage()), MediaType.APPLICATION_JSON_TYPE+ ";charset=UTF-8"));
                break;
            default: return false;
        }
      return true;

    }

    public boolean executeFallBack() {
        WebTarget webTarget = client.target(retryItem.getfHttpUri());
        System.out.printf("Execute FallBack : "+retryItem.getMessageId());
        switch (retryItem.getfHttpMethod()){
            case POST:response=webTarget.request().post(Entity.entity(new JSONObject(retryItem.getMessage()), MediaType.APPLICATION_JSON_TYPE+ ";charset=UTF-8"));
                break;
            case GET:response=webTarget.request().buildGet().invoke();
                break;
            case PUT: response = webTarget.request().put(Entity.entity(new JSONObject(retryItem.getMessage()), MediaType.APPLICATION_JSON_TYPE+ ";charset=UTF-8"));
                break;
            default:return false;
        }
       return  (Response.Status.Family.familyOf(response.getStatus())== Response.Status.Family.SERVER_ERROR)?false:true;
    }


}
