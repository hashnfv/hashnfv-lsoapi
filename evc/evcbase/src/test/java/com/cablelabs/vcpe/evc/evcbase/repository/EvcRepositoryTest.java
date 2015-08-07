package com.cablelabs.vcpe.evc.evcbase.repository;

import com.cablelabs.vcpe.cos.cosbase.model.CoS;
import com.cablelabs.vcpe.evc.evcbase.model.Evc;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by steve on 5/25/15.
 */
public class EvcRepositoryTest
{

    @Test
    public void test() {

        // First, create a couple of CoS's to reference
        CoS gold   = new CoS();
        CoS silver = new CoS();
        CoS bronze = new CoS();

        //            id        CIR/MBS  avail  delay   jitter  frameloss
        gold.setAllProps("gold", 100, 0.99, 17.43, 2.43, 0.01);
        silver.setAllProps("silver", 50, 0.95, 27.43, 2.43, 0.02);
        bronze.setAllProps("bronze", 25,      0.90,  37.43,  2.43,   0.03);

        Evc evc_1   = new Evc();
        Evc evc_2 = new Evc();
        Evc evc_3 = new Evc();

        List<String> uniIdList = new ArrayList<String>();
        uniIdList.add("UNI-1");
        uniIdList.add("UNI-2");

        List<String> uniMacList = new ArrayList<String>();
        uniMacList.add("11:11:11:11:11:11");
        uniMacList.add("22:22:22:22:22:22");

        List<String> uniIpList = new ArrayList<String>();
        uniIpList.add("192.168.1.1");
        uniIpList.add("192.168.1.2");

        evc_1.setAllNonPerfProps("evc_1",
                Evc.EvcType.POINT_TO_POINT, 2, uniIdList, uniMacList, uniIpList,
                Evc.FrameDelivery.UNCONDITIONAL,
                Evc.FrameDelivery.UNCONDITIONAL,
                Evc.FrameDelivery.UNCONDITIONAL,
                true, true, 1600, gold.getId());
        evc_2.setAllNonPerfProps("evc_2",
                Evc.EvcType.POINT_TO_POINT, 2, uniIdList, uniMacList, uniIpList,
                Evc.FrameDelivery.UNCONDITIONAL,
                Evc.FrameDelivery.UNCONDITIONAL,
                Evc.FrameDelivery.UNCONDITIONAL,
                true, true, 1500, silver.getId());
        evc_3.setAllNonPerfProps("evc_3",
                Evc.EvcType.POINT_TO_POINT, 2, uniIdList, uniMacList, uniIpList,
                Evc.FrameDelivery.UNCONDITIONAL,
                Evc.FrameDelivery.UNCONDITIONAL,
                Evc.FrameDelivery.UNCONDITIONAL,
                true, true, 1400, bronze.getId());

        evc_1.dump();
        evc_2.dump();
        evc_3.dump();

        EvcRespository repo = EvcRespositoryInMem.INSTANCE;

        assertNotNull(repo.add(evc_1));
        assertNotNull(repo.add(evc_2));
        assertNotNull(repo.add(evc_3));
        assertNull(repo.add(evc_3)); // duplicate
        assertEquals(repo.count(), 3);

        assertNotNull(repo.get(evc_1.getId()));
        assertNotNull(repo.get(evc_2.getId()));
        assertNotNull(repo.get(evc_3.getId()));

        assertNotNull(repo.delete(evc_2.getId()));
        assertNull(repo.delete(evc_2.getId()));
        assertNull(repo.delete("not-in-repo"));
        assertEquals(repo.count(), 2);

        assertEquals(repo.get(evc_1.getId()).getEvcMaxSvcFrameSize(), 1600);
        assertNotEquals(repo.get(evc_3.getId()).getEvcMaxSvcFrameSize(), 1600);

        Evc evc_4  = new Evc();
        evc_4.setAllNonPerfProps("evc_4",
                Evc.EvcType.POINT_TO_POINT, 2, uniIdList, uniMacList, uniIpList,
                Evc.FrameDelivery.UNCONDITIONAL,
                Evc.FrameDelivery.UNCONDITIONAL,
                Evc.FrameDelivery.UNCONDITIONAL,
                true, true, 1200, bronze.getId());

        assertNull(repo.update(evc_4));    // update non-existent cos
        assertEquals(repo.count(), 3);
        assertEquals(repo.get("evc_4").getEvcMaxSvcFrameSize(), 1200);

        evc_4.setEvcMaxSvcFrameSize(1250);
        assertNotNull(repo.update(evc_4)); // update existing evc, same object
        assertEquals(repo.get(evc_4.getId()).getEvcMaxSvcFrameSize(), 1250);

        evc_4.dump();

        Evc evc_4_2  = new Evc();
        evc_4_2.setAllNonPerfProps("evc_4",
                Evc.EvcType.POINT_TO_POINT, 2, uniIdList, uniMacList,  uniIpList,
                Evc.FrameDelivery.UNCONDITIONAL,
                Evc.FrameDelivery.UNCONDITIONAL,
                Evc.FrameDelivery.UNCONDITIONAL,
                true, true, 500, bronze.getId());

        assertNotNull(repo.update(evc_4_2)); // update evc, new object
        assertEquals(repo.get(evc_4_2.getId()).getEvcMaxSvcFrameSize(), 500);
    }
}
