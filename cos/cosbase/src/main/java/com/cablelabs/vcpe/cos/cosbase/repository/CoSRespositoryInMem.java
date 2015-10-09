/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.cos.cosbase.repository;

import com.cablelabs.vcpe.common.Dbg;
import com.cablelabs.vcpe.cos.cosbase.model.CoS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by steve on 5/24/15.
 */


/*
    Singleton that contains a hashmap which holds instances of CoS objects, indexed by CiS ID
 */

public enum CoSRespositoryInMem implements CoSRespository {
    INSTANCE; // Singleton

    private Map< String, CoS> cosDB = new ConcurrentHashMap<String, CoS>();

    @Override
    public CoS add(CoS cos) {
        if ( this.get(cos.getId()) != null ) {
            return null;
        }
        cosDB.put(cos.getId(), cos );
        return cos;
    }

    @Override
    public CoS get(String cosId) {
        return cosDB.get(cosId);
    }

    @Override
    public CoS update(CoS cos) {
        // put returns null if cos did not exist, other returns cos as it stood prior to put
        return cosDB.put(cos.getId(), cos);
    }

    @Override
    public CoS delete(String cosId) {
        // remove returns null if cos did not exist, other returns cos as it stood prior to remove
        return cosDB.remove(cosId);
    }

    @Override
    public List<CoS> getAll() {
        List<CoS> cosList = new ArrayList<CoS>(cosDB.values());
        return cosList;
    }

    @Override
    public int count() {
        return cosDB.size();
    }

    @Override
    public void dump(int tab) {

        Dbg.p(tab, "CoS Repo: " + cosDB.size() + " entrie(s)");
        int numCos = 0;
        for (CoS curCos : cosDB.values()) {
            numCos++;
            Dbg.p(tab+1, "<Entry " + numCos+">");
            curCos.dump(tab+2);
        }
    }

}
