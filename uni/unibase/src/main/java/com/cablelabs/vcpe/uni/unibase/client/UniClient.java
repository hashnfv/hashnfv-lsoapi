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
import java.util.List;
import javax.xml.bind.DatatypeConverter;

/**
 * Created by steve on 5/28/15.
 */

public class UniClient {

    private String uniMgrServer   = "localhost";
    private String uniMgrPort     = "8181";
    private String uniMgrCfgRESTPath = "/restconf/config/cl-vcpe-mef:unis/";

    private Client client; // provided by Jersey

    public UniClient() {
        client = ClientBuilder.newClient();
    }

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
    public Response update(Uni uni) throws Exception
  //--------------------------------------------------------
  // create/update Uni.  This seems to be how OLD expects uni creation
    {
        WebTarget target =client.target("http://"+uniMgrServer+":" + uniMgrPort + uniMgrCfgRESTPath);

        String json = uni.toJson();
        Dbg.p("\nUNI Create/Update JSON:");
        Dbg.p(json);

        String uNameAndPass = "admin:admin";
        String authorizationHeaderValue = "Basic " +
                                           DatatypeConverter.printBase64Binary(uNameAndPass.getBytes("UTF-8"));
        Response response = target.path("uni/"+uni.getId())
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

        Response response = target.path("uni/" + uniId)
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
    public Uni get(String uniId)
  //--------------------------------------------------------
  // get Uni of specified ID
    {
        WebTarget target = client.target("http://localhost:9090/unimgr/webapi/");

        Response response = target.path("uni/"+uniId).request(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }

        //return response;
        return response.readEntity(Uni.class);
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
