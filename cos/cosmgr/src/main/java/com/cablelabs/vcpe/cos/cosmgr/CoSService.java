/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.cos.cosmgr;

import com.cablelabs.vcpe.common.Dbg;
import com.cablelabs.vcpe.cos.cosbase.model.CoS;
import com.cablelabs.vcpe.cos.cosbase.repository.CoSRespositoryInMem;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("cos")
public class CoSService
{

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
  //--------------------------------------------------------
    public Response create(CoS cos)
  //--------------------------------------------------------
    {
        if ( cos == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Dbg.p("\nADDING [" + cos.getId() + "] to cos repo");
        CoSRespositoryInMem.INSTANCE.add(cos);
        CoSRespositoryInMem.INSTANCE.dump(0);
        return Response.ok().entity(cos).build();
    }

    @PUT
    @Path("{cosId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
  //--------------------------------------------------------
    public Response update(CoS cos)
  //--------------------------------------------------------
    {
        Dbg.p("\nUPDATING [" + cos.getId()+"]");
        CoSRespositoryInMem.INSTANCE.update(cos);
        CoSRespositoryInMem.INSTANCE.dump(0);
        return Response.ok().entity(cos).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{cosId}")
  //--------------------------------------------------------
    public Response get( @PathParam("cosId") String cosId )
  //--------------------------------------------------------
    {
        if ( cosId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Dbg.p("\nRETRIEVING ["+cosId+"]");
        CoSRespositoryInMem.INSTANCE.dump(1);
        CoS cos = CoSRespositoryInMem.INSTANCE.get(cosId);
        if (cos == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        cos.dump(1);
        return Response.ok().entity(cos).build();
    }

    @DELETE
    @Path("{cosId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
  //--------------------------------------------------------
    public Response delete(@PathParam("cosId") String cosId)
  //--------------------------------------------------------

    {
        if ( cosId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Dbg.p("\nDELETE:" + cosId);
        CoSRespositoryInMem.INSTANCE.delete(cosId);
        CoSRespositoryInMem.INSTANCE.dump(0);
        return Response.ok().build();
    }


    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
  //--------------------------------------------------------
    public Response cosList()
  //--------------------------------------------------------
  // get a list of all CoS objects
    {
        Dbg.p("\nCOS GET ALL:");
        List cosList = CoSRespositoryInMem.INSTANCE.getAll();
        if (cosList == null  )
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(new GenericEntity<List<CoS>>(cosList) {}).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
  //--------------------------------------------------------
    public CoS testGet()
  //--------------------------------------------------------
  // simple get to check out json format

    {
        CoS cos = new CoS();
        cos.setAllProps("testGetCoS", 100, 0.99, 17.43, 2.43, 0.01);
        return cos;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
  //--------------------------------------------------------
    public String ping()
  //--------------------------------------------------------
  // simple ping to check connectivity
    {
        Dbg.p("In CoS Service simple get connection test server");
        return "... pingCos reponse";
    }
}
