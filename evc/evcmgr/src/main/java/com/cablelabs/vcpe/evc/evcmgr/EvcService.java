/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.evc.evcmgr;

import com.cablelabs.vcpe.common.Dbg;
import com.cablelabs.vcpe.cos.cosbase.model.CoS;
import com.cablelabs.vcpe.cos.cosbase.client.CoSClient;
import com.cablelabs.vcpe.uni.unibase.client.EvcPathClient;
import com.cablelabs.vcpe.uni.unibase.model.EvcPath;
import com.cablelabs.vcpe.uni.unibase.model.Uni;
import com.cablelabs.vcpe.uni.unibase.client.UniClient;
import com.cablelabs.vcpe.evc.evcbase.model.Evc;
import com.cablelabs.vcpe.evc.evcbase.repository.EvcRespositoryInMem;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Root resource (exposed at "evc" path)
 */

//public class EvcService implements EvcServiceJAXRS {

@Path("evc")
public class EvcService {

    // for proof of concept, evc ID will be unique via this counter.  For projecution of course
    // that does not scale, and does not survive restart, and another method will be needed
    static private long evcIdCounter = 1;
    static private long uniIdCounter = 1;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
  //--------------------------------------------------------
    public Response create(Evc evc) throws Exception
  //--------------------------------------------------------
    {
        if ( evc == null ||
             evc.getUniMacList() == null     ||
             evc.getUniIdList()  == null     ||
             evc.getUniMacList().size() != 2 ||
             evc.getUniIdList().size() != 2  ||
             evc.getUniIpList().size() != 2   )
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Dbg.p("\nADDING [" + evc.getId() + "] to evc repo");

        // Need to get cos params based on incoming cos ID
        CoSClient cosClient = new CoSClient();
        CoS cos = cosClient.get(evc.getCosId());
        if (cos == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // set the evc Perf properties according to CoS
        evc.setAllPerfProps( cos.getFrameDelay(),
                             cos.getFrameLoss(),
                             cos.getAvailbility());

        Dbg.p("... EVC SVC: retrieved following cos info");
        cos.dump(1);

        // We need a unique EVC ID, using internal counter for the prototype
        evc.setId("evc-"+ evcIdCounter++);

        //
        // Create the two unis in ODL
        //

        Uni.SvcSpeed   svcSpeed   = Uni.cirToSvcSpeed( cos.getCommitedInfoRate());
        Uni.PhysMedium physMedium = Uni.svcSpeedToPhysMedium(svcSpeed ); // just for demo
                                                                         // for production need to pull from host
        Uni uni1 = new Uni();
        uni1.setAllProps("uni-" + uniIdCounter++,
                svcSpeed,
                evc.getUniIpList().get(0),
                evc.getUniMacList().get(0),
                physMedium,
                Uni.MacLayer.IEEE_802_3,
                Uni.SyncMode.ENABLED,
                Uni.Type.UNITYPE,
                1600);

        Uni uni2 = new Uni();
        uni2.setAllProps("uni-" + uniIdCounter++,
                svcSpeed,
                evc.getUniIpList().get(1),
                evc.getUniMacList().get(1),
                physMedium,
                Uni.MacLayer.IEEE_802_3,
                Uni.SyncMode.ENABLED,
                Uni.Type.UNITYPE,
                1600);

        // RESTCONF expecting PUT on create, so do an update instead of create
        UniClient uniClient = new UniClient();
        uniClient.update(uni1);
        uniClient.update(uni2);

		// Give ODL a chance to finish creating the UNIs before moving on
        // NOTE: Should be fixed on ODL side eventually via synchronized message handling
		try{
		Thread.sleep(3000);
		} catch(InterruptedException ex)
			{Thread.currentThread().interrupt();
		}

        //
        // Create the evc path in ODL
        //

        EvcPath evcPath =  new EvcPath( evc.getId(), uni1, uni2,
                                        svcSpeed, svcSpeed, cos.getId());
        EvcPathClient evcPathClient = new EvcPathClient();
        evcPathClient.update(evcPath);

        //
        // Finish up with the evc
        //

        // Set our UNI IDs for the EVC here
        // eventually they will be generated and returned by ODL
        evc.getUniIdList().set(0,uni1.getId());
        evc.getUniIdList().set(1,uni2.getId());

        EvcRespositoryInMem.INSTANCE.add(evc);
        EvcRespositoryInMem.INSTANCE.dump(0);
        return Response.ok().entity(evc).build();
    }

    @PUT
    @Path("{evcId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
  //--------------------------------------------------------
    public Response update(Evc evc)
  //--------------------------------------------------------
    {
        Dbg.p("\nUPDATING [" + evc.getId()+"]");

        // Need to get cos params based on incoming cos ID
        CoSClient cosClient = new CoSClient();
        CoS cos = cosClient.get(evc.getCosId());
        if (cos == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        evc.setOneWayFrameDelay(cos.getFrameDelay());
        evc.setOneWayFrameLossRatio(cos.getFrameLoss());
        evc.setOneWayAvailability(cos.getAvailbility());

        EvcRespositoryInMem.INSTANCE.update(evc);
        EvcRespositoryInMem.INSTANCE.dump(0);
        return Response.ok().entity(evc).build();
    }

    @GET
    @Path("{evcId}")
    @Produces(MediaType.APPLICATION_JSON)
  //--------------------------------------------------------
    public Response get( @PathParam("evcId") String evcId )
  //--------------------------------------------------------
    {
        if ( evcId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Dbg.p("\nRETRIEVING ["+evcId+"]");
        Evc evc = EvcRespositoryInMem.INSTANCE.get(evcId);
        if (evc == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        evc.dump(1);
        return Response.ok().entity(evc).build();
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
  //--------------------------------------------------------
    public Response evcList()
  //--------------------------------------------------------

    // get a list of all Evc objects
    {
        Dbg.p("\nEVC GET ALL:");
        List evcList = EvcRespositoryInMem.INSTANCE.getAll();
        if (evcList == null  )
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(new GenericEntity<List<Evc>>(evcList) {}).build();
    }

    @DELETE
    @Path("{evcId}")
  //--------------------------------------------------------
    public Response delete(@PathParam("evcId") String evcId)
                           throws Exception
  //--------------------------------------------------------
    {
        if ( evcId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Dbg.p("\nDELETE:" + evcId);

        Evc evcToDel = EvcRespositoryInMem.INSTANCE.get(evcId);
        List<String> uniIdList = evcToDel.getUniIdList();

        // Delete EvcPath first (unis can exists independent of EVC, but not vica-versa)
        EvcPathClient evcPathClient = new EvcPathClient();
        evcPathClient.delete(evcToDel.getId());

        // Give ODL a chance to finish deleting the evc Path
        // NOTE: Should be fixed on ODL side eventually via synchronized message handling
        try{
            Thread.sleep(1000);
        } catch(InterruptedException ex)
        {Thread.currentThread().interrupt();
        }

        // Delete the UNI's in ODL
        UniClient uniClient = new UniClient();
        if ( uniIdList != null && uniIdList.size() > 0 )
            uniClient.delete(uniIdList.get(0));
        if ( uniIdList != null && uniIdList.size() > 1 )
            uniClient.delete(uniIdList.get(1));

        // OK, now we can delete the EVC itself
        EvcRespositoryInMem.INSTANCE.delete(evcId);
        EvcRespositoryInMem.INSTANCE.dump(0);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
  //--------------------------------------------------------
    public Evc testGet()
  //--------------------------------------------------------
  // simple get to check out json format
    {
        List<String> uniList = new ArrayList<String>();
        uniList.add("UNI-1");
        uniList.add("UNI-2");

        List<String> uniMacList = new ArrayList<String>();
        uniMacList.add("11:00:11:11:11:11");
        uniMacList.add("11:00:22:22:22:22");

        List<String> uniIpList = new ArrayList<String>();
        uniIpList.add("192.168.1.1");
        uniIpList.add("192.168.1.2");


        Evc evc = new Evc();
        evc.setAllNonPerfProps("Eve", // id
                Evc.EvcType.POINT_TO_POINT, // evcType
                2, // maxUnis
                uniList, uniMacList, uniIpList,
                Evc.FrameDelivery.UNCONDITIONAL, // unicastFrameDelivery
                Evc.FrameDelivery.UNCONDITIONAL, // multicastFrameDelivery
                Evc.FrameDelivery.UNCONDITIONAL, // broadcastFrameDelivery
                true, // ceVLanIdPreservation
                true, // ceVlanCosPreservation
                1600, // evcMaxSvcFrameSize
                "gold"); // cosId
        return evc;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
  //--------------------------------------------------------
    public String ping()
  //--------------------------------------------------------
  // simple ping to check connectivity

    {
        Dbg.p("In Evc Service: ping test");
        return "... pingEvc reponse";
    }
}
