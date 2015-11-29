====================================================
REST Clients Notes : CoSClient, EvcClient, EplClient
====================================================
1. Provide rest communications to cosmgr, evcmgr, and eplmgr repsectivly.
   - provide create/request/update/delete capabilities
   - these services must be running within tomcat
   - each has it's own separate war file
2. Creating resources
   - The create() methods take cos/epl/evc objects as inputs
   - Not all inputs are known at time of resource creation, and the create service itself will set some of the parameters, and the all return object of the type that was created which will capture any values set within the create service (in addition to remembering the values originally supplied)
   - You must capture the object returned by the create function, as there will be values that were set during that proccess
