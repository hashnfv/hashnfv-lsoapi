/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.evc.evcbase.client;

import com.cablelabs.vcpe.common.Dbg;
import com.cablelabs.vcpe.cos.cosbase.client.CoSClient;
import com.cablelabs.vcpe.cos.cosbase.model.CoS;
import com.cablelabs.vcpe.evc.evcbase.model.Evc;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by steve on 6/8/15.
 */
public class EvcClientTest {

    @Test
    public void testAll() throws Exception {

        // We have to have a CoS for Evc to referece
        CoSClient coSClient = new CoSClient();

        // Add gold service to CoS layer
        CoS gold = new CoS();
        gold.setAllProps("gold", 100, 0.99, 17.43, 2.43, 0.01);
        Dbg.p("gold svc being created in CoS");

        gold = coSClient.create(gold);
        assertNotNull(gold);

        Evc evc_1   = new Evc();

        List<String> uniIdList1 = new ArrayList<String>();
        uniIdList1.add("unset-id-1");
        uniIdList1.add("unset-id-1");

        List<String> uniMacList1 = new ArrayList<String>();
        uniMacList1.add("11:00:11:11:11:11");
        uniMacList1.add("11:00:22:22:22:22");

        List<String> ipList1 = new ArrayList<String>();
        ipList1.add("192.168.1.1");
        ipList1.add("192.168.1.2");

        evc_1.setAllNonPerfProps("id-unset-1",
                Evc.EvcType.POINT_TO_POINT, 2, uniIdList1, uniMacList1, ipList1,
                Evc.FrameDelivery.UNCONDITIONAL,
                Evc.FrameDelivery.UNCONDITIONAL,
                Evc.FrameDelivery.UNCONDITIONAL,
                true, true, 1600, gold.getId());

        EvcClient evcClient = new EvcClient();
        Dbg.p(evc_1.getId()+" being created via evcmgr");
        evc_1.dump(1);

        evc_1 = evcClient.create(evc_1);
        assertNotNull(evc_1);

        Evc retrievedEvc = evcClient.get(evc_1.getId());
        assertNotNull(retrievedEvc);
        Dbg.p("evc just retrieved from Evc Service");
        retrievedEvc.dump(1);
        retrievedEvc = null;

        evc_1.setBroadcastFrameDelivery(Evc.FrameDelivery.DISCARD);
        Dbg.p("EVC["+ evc_1.getId()+ "] : about to be updated");
        evc_1.dump(1);
        assertNotNull(evcClient.update(evc_1));
        retrievedEvc = evcClient.get(evc_1.getId());
        assertNotNull(retrievedEvc);
        Dbg.p("EVC["+ evc_1.getId()+ "] : retrieved after the update");
        retrievedEvc.dump(1);

        Evc evc_2   = new Evc();
        Evc evc_3   = new Evc();

        List<String> uniIdList2 = new ArrayList<String>();
        uniIdList2.add("unset-id-11");
        uniIdList2.add("unset-id-22");

        List<String> uniIdList3 = new ArrayList<String>();
        uniIdList3.add("unset-id-111");
        uniIdList3.add("unset-id-222");

        List<String> uniMacList2 = new ArrayList<String>();
        uniMacList2.add("22:00:11:11:11:11");
        uniMacList2.add("22:00:22:22:22:22");

        List<String> uniMacList3 = new ArrayList<String>();
        uniMacList3.add("33:00:11:11:11:11");
        uniMacList3.add("33:00:22:22:22:22");

        List<String> ipList2 = new ArrayList<String>();
        ipList2.add("192.168.2.1");
        ipList2.add("192.168.2.2");

        List<String> ipList3 = new ArrayList<String>();
        ipList3.add("192.168.3.1");
        ipList3.add("192.168.3.2");

        evc_2.setAllNonPerfProps("id-unset-2",
                Evc.EvcType.ROOTED_MULTIPOINT, 2, uniIdList2, uniMacList2, ipList2,
                Evc.FrameDelivery.CONDITIONAL,
                Evc.FrameDelivery.CONDITIONAL,
                Evc.FrameDelivery.CONDITIONAL,
                true, false, 1111, gold.getId());

        evc_3.setAllNonPerfProps("id-unset-3",
                Evc.EvcType.MULTIPOINT_TO_MULTIPOINT, 2, uniIdList3, uniMacList2, ipList3,
                Evc.FrameDelivery.DISCARD,
                Evc.FrameDelivery.DISCARD,
                Evc.FrameDelivery.DISCARD,
                false, true, 2222, gold.getId());

        evc_2 = evcClient.create(evc_2);
        assertNotNull(evc_2);

        evc_3 = evcClient.create(evc_3);
        assertNotNull(evc_3);

        List<Evc> evcList = evcClient.getAll();
        assertNotNull(evcList);
        assertEquals(evcList.size(), 3);

        Evc.dumpList(evcList);

        evcClient.delete(evc_3.getId());
        evcList = evcClient.getAll();
        assertNotNull(evcList);
        assertEquals(evcList.size(),2);
        Evc.dumpList(evcList);

        evcClient.delete(evc_2.getId());
        evcList = evcClient.getAll();
        assertNotNull(evcList);
        assertEquals(evcList.size(),1);
        Evc.dumpList(evcList);

        evcClient.delete(evc_1.getId());
        evcList = evcClient.getAll();
        assertNotNull(evcList);
        assertEquals(evcList.size(),0);
        Evc.dumpList(evcList);

/*
        // Test update
        // Add silver service to CoS layer
        CoS silver = new CoS();
        silver.setAllProps("silver", 100, 0.99, 17.43, 2.43, 0.01);
        Dbg.p("silver svc being created in CoS");

        silver = coSClient.create(silver);
        assertNotNull(silver);

        Evc evc_4   = new Evc();

        evc_4.setAllNonPerfProps("id-unset-1",
                Evc.EvcType.POINT_TO_POINT, 2, uniIdList1, uniMacList1, ipList1,
                Evc.FrameDelivery.UNCONDITIONAL,
                Evc.FrameDelivery.UNCONDITIONAL,
                Evc.FrameDelivery.UNCONDITIONAL,
                true, true, 1600, gold.getId());

        EvcClient evcClient2 = new EvcClient();
        Dbg.p(evc_4.getId()+" being created via evcmgr");
        evc_4.dump(1);

        evc_4 = evcClient2.create(evc_4);
        assertNotNull(evc_4);

        evc_4.setCosId("silver");

        evc_4 = evcClient2.update(evc_4);
        assertNotNull(evc_4);

        retrievedEvc = evcClient2.get(evc_4.getId());
        assertNotNull(retrievedEvc);
        Dbg.p("evc just retrieved from Evc Service");
        retrievedEvc.dump(1);
        retrievedEvc = null;
*/
    }

    @Test
    public void testTestGet() throws Exception {
        EvcClient evcClient = new EvcClient();
        Evc evc = evcClient.testGet();
        evc.dump();;
    }

    @Test
    public void testPing() throws Exception {

        EvcClient evcClient = new EvcClient();
        String resp = evcClient.ping();
        Dbg.p(resp);
    }
}
