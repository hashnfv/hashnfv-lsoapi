package com.cablelabs.vcpe.evc.evcmgr;

import org.glassfish.jersey.server.ResourceConfig;

//
// In order to avoid CORS issues, register our CORS Response filter
//

public class EvcJaxRsApplication extends ResourceConfig {

    /**
     * Register JAX-RS application components.
     */
    public EvcJaxRsApplication() {
        packages("com.cablelabs.vcpe.cos.cosmgr");
		register(CORSResponseFilter.class);
    }
}