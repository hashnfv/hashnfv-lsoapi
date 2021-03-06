/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.cos.cosbase.repository;

import com.cablelabs.vcpe.cos.cosbase.model.CoS;

import java.util.List;

/**
 * Created by steve on 5/25/15.
 */
public interface CoSRespository
{
    // TODO add exceptions

    CoS add(CoS cos);          // returns null if already exists, otherwise returns stored cos
    CoS get(String cosId);     // returns null if not found, otherwise stored cos
    CoS update(CoS cos);       // returns null if did not exit, otherwise cos as it was previous to update (put in any case)
    CoS delete(String cosId);  // returns null if not found, otherwise cos as it was previous to delete
    int count();               // number of CoS stored in the repo
    List<CoS> getAll();

    public void dump(int tab); // print out contents of the repo
}
