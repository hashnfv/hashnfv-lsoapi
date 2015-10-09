/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.uni.unibase.client;

import com.cablelabs.vcpe.uni.unibase.model.EvcPath;

//import com.cablelabs.vcpe.cos.cosbase.client.CoSClient;
//import com.cablelabs.vcpe.cos.cosbase.model.CoS;

import com.cablelabs.vcpe.uni.unibase.model.Uni;
import org.junit.Test;

/**
 * Created by steve on 5/28/15.
 */

public class UniClientTest {

    @Test
    public void testAll() throws Exception {

//        CoS gold   = new CoS();
//        gold.setAllProps("gold", 100, 0.99, 17.43, 2.43, 0.01);


        UniClient uniClient = new UniClient();

        Uni uni1 = new Uni();
        uni1.setAllProps("uni-1", Uni.SvcSpeed.ONE_GIG,
                "192.168.1.100", "11:AA:00:00:00:00",
                Uni.PhysMedium.GIG_BASE_T, Uni.MacLayer.IEEE_802_3,
                Uni.SyncMode.ENABLED, Uni.Type.UNITYPE, 1600);
        uniClient.update(uni1);

        Uni uni2 = new Uni();
        uni2.setAllProps("uni-2", Uni.SvcSpeed.HUNDRED_MEG,
                "192.168.1.101", "11:BB:00:00:00:00",
                Uni.PhysMedium.HUNDERED_BASE_T, Uni.MacLayer.IEEE_802_3,
                Uni.SyncMode.ENABLED, Uni.Type.UNITYPE, 1600);

        uniClient.update(uni1);
        uniClient.update(uni2);


        EvcPathClient evcPathClient = new EvcPathClient();
        EvcPath evcPath =  new EvcPath( "new-evc", uni1, uni2,
                                        Uni.SvcSpeed.ONE_GIG,
                                        Uni.SvcSpeed.TEN_GIG,
                                        "Gold" );
        evcPathClient.update(evcPath);
    }
}