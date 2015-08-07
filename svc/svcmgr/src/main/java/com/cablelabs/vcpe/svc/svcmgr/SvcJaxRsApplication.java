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