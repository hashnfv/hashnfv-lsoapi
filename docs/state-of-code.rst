============
Introduction
============

This is a proof of concept prototype implementation enabling the creation and management of MEF EPL Services.

Being a proof of concept prototype, there are some areas which have received only superficial coverage or no coverage at all.  Following are items that should be addressed when evolving this to a production oriented implementation

1. The current test suite is reasonable, but exhaustive.  Production targeted version should have more thourough test coverage
2. Exception handling is only superficially implemented.  A production system should include a more robust exception handling and error reporting architecture
3. Only a subset of the MEF Attributes have been implemented.  Long term the goal is to evolve this code base to fully support all MEF paramaters
4. We are only implementing a portion of the data model for the sake of expediency.
    - Only EPL service is supported at the moment
    - Currently, the service repo is hard coded to hold EPL (we have not implemented the more general MEFSvc and Eline/ELine/Tree classes that in the future will also reside in the repo).
        - The MEF service hierarchy (MEFSvc->Eline->EPL) need to be architected and implemented
        - The SvcMgr needs to be architected in order to handle multiple service types from a REST API perspective and from a persitance perspective
    - the REST URL/path architecture is currently hard coded, should be configurable at some point (config file, etc).
5. There has not been a definitive analysis of which specific service attributes are recieved, generated, or queried at each service layer.
    - It would be very useful to do this analysis, and then reflect in the code through structure and documentation, which attributes are set/queried at each layer

==============
Specific To Do
==============
1. Who supplies/generates evcMaxSvcFrameSize?  Currently hard coded to 1600 in EplService.create()
2. Make sure for all XXXClient.create(xxx), that we capture the returned version, in case the service layer modified it
    - for example epl = eplClient.create(epl)
3. Check for nulls when prior to printing out lists in dump() fxns
4. Unique EVC IDs are being generated via an internal counter.  This of course does not scale nor survive restart.  A proper unique ID generation approach will be needed for production.
