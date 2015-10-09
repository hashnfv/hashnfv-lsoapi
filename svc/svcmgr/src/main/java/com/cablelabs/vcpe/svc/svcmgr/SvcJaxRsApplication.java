/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.svc.svcmgr;

import org.glassfish.jersey.server.ResourceConfig;

//
// In order to avoid CORS issues, register our CORS Response filter
//

public class SvcJaxRsApplication extends ResourceConfig {

    /**
     * Register JAX-RS application components.
     */
    public SvcJaxRsApplication() {
        packages("com.cablelabs.vcpe.cos.cosmgr");
		register(CORSResponseFilter.class);
    }
}