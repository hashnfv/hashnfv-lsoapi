/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.evc.evcbase.repository;

import com.cablelabs.vcpe.common.Dbg;
import com.cablelabs.vcpe.evc.evcbase.model.Evc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by steve on 5/24/15.
 */


/*
    Singleton that contains a hashmap which holds instances of Evc objects, indexed by CiS ID
 */

public enum EvcRespositoryInMem implements EvcRespository {
    INSTANCE; // Singleton

    private Map<String, Evc> evcDB = new ConcurrentHashMap<>();

    @Override
  //--------------------------------------------------------
    public Evc add(Evc evc)
  //--------------------------------------------------------
    {
        if ( this.get(evc.getId()) != null ) {
            return null;
        }
        evcDB.put(evc.getId(), evc );
        return evc;
    }

    @Override
  //--------------------------------------------------------
    public Evc get(String evcId) {
        return evcDB.get(evcId);
    }
  //--------------------------------------------------------

    @Override
  //--------------------------------------------------------
    public Evc update(Evc evc)
  //--------------------------------------------------------
    {
        // put returns null if evc did not exist, other returns evc as it stood prior to put
        return evcDB.put(evc.getId(), evc);
    }

    @Override
  //--------------------------------------------------------
    public Evc delete(String evcId)
  //--------------------------------------------------------
    {
        // remove returns null if evc did not exist, other returns evc as it stood prior to remove
        return evcDB.remove(evcId);
    }

    @Override
  //--------------------------------------------------------
    public int count() {
        return evcDB.size();
    }
  //--------------------------------------------------------

    @Override
  //--------------------------------------------------------
    public List<Evc> getAll()
  //--------------------------------------------------------
    {
        List<Evc> evcList = new ArrayList<Evc>(evcDB.values());
        return evcList;
    }

    @Override
  //--------------------------------------------------------
    public void dump(int tab)
  //--------------------------------------------------------
    {
        Dbg.p(tab, "Evc Repo: " + evcDB.size() + " entrie(s)");
        int numEvc = 0;
        for (Evc curEvc : evcDB.values()) {
            numEvc++;
            Dbg.p(tab+1, "<Entry " + numEvc+">");
            curEvc.dump(tab+2);
        }
    }
}
