package com.vijaysy.boomerang;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Created by vijay.yala on 05/04/16.
 */
public class Test {
    public static void main(String [] args){
        Client client= ClientBuilder.newClient();
        Response response;
        WebTarget webTarget = client.target("http://localhost:8080/mock/33/fallback");
        response=webTarget.request().put(Entity.text(""));
        System.out.printf(response.toString());
    }
}
