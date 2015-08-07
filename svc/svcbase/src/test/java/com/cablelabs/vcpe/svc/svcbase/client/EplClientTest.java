package com.cablelabs.vcpe.svc.svcbase.client;

import com.cablelabs.vcpe.common.Dbg;
import com.cablelabs.vcpe.cos.cosbase.client.CoSClient;
import com.cablelabs.vcpe.cos.cosbase.model.CoS;
import com.cablelabs.vcpe.svc.svcbase.model.Epl;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by steve on 6/8/15.
 */
public class EplClientTest {

    @Test
    public void testAll() throws Exception {

        // First, create a couple of CoS's to reference
        CoS gold   = new CoS();
        CoS silver = new CoS();
        CoS bronze = new CoS();

        gold.setAllProps("gold", 100, 0.99, 17.43, 2.43, 0.01);
        silver.setAllProps("silver", 50, 0.95, 27.43, 2.43, 0.02);
        bronze.setAllProps("bronze", 25, 0.90,  37.43,  2.43,   0.03);

        CoSClient coSClient = new CoSClient();

        gold = coSClient.create(gold);
        assertNotNull(gold);
        silver = coSClient.create(silver);
        assertNotNull(silver);
        bronze = coSClient.create(bronze);
        assertNotNull(bronze);

        List<String> locList1 = new ArrayList<String>();
        locList1.add("1 MEF Dr, Honolulu HI, USA");
        locList1.add("1 MEF Dr, Boston MA, MAS");
        List<String> uniList1 = new ArrayList<String>();
        uniList1.add("11:AA:00:00:00:00");
        uniList1.add("11:BB:00:00:00:00");
        List<String> ipList1 = new ArrayList<String>();
        ipList1.add("192.168.1.1");
        ipList1.add("192.168.1.2");

        List<String> locList2 = new ArrayList<String>();
        locList2.add("22 MEF Dr, Honolulu HI, USA");
        locList2.add("22 MEF Dr, Boston MA, MAS");
        List<String> uniList2 = new ArrayList<String>();
        uniList2.add("22:AA:00:00:00:00");
        uniList2.add("22:BB:00:00:00:00");
        List<String> ipList2 = new ArrayList<String>();
        ipList2.add("192.168.2.1");
        ipList2.add("192.168.2.2");

        List<String> locList3 = new ArrayList<String>();
        locList3.add("33 MEF Dr, Honolulu HI, USA");
        locList3.add("33 MEF Dr, Boston MA, MAS");
        List<String> uniList3 = new ArrayList<String>();
        uniList3.add("33:AA:00:00:00:00");
        uniList3.add("33:BB:00:00:00:00");
        List<String> ipList3 = new ArrayList<String>();
        ipList3.add("192.168.3.1");
        ipList3.add("192.168.3.2");

        Epl epl_1 = new Epl();
        epl_1.setAllProps("epl-1", 2, locList1, uniList1, ipList1, gold.getId(), "unset");

        EplClient eplClient = new EplClient();
        Dbg.p(epl_1.getId()+" being created via eplmgr");
        epl_1.dump(1);


        // need to capture returned EPL in case it was modified by svc layer during creation
        epl_1 = eplClient.create(epl_1);
        assertNotNull(epl_1);

        Epl retrievedEpl = eplClient.get(epl_1.getId());
        assertNotNull(retrievedEpl);
        Dbg.p("epl just retrieved from Epl Service");
        retrievedEpl.dump(1);
        retrievedEpl = null;

        epl_1.setCos(bronze.getId());
        Dbg.p("EPL["+ epl_1.getId()+ "] : about to be updated");
        epl_1.dump(1);
        assertNotNull(eplClient.update(epl_1));
        retrievedEpl = eplClient.get(epl_1.getId());
        assertNotNull(retrievedEpl);
        Dbg.p("EPL["+ epl_1.getId()+ "] : retrieved after the update");
        retrievedEpl.dump(1);

        Epl epl_2 = new Epl();
        epl_2.setAllProps("epl-2", 2, locList2, uniList2, ipList2, silver.getId(), "unset");

        Epl epl_3 = new Epl();
        epl_3.setAllProps("epl-3", 2, locList3, uniList3, ipList3, bronze.getId(), "unset");

        // need to capture returned EPL in case it was modified by svc layer during creation
        epl_2 = eplClient.create(epl_2);
        assertNotNull(epl_2);
        epl_3 = eplClient.create(epl_3);
        assertNotNull(epl_3);

        List<Epl> eplList = eplClient.getAll();
        assertNotNull(eplList);
        assertEquals(eplList.size(), 3);

        Epl.dumpList(eplList);

        eplClient.delete(epl_3.getId());
        eplList = eplClient.getAll();
        assertNotNull(eplList);
        assertEquals(eplList.size(),2);
        Epl.dumpList(eplList);

        eplClient.delete(epl_2.getId());
        eplList = eplClient.getAll();
        Epl.dumpList(eplList);
        assertNotNull(eplList);
        assertEquals(eplList.size(),1);
        Epl.dumpList(eplList);

        eplClient.delete(epl_1.getId());
        eplList = eplClient.getAll();
        assertNotNull(eplList);
        assertEquals(eplList.size(),0);
        Epl.dumpList(eplList);
    }

    @Test
    public void testTestGet() throws Exception {
        EplClient eplClient = new EplClient();
        Epl epl = eplClient.testGet();
        epl.dump();;
    }

    @Test
    public void testPing() throws Exception {

        EplClient eplClient = new EplClient();
        String resp = eplClient.ping();
        Dbg.p(resp);
    }
}
