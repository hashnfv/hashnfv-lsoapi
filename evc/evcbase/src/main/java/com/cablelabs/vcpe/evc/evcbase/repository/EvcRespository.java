package com.cablelabs.vcpe.evc.evcbase.repository;

import com.cablelabs.vcpe.evc.evcbase.model.Evc;

import java.util.List;

/**
 * Created by steve on 5/25/15.
 */
public interface EvcRespository
{
    // TODO add exceptions

    Evc add(Evc evc);          // returns null if already exists, otherwise returns stored evc
    Evc get(String evcId);     // returns null if not found, otherwise stored evc
    Evc update(Evc evc);       // returns null if did not exit, otherwise evc as it was previous to update (put in any case)
    Evc delete(String evcId);    // returns null if not found, otherwise evc as it was previous to delete
    int count();               // number of Evc stored in the repo
    public void dump(int tab); // print out contents of the repo
    List<Evc> getAll();
}
