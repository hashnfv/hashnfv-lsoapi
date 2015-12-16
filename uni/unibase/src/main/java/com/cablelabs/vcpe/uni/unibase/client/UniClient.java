/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.uni.unibase.client;

import com.cablelabs.vcpe.common.Dbg;
import com.cablelabs.vcpe.uni.unibase.model.Uni;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.json.*;
import javax.json.stream.*;
import java.io.StringReader;
import java.util.List;
import javax.xml.bind.DatatypeConverter;

/**
 * Created by steve on 5/28/15.
 */

public class UniClient {

    // put this in config file
    // private String uniMgrServer   = "localhost";

    //private String uniMgrServer   = "10.36.0.20";
    private String uniMgrServer   = "localhost";
    private String uniMgrPort     = "8181";

    private String uniMgrCfgRESTPath = "/restconf/config/network-topology:network-topology/topology/unimgr:uni/node/";
    private String uniMgrOpRESTPath = "/restconf/operational/network-topology:network-topology/topology/unimgr:uni/node/";

    private Client client; // provided by Jersey

    public UniClient() {
        client = ClientBuilder.newClient();
    }

  //--------------------------------------------------------
    public Response update(Uni uni) throws Exception
  //--------------------------------------------------------
  // create/update Uni.  This seems to be how ODL expects uni creation
    {
        WebTarget target =client.target("http://"+uniMgrServer+":" + uniMgrPort + uniMgrCfgRESTPath);

        String json = uni.toJson();
        Dbg.p("\nUNI Create/Update JSON:");
        Dbg.p(json);

        String uNameAndPass = "admin:admin";
        String authorizationHeaderValue = "Basic " +
                                           DatatypeConverter.printBase64Binary(uNameAndPass.getBytes("UTF-8"));
        Response response = target.path(uni.getId())
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", authorizationHeaderValue)
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200 ) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }
        return response;
    }

  //--------------------------------------------------------
    public void delete(String uniId) throws Exception
  //--------------------------------------------------------
  // delete Uni of specified ID
    {
        WebTarget target =client.target("http://"+uniMgrServer+":" + uniMgrPort + uniMgrCfgRESTPath);

        String uNameAndPass = "admin:admin";
        String authorizationHeaderValue = "Basic " +
                DatatypeConverter.printBase64Binary(uNameAndPass.getBytes("UTF-8"));

        Response response = target.path(uniId)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", authorizationHeaderValue)
                .delete();

        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }
    }

  //--------------------------------------------------------
    public Uni get(String uniId) throws Exception
  //--------------------------------------------------------
  // get Uni of specified ID
    {
        WebTarget target =client.target("http://"+uniMgrServer+":" + uniMgrPort + uniMgrOpRESTPath);
        Response response = target.path("uni/" + uniId).request(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }

        Uni uni = new Uni();
        uni.setId(uniId);
        String uniJson = response.readEntity(String.class);
        return JsonToUni(uni, uniJson);
    }

  //--------------------------------------------------------
    public Uni JsonToUni(Uni uni, String uniJson) {
  //--------------------------------------------------------
  // Parser utility encapsulation

        try {
            JsonParser parser = Json.createParser(new StringReader(uniJson));
            JsonParser.Event event = null;

            while (parser.hasNext()) {
                event = parser.next();
                switch(event) {
                   case KEY_NAME:
                      if (parser.getString().equals("speed")) {
                          event = parser.next();
                          if (parser.getString().equals("speed-10M")) {
                              uni.setSpeed(Uni.SvcSpeed.TEN_MEG);
                          } else
                          if (parser.getString().equals("speed-100M")) {
                              uni.setSpeed(Uni.SvcSpeed.HUNDRED_MEG);
                          } else
                          if (parser.getString().equals("speed-1G")) {
                              uni.setSpeed(Uni.SvcSpeed.ONE_GIG);
                          } else
                          if (parser.getString().equals("speed-10G")) {
                              uni.setSpeed(Uni.SvcSpeed.TEN_GIG);
                          } else {
                              uni.setSpeed(Uni.SvcSpeed.UNASSIGNED);
                          }
                      }
                      if (parser.getString().equals("mac-layer")) {
                          event = parser.next();
                          uni.setMacLayer(Uni.MacLayer.UNASSIGNED);
                          if (parser.getString().equals("IEEE 802.3-2005")) {
                              uni.setMacLayer(Uni.MacLayer.IEEE_802_3);
                          }
                      }
                      if (parser.getString().equals("physical-medium")) {
                          event = parser.next();
                          if (parser.getString().equals("10BASE-T")) {
                              uni.setPhysicalMedium(Uni.PhysMedium.TEN_BASE_T);
                          } else
                          if (parser.getString().equals("100BASE-T")) {
                              uni.setPhysicalMedium(Uni.PhysMedium.HUNDERED_BASE_T);
                          } else
                          if (parser.getString().equals("100BASE-T")) {
                              uni.setPhysicalMedium(Uni.PhysMedium.GIG_BASE_T);
                          } else
                          if (parser.getString().equals("10GBASE-T")) {
                              uni.setPhysicalMedium(Uni.PhysMedium.TEN_GIG_BASE_T);
                          } else {
                              uni.setPhysicalMedium(Uni.PhysMedium.UNASSIGNED);
                          }
                      }
                      if (parser.getString().equals("mtu-size")) {
                          uni.setMtuSize(parser.getLong());
                          event = parser.next();
                      }
                      if (parser.getString().equals("type")) {
                          event = parser.next();
                          uni.setType(Uni.Type.UNASSIGNED);
                          if (parser.getString().equals("IEEE 802.3-2005")) {
                              uni.setType(Uni.Type.UNITYPE);
                          }
                      }
                      if (parser.getString().equals("mac-address")) {
                          uni.setMacAddress(parser.getString());
                          event = parser.next();
                      }
                      if (event.toString().equals("ip-address")) {
                          uni.setIpAddress(parser.getString());
                          event = parser.next();
                      }
                      if (event.toString().equals("mode")) {
                          event = parser.next();
                          if (parser.getString().equals("syncEnabled")) {
                              uni.setMode(Uni.SyncMode.ENABLED);
                          } else
                          if (parser.getString().equals("syncDisabled")) {
                              uni.setMode(Uni.SyncMode.DISABLED);
                          } else {
                              uni.setMode(Uni.SyncMode.UNASSIGNED);
                          }
                      }
                      break;
                   default:
                      break;
                }
            }
        } catch(Exception e) {
            Dbg.p("\n, JsonReader:" + e);
        }
        return uni;
    }

    //
    // Code from here below requires work in order to work with ODL
    // Currently none of the calls below are being invoked
    // Consider removing these from the source
    //

  //--------------------------------------------------------
    public Response create(Uni uni ) throws Exception
  //--------------------------------------------------------
    // create Uni
    {
        WebTarget target =client.target("http://"+uniMgrServer+":" + uniMgrPort + uniMgrCfgRESTPath);

        String json = uni.toJson();
        Dbg.p("\nUNI Create JSON:");
        Dbg.p(json);

        String uNameAndPass = "admin:admin";
        String authorizationHeaderValue = "Basic " +
                DatatypeConverter.printBase64Binary(uNameAndPass.getBytes("UTF-8"));
        Response response = target.path("uni")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", authorizationHeaderValue)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200 ) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }
        //return response.readEntity(Uni.class);
        return response;
    }

  //--------------------------------------------------------
    public List<Uni> getAll()
  //--------------------------------------------------------
  // get a list of all Uni instances
    {
        WebTarget target = client.target("http://localhost:9090/unimgr/webapi/");

        // Can I do this with a Response, so that I can check for errors
        List<Uni> response = target.path("uni/list")
                                   .request(MediaType.APPLICATION_JSON)
                                   .get(new GenericType<List<Uni>>() {
                                   });
        if (response == null) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException("there was an error on the server.");
        }
        return response;
    }

  //--------------------------------------------------------
    public Uni testGet()
  //--------------------------------------------------------
  // test marshaling of Uni class from server json
    {
        WebTarget target = client.target("http://localhost:9090/unimgr/webapi/");

        Response response = target.path("uni").request(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }

        //return response;
        return response.readEntity(Uni.class);
    }

  //--------------------------------------------------------
    public String ping()
  //--------------------------------------------------------
  // test connectivity
    {

            WebTarget target = client.target("http://localhost:9090/unimgr/webapi/");

            Response response = target.path("uni").request(MediaType.TEXT_PLAIN).get();
            if (response.getStatus() != 200) // figure out how to use Status.OK
            {
                // in production you can be more specific based on reponse code, id, etc
                throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
            }


            return response.readEntity(String.class);
    }
}
