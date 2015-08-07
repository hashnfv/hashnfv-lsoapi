package com.cablelabs.vcpe.evc.evcbase.client;

import com.cablelabs.vcpe.evc.evcbase.model.Evc;

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
public class EvcClient {

    private Client client; // provided by Jersey

    public EvcClient() {
        client = ClientBuilder.newClient();
    }

  //--------------------------------------------------------
    public Evc create(Evc evc)
  //--------------------------------------------------------
    {
        WebTarget target =client.target("http://localhost:9090/evcmgr/webapi/");
        Response response = target.path("evc")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(evc, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200 ) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }
        return response.readEntity(Evc.class);
    }

  //--------------------------------------------------------
    public Evc update(Evc evc)
  //--------------------------------------------------------
    {
        WebTarget target =client.target("http://localhost:9090/evcmgr/webapi/");
        Response response = target.path("evc/"+evc.getId())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(evc, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200 ) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }
        return response.readEntity(Evc.class);
    }

  //--------------------------------------------------------
    public Evc get(String evcId)
  //--------------------------------------------------------
  // get Evc of specified ID

    {
        WebTarget target = client.target("http://localhost:9090/evcmgr/webapi/");

        Response response = target.path("evc/"+evcId).request(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }

        //return response;
        return response.readEntity(Evc.class);
    }

  //--------------------------------------------------------
    public List<Evc> getAll()
  //--------------------------------------------------------
  // get a list of all Evc instances
    {

        WebTarget target = client.target("http://localhost:9090/evcmgr/webapi/");

        // Can I do this with a Response, so that I can check for errors
        List<Evc> response = target.path("evc/list")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Evc>>() {
                });
        if (response == null) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException("there was an error on the server.");
        }
        return response;
    }

  //--------------------------------------------------------
    public void delete(String evcId)
  //--------------------------------------------------------
  // delete Evc of specified ID
    {
        WebTarget target = client.target("http://localhost:9090/evcmgr/webapi/");
        Response response = target.path("evc/"+evcId).request(MediaType.APPLICATION_JSON).delete();
        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }
    }

  //--------------------------------------------------------
    public Evc testGet()
  //--------------------------------------------------------
  // test marshaling of Evc class from server json

    {
        WebTarget target = client.target("http://localhost:9090/evcmgr/webapi/");

        Response response = target.path("evc").request(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }

        //return response;
        return response.readEntity(Evc.class);
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
