package com.cablelabs.vcpe.cos.cosmgr;

import org.glassfish.jersey.server.ResourceConfig;

//
// In order to avoid CORS issues, register our CORS Response filter
//

public class CoSJaxRsApplication extends ResourceConfig {

    /**
     * Register JAX-RS application components.
     */
    public CoSJaxRsApplication() {
        packages("com.cablelabs.vcpe.cos.cosmgr");
		register(CORSResponseFilter.class);
    }
}