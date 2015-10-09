/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.svc.svcbase.repository;

import com.cablelabs.vcpe.cos.cosbase.model.CoS;
import com.cablelabs.vcpe.svc.svcbase.model.Epl;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by steve on 5/25/15.
 */
public class EplRepositoryTest
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

        // lets add these to the

        List<String> locList1 = new ArrayList<String>();
        locList1.add("1111 MEF Dr, Honolulu HI, USA");
        locList1.add("2222 MEF Dr, Honolulu HI, USA");
        List<String> uniList1 = new ArrayList<String>();
        uniList1.add("11:11:11:11:11:11");
        uniList1.add("22:22:22:22:22:22");
        List<String> ipList1 = new ArrayList<String>();
        ipList1.add("192.168.1.1");
        ipList1.add("192.168.1.2");


        List<String> locList2 = new ArrayList<String>();
        locList2.add("1111 MEF Dr, Honolulu HI, USA");
        locList2.add("2222 MEF Dr, Boston MA, MAS");
        List<String> uniList2 = new ArrayList<String>();
        uniList2.add("11:11:11:11:11:11");
        uniList2.add("22:22:22:22:22:22");
        List<String> ipList2 = new ArrayList<String>();
        ipList2.add("192.168.2.1");
        ipList2.add("192.168.2.2");

        List<String> locList3 = new ArrayList<String>();
        locList3.add("1111 MEF Dr, Honolulu HI, USA");
        locList3.add("2222 MEF Dr, Boston MA, MAS");
        locList3.add("3333 MEF Dr, Boulder CO, USA");
        List<String> uniList3 = new ArrayList<String>();
        uniList3.add("11:11:11:11:11:11");
        uniList3.add("22:22:22:22:22:22");
        List<String> ipList3 = new ArrayList<String>();
        ipList3.add("192.168.3.1");
        ipList3.add("192.168.3.2");

        List<String> locList4 = new ArrayList<String>();
        locList4.add("1111 MEF Dr, Honolulu HI, USA");
        locList4.add("2222 MEF Dr, Boston MA, MAS");
        locList4.add("3333 MEF Dr, Boulder CO, USA");
        locList4.add("4444 MEF Dr, Los Angeles, CA, USA");
        List<String> uniList4 = new ArrayList<String>();
        uniList4.add("11:11:11:11:11:11");
        uniList4.add("22:22:22:22:22:22");
        uniList4.add("33:33:33:33:33:33");
        uniList4.add("44:44:44:44:44:44");
        List<String> ipList4 = new ArrayList<String>();
        ipList4.add("192.168.4.1");
        ipList4.add("192.168.4.2");



        Epl epl_1 = new Epl();
        Epl epl_2 = new Epl();
        Epl epl_3 = new Epl();
        Epl epl_4 = new Epl();

        epl_1.setAllProps("epl-1", 1, locList1, uniList1, ipList1, gold.getId(),   "unset");
        epl_2.setAllProps("epl-2", 2, locList2, uniList2, ipList2, silver.getId(), "unset");
        epl_3.setAllProps("epl-3", 3, locList3, uniList3, ipList3, bronze.getId(), "unset");
        epl_4.setAllProps("epl-4", 4, locList4, uniList4, ipList4, gold.getId(),   "unset");

        EplRespository repo = EplRespositoryInMem.INSTANCE;

        assertNotNull(repo.add(epl_1));
        assertNotNull(repo.add(epl_2));
        assertNotNull(repo.add(epl_3));
        assertNull(repo.add(epl_3)); // duplicate
        assertEquals(repo.count(), 3);

        assertNotNull(repo.get(epl_1.getId()));
        assertNotNull(repo.get(epl_2.getId()));
        assertNotNull(repo.get(epl_3.getId()));

        assertNotNull(repo.delete(epl_2.getId()));
        assertNull(repo.delete(epl_2.getId()));
        assertNull(repo.delete("not-in-repo"));
        assertEquals(repo.count(), 2);

        assertEquals(repo.get(epl_1.getId()).getNumCustLocations(), 1);
        assertNotEquals(repo.get(epl_3.getId()).getNumCustLocations(), 1);


        assertNull(repo.update(epl_4));    // update non-existent cos
        assertEquals(repo.count(), 3);
        assertEquals(repo.get("epl-4").getCos(), gold.getId());

        epl_4.setCos(silver.getId());
        assertNotNull(repo.update(epl_4)); // update existing svc, same object
        assertEquals(repo.get(epl_4.getId()).getCos(), silver.getId());

        Epl epl_4_2 = new Epl();
        epl_4_2.setAllProps("epl-4", 4, locList4, uniList4, ipList4, bronze.getId(), "unset");
        assertNotNull(repo.update(epl_4_2)); // update svc, new object
        assertEquals(repo.get(epl_4_2.getId()).getNumCustLocations(), 4);
    }
}
