package com.cablelabs.vcpe.cos.cosbase.client;

import com.cablelabs.vcpe.cos.cosbase.model.CoS;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by steve on 5/28/15.
 */
public class CoSClient {

    private Client client; // provided by Jersey

    public CoSClient() {
        client = ClientBuilder.newClient();
    }

  //--------------------------------------------------------
    public CoS create(CoS cos)
  //--------------------------------------------------------
    {
        WebTarget target =client.target("http://localhost:9090/cosmgr/webapi/");
        Response response = target.path("cos")
                                  .request(MediaType.APPLICATION_JSON)
                                  .post(Entity.entity(cos, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200 ) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }
        return response.readEntity(CoS.class);
    }

  //--------------------------------------------------------
    public CoS update(CoS cos)
  //--------------------------------------------------------
    {
        WebTarget target =client.target("http://localhost:9090/cosmgr/webapi/");
        Response response = target.path("cos/"+cos.getId())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(cos, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200 ) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }
        return response.readEntity(CoS.class);
    }


  //--------------------------------------------------------
    public CoS get(String cosId)
  //--------------------------------------------------------
  // get CoS of specified ID
    {
        WebTarget target = client.target("http://localhost:9090/cosmgr/webapi/");

        Response response = target.path("cos/"+cosId).request(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }

        //return response;
        return response.readEntity(CoS.class);
    }

  //--------------------------------------------------------
    public List<CoS> getAll()
  //--------------------------------------------------------
  // get a list of all CoS instances
    {

        WebTarget target = client.target("http://localhost:9090/cosmgr/webapi/");

        // Can I do this with a Response, so that I can check for errors
        List<CoS> response = target.path("cos/list")
                                   .request(MediaType.APPLICATION_JSON)
                                   .get(new GenericType<List<CoS>>() {
                                   });
        if (response == null) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException("there was an error on the server.");
        }
        return response;
    }



  //--------------------------------------------------------
    public void delete(String cosId)
  //--------------------------------------------------------
  // delete CoS of specified ID
    {
        WebTarget target = client.target("http://localhost:9090/cosmgr/webapi/");
        Response response = target.path("cos/"+cosId).request(MediaType.APPLICATION_JSON).delete();
        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }
    }

  //--------------------------------------------------------
    public CoS testGet()
  //--------------------------------------------------------
  // test marshaling of CoS class from server json
    {
        WebTarget target = client.target("http://localhost:9090/cosmgr/webapi/");

        Response response = target.path("cos").request(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatus() != 200) // figure out how to use Status.OK
        {
            // in production you can be more specific based on reponse code, id, etc
            throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
        }

        //return response;
        return response.readEntity(CoS.class);
    }

  //--------------------------------------------------------
    public String ping()
  //--------------------------------------------------------
  // test connectivity
    {

            WebTarget target = client.target("http://localhost:9090/cosmgr/webapi/");

            Response response = target.path("cos").request(MediaType.TEXT_PLAIN).get();
            if (response.getStatus() != 200) // figure out how to use Status.OK
            {
                // in production you can be more specific based on reponse code, id, etc
                throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
            }


            return response.readEntity(String.class);
    }
}
