/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.svc.svcmgr;


import com.cablelabs.vcpe.common.Dbg;
import com.cablelabs.vcpe.evc.evcbase.client.EvcClient;
import com.cablelabs.vcpe.svc.svcbase.model.Epl;
import com.cablelabs.vcpe.svc.svcbase.repository.EplRespositoryInMem;
import com.cablelabs.vcpe.evc.evcbase.model.Evc;


import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Root resource (exposed at "svc/epl" path)
 */

@Path("svc/epl")
public class EplService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
  //--------------------------------------------------------
    public Response create(Epl epl)
  //--------------------------------------------------------
    {
        if ( epl == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Dbg.p("\nADDING [" + epl.getId() + "] to epl repo");

        //
        // Create an EVC
        //

        Evc evcForEpl   = new Evc();

        // Uni IDs set at lower layers
        List<String> uniIdList = new ArrayList<String>();
        uniIdList.add("unset");
        uniIdList.add("unset");

        // Transfer EPLs mac list to EVC
        List<String> uniMacList = new ArrayList<String>();
        if (epl.getUniHostMacList() != null && epl.getUniHostMacList().size() == 2) {
            uniMacList.add(epl.getUniHostMacList().get(0) );
            uniMacList.add(epl.getUniHostMacList().get(1));
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // Transfer EPLs I{P list to EVC
        List<String> uniIpList = new ArrayList<String>();
        if (epl.getUniHostIpList() != null && epl.getUniHostIpList().size() == 2) {
            uniIpList.add(epl.getUniHostIpList().get(0) );
            uniIpList.add(epl.getUniHostIpList().get(1));
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // evc mgr will set perf props based on CoS.  We need to set everything else
        evcForEpl.setAllNonPerfProps(   "unset",                          // set by lower layer (id)
                                        Evc.EvcType.POINT_TO_POINT,       // required for EPL (evcType)
                                        2,                                // required for EPL (maxUnis)
                                        uniIdList,                        // created above
                                        uniMacList, uniIpList,            // passed in
                                        Evc.FrameDelivery.UNCONDITIONAL,  // required for EPL (unicastFrameDelivery)
                                        Evc.FrameDelivery.UNCONDITIONAL,  // required for EPL (multicastFrameDelivery)
                                        Evc.FrameDelivery.UNCONDITIONAL,  // required for EPL (broadcastFrameDelivery)
                                        true,                             // required for EPL (ceVLanIdPreservation)
                                        true,                             // required for EPL (ceVlanCosPreservation)
                                        1600,                             // hard coded, need to figure out source !!
                                        epl.getCos());

        // send create request to EVC mgr
        EvcClient evcClient = new EvcClient();
        evcForEpl= evcClient.create(evcForEpl);

        // EVC Layer generates the ID, we need to capture it
        epl.setEvcId(evcForEpl.getId());

        // Now add the EPL to our local repo
        EplRespositoryInMem.INSTANCE.add(epl);
        EplRespositoryInMem.INSTANCE.dump(0);
        return Response.ok().entity(epl).build();
    }

    @PUT
    @Path("{eplId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
  //--------------------------------------------------------
    public Response update(Epl epl)
  //--------------------------------------------------------
    {
        Dbg.p("\nUPDATING [" + epl.getId()+"]");

        EplRespositoryInMem.INSTANCE.update(epl);
        EplRespositoryInMem.INSTANCE.dump(0);
        return Response.ok().entity(epl).build();
    }

    @GET
    @Path("{eplId}")
    @Produces(MediaType.APPLICATION_JSON)
  //--------------------------------------------------------
    public Response get( @PathParam("eplId") String eplId )
  //--------------------------------------------------------
    {
        if ( eplId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Dbg.p("\nRETRIEVING ["+eplId+"]");
        Epl epl = EplRespositoryInMem.INSTANCE.get(eplId);
        if (epl == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        epl.dump(1);
        return Response.ok().entity(epl).build();
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
  //--------------------------------------------------------
    public Response eplList()
  //--------------------------------------------------------

    // get a list of all Epl objects
    {
        Dbg.p("\nEPL GET ALL:");
        List eplList = EplRespositoryInMem.INSTANCE.getAll();
        if (eplList == null  )
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(new GenericEntity<List<Epl>>(eplList) {}).build();
    }

    @DELETE
    @Path("{eplId}")
  //--------------------------------------------------------
    public Response delete(@PathParam("eplId") String eplId)
  //--------------------------------------------------------
    {
        if ( eplId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // grab the full EPL so that we can get EVC ID to delete
        Epl epl = EplRespositoryInMem.INSTANCE.get(eplId);
        if (epl == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        String evcId = epl.getEvcId();

        // Delete the EVC associated with the EPL being deleted
        EvcClient evcClient = new EvcClient();
        evcClient.delete(evcId);

        Dbg.p("\nDELETE:" + eplId);
        EplRespositoryInMem.INSTANCE.delete(eplId);
        EplRespositoryInMem.INSTANCE.dump(0);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
  //--------------------------------------------------------
    public Epl testGet()
  //--------------------------------------------------------
  // simple get to check out json format

    {
        long numLocations = 2;

        List<String> locList = new ArrayList<String>();
        locList.add("1111 MEF Dr, Honolulu HI, USA");
        locList.add("2222 MEF Dr, Boston MA, MAS");

        List<String> uniList = new ArrayList<String>();
        uniList.add("00:0a:95:9d:68:16");
        uniList.add("00:A0:C9:14:C8:29");

        List<String> ipList = new ArrayList<String>();
        ipList.add("192.168.1.10");
        ipList.add("192.168.1.10");

        Epl epl = new Epl();
        epl.setAllProps("epl-1", numLocations, locList, uniList, ipList, "gold", "unset");
        return epl;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
  //--------------------------------------------------------
    public String ping()
  //--------------------------------------------------------
  // simple ping to check connectivity

    {
        Dbg.p("In Epl Service: ping test");
        return "... pingEpl reponse";
    }
}
