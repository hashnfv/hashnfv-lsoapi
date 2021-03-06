/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.svc.svcbase.repository;

import com.cablelabs.vcpe.svc.svcbase.model.Epl;

import java.util.List;

/**
 * Created by steve on 5/25/15.
 */
public interface EplRespository
{
    // TODO add exceptions

    Epl add(Epl epl);          // returns null if already exists, otherwise returns stored epl
    Epl get(String svcId);     // returns null if not found, otherwise stored svc
    Epl update(Epl epl);       // returns null if did not exit, otherwise epl as it was previous to update (put in any case)
    Epl delete(String svcId);    // returns null if not found, otherwise svc as it was previous to delete
    int count();               // number of Epl stored in the repo
    public void dump(int tab); // print out contents of the repo
    List<Epl> getAll();
}
