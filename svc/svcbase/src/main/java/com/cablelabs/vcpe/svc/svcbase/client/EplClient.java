/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.svc.svcbase.client;

import com.cablelabs.vcpe.svc.svcbase.model.Epl;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by steve on 6/8/15.
 */
public class EplClient {

    private Client client; // provided by Jersey

    public EplClient() {
        client = ClientBuilder.newClient();
    }

  //--------------------------------------------------------
    public Epl create(Epl epl)
  //--------------------------------------------------------
    {
        WebTarget target =client.target("http://localhost:9090/svcmgr/webapi/svc/");
        Response response = target.path("epl")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(epl, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200 ) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }
        return response.readEntity(Epl.class);
    }

  //--------------------------------------------------------
    public Epl update(Epl epl)
  //--------------------------------------------------------
    {
        WebTarget target =client.target("http://localhost:9090/svcmgr/webapi/svc/");
        Response response = target.path("epl/"+ epl.getId())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(epl, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200 ) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }
        return response.readEntity(Epl.class);
    }

  //--------------------------------------------------------
    public Epl get(String eplId)
  //--------------------------------------------------------
  // get Epl of specified ID

    {
        WebTarget target = client.target("http://localhost:9090/svcmgr/webapi/svc");

        Response response = target.path("epl/"+eplId).request(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }

        //return response;
        return response.readEntity(Epl.class);
    }

  //--------------------------------------------------------
    public List<Epl> getAll()
  //--------------------------------------------------------
    // get a list of all Epl instances
    {

        WebTarget target = client.target("http://localhost:9090/svcmgr/webapi/svc/");

        // Can I do this with a Response, so that I can check for errors
        List<Epl> response = target.path("epl/list")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Epl>>() {
                });
        if (response == null) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException("there was an error on the server.");
        }
        return response;
    }

  //--------------------------------------------------------
    public void delete(String eplId)
  //--------------------------------------------------------
  // delete Epl of specified ID
    {
        WebTarget target = client.target("http://localhost:9090/svcmgr/webapi/svc/");
        Response response = target.path("epl/"+eplId).request(MediaType.APPLICATION_JSON).delete();
        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }
    }

  //--------------------------------------------------------
    public Epl testGet()
  //--------------------------------------------------------
  // test marshaling of Epl class from server json

    {
        WebTarget target = client.target("http://localhost:9090/svcmgr/webapi/svc/");

        Response response = target.path("epl").request(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }

        //return response;
        return response.readEntity(Epl.class);
    }

  //--------------------------------------------------------
    public String ping()
  //--------------------------------------------------------
  // test connectivity
    {

        WebTarget target = client.target("http://localhost:9090/svcmgr/webapi/svc/");

        Response response = target.path("epl").request(MediaType.TEXT_PLAIN).get();
        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }

        return response.readEntity(String.class);
    }
}
