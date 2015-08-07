package com.cablelabs.vcpe.uni.unibase.client;

import com.cablelabs.vcpe.common.Dbg;
import com.cablelabs.vcpe.uni.unibase.model.EvcPath;
import com.cablelabs.vcpe.uni.unibase.model.Uni;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.util.List;

/**
 * Created by steve on 5/28/15.
 */

public class EvcPathClient {

    private String evcMgrServer   = "localhost";
    private String evcMgrPort     = "8181";
    private String evcMgrCfgRESTPath    = "/restconf/config/cl-vcpe-mef:evcs/";

    private Client client; // provided by Jersey

    public EvcPathClient() {
        client = ClientBuilder.newClient();
    }

  //--------------------------------------------------------
    public Response create(EvcPath evcPath )
  //--------------------------------------------------------
    {
        WebTarget target =client.target("http://"+evcMgrServer+":" + evcMgrPort + evcMgrCfgRESTPath);

        String json = evcPath.toJson();
        Dbg.p("\nEVC Create JSON:");
        Dbg.p(json);

        Response response = target.path("evc")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200 ) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }
        //return response.readEntity(EvcPath.class);
        return response;
    }

  //--------------------------------------------------------
    public Response update(EvcPath evcPath) throws Exception
  //--------------------------------------------------------
    {
        WebTarget target =client.target("http://"+evcMgrServer+":" + evcMgrPort + evcMgrCfgRESTPath);

        String json = evcPath.toJson();
        String unamepass = "admin:admin";
        String authorizationHeaderValue = "Basic " + DatatypeConverter.printBase64Binary(unamepass.getBytes("UTF-8"));
        Dbg.p("\nEVC Create/Update JSON:");
        Dbg.p(json);

        Response response = target.path("evc/"+evcPath.getId())
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
    public void delete(String evcPathId) throws Exception
  //--------------------------------------------------------
  // delete EvcPath of specified ID
    {
        WebTarget target =client.target("http://"+evcMgrServer+":" + evcMgrPort + evcMgrCfgRESTPath);

        String uNameAndPass = "admin:admin";
        String authorizationHeaderValue = "Basic " +
                DatatypeConverter.printBase64Binary(uNameAndPass.getBytes("UTF-8"));

        Response response = target.path("evc/" + evcPathId)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", authorizationHeaderValue)
                .delete();
        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }
    }

    //
    // Code from here below requires work in order to work with ODL
    //

  //--------------------------------------------------------
    public EvcPath get(String evcPathId)
  //--------------------------------------------------------
  // get EvcPath of specified ID
    {
        WebTarget target = client.target("http://localhost:9090/evcmgr/webapi/");

        Response response = target.path("evc/"+evcPathId).request(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }

        return response.readEntity(EvcPath.class);
    }

  //--------------------------------------------------------
    public List<EvcPath> getAll()
  //--------------------------------------------------------
  // get a list of all EvcPath instances
    {

        WebTarget target = client.target("http://localhost:9090/evcmgr/webapi/");

        // Can I do this with a Response, so that I can check for errors
        List<EvcPath> response = target.path("evc/list")
                                   .request(MediaType.APPLICATION_JSON)
                                   .get(new GenericType<List<EvcPath>>() {
                                   });
        if (response == null) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException("there was an error on the server.");
        }
        return response;
    }

  //--------------------------------------------------------
    public EvcPath testGet()
  //--------------------------------------------------------
  // test marshaling of EvcPath class from server json
    {
        WebTarget target = client.target("http://localhost:9090/evcmgr/webapi/");

        Response response = target.path("evc").request(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }

        //return response;
        return response.readEntity(EvcPath.class);
    }

  //--------------------------------------------------------
    public String ping()
  //--------------------------------------------------------
  // test connectivity
    {

            WebTarget target = client.target("http://localhost:9090/evcmgr/webapi/");

            Response response = target.path("evc").request(MediaType.TEXT_PLAIN).get();
            if (response.getStatus() != 200) // figure out how to use Status.OK
            {
                // in production you can be more specific based on reponse code, id, etc
                throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
            }


            return response.readEntity(String.class);
    }
}
