package com.cablelabs.vcpe.svc.svcbase.repository;

import com.cablelabs.vcpe.common.Dbg;
import com.cablelabs.vcpe.svc.svcbase.model.Epl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by steve on 5/24/15.
 */


/*
    Singleton that contains a hashmap which holds instances of Epl objects, indexed by CiS ID
 */

public enum EplRespositoryInMem implements EplRespository {
    INSTANCE; // Singleton

    private Map<String, Epl> svcDB = new ConcurrentHashMap<>();

    @Override
  //--------------------------------------------------------
    public Epl add(Epl epl)
  //--------------------------------------------------------
    {
        if ( this.get(epl.getId()) != null ) {
            return null;
        }
        svcDB.put(epl.getId(), epl);
        return epl;
    }

    @Override
  //--------------------------------------------------------
    public Epl get(String svcId) {
        return svcDB.get(svcId);
    }
  //--------------------------------------------------------

    @Override
  //--------------------------------------------------------
    public Epl update(Epl epl)
  //--------------------------------------------------------
    {
        // put returns null if epl did not exist, other returns epl as it stood prior to put
        return svcDB.put(epl.getId(), epl);
    }

    @Override
  //--------------------------------------------------------
    public Epl delete(String svcId)
  //--------------------------------------------------------
    {
        // remove returns null if svc did not exist, other returns svc as it stood prior to remove
        return svcDB.remove(svcId);
    }

    @Override
  //--------------------------------------------------------
    public int count() {
        return svcDB.size();
    }
  //--------------------------------------------------------

    @Override
  //--------------------------------------------------------
    public List<Epl> getAll()
  //--------------------------------------------------------
    {
        List<Epl> eplList = new ArrayList<Epl>(svcDB.values());
        return eplList;
    }

    @Override
  //--------------------------------------------------------
    public void dump(int tab)
  //--------------------------------------------------------
    {
        Dbg.p(tab, "Epl Repo: " + svcDB.size() + " entrie(s)");
        int numSvc = 0;
        for (Epl curEpl : svcDB.values()) {
            numSvc++;
            Dbg.p(tab+1, "<Entry " + numSvc+">");
            curEpl.dump(tab+2);
        }
    }
}
