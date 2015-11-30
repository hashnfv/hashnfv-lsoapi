
=============================================
Connectivity Services LSO (LSOAPI) User Guide
=============================================
..toctree::
	:caption: Table of Contents
	:numbered:
	:maxdepth:  3

API Configuration Information
-----------------------------
Installation and configuration information for Connectivity Services LSO APIs 
and their implementations for Brahmaputra is provided in the 
LSOAPI_Configuration_Guide.rst file in the docs folder in the LSOAPI 
project branch of the Brahmaputra Git repository.

API Capabilities and Uses
-------------------------
Connectivity Services LSO APIs (short name LSOAPI) provide REST services for 
development of northbound  Business Services applications. The APIs provide a 
payload of attributes defined in Metro Ethernet Forum (MEF) Carrier Ethernet 
services applications and expose capabilities of software defined networking 
(SDN) controller functions allowing applications to configure and activate 
network elements to provide Carrier Ethernet services. LSOAPI uses JSON data 
interchange format to exchange the attributes. Carrier Ethernet services 
defined by MEF include ELine point-to-point service, E-Tree point-to-multipoint 
service and E-LAN multipoint-to-multipoint service.

The APIs receive from a northbound application a request for service, 
identification information for service endpoints and service attributes.  
API implementations can then use the information to make subsequent calls to 
lower layers and take other action including storing the attributes.

For the initial phase of the LSOAPI project included in the OPNFV Brahmaputra 
release, implementations of the APIs were developed to validate the APIs and 
to demonstrate ‘proof-of-concept’ functionality. The implementations, driven by 
a simple Web UI developed for the purpose, use the REST interface of an SDN 
controller to initiate the creation of a GRE tunnel between the endpoints, 
emulating a simple ELine service. API implementation and Web UI code is 
provided in the LSOAPI project branch of the Brahmaputra Git repository.

The initial version of LSOAPI included in OPNFV Brahmaputra release is limited 
in scope in several ways in order to enable a relatively quick proof-of-concept:

- Only Ethernet Private Line (EPL) sub-type of ELine point-to-point service is 
  implemented
- The API implementation actually uses only a subset of the full set of EPL 
  parameters
- Implementation of EPL service consists of creation of a GRE tunnel between 
  two instances of Open vSwitch without class-of-service or bandwidth profile 
  features. 
- The implementation acts on two endpoints only. It does not configure network 
  elements between the endpoints.
- The APIs use REST services provided by an OpenDaylight plug-in only. The 
  implementation does not use services of any other SDN controller.
  
 The initial phase of the LSOAPI project developed three APIs: Ethernet Services
 Manager (ESM), Ethernet Virtual Connection (EVC) Manager, and Class of Service 
 (CoS) Manager.  Description of each API follows. The APIs are also documented 
 in RESTful API Modeling Language (RAML). The file, lsoapi-rest.raml, is 
 provided in the api folder in the LSOAPI project branch of the Brahmaputra Git 
 repository. HTML representation of the APIs as described in the RAML file is 
 linked from the `LSOAPI project documentation page 
 <https://wiki.opnfv.org/lsoapi/documents/>`_. Attributes passed for each 
 operation are listed in the RAML documentation.
 
 The Connectivity Services LSO project intends to evolve and expand the APIs to 
 complete the objective of aligning the APIs with MEF specifications. The 
 project will not necessarily further develop implementations for each of the 
 APIs as they evolve.
 
 Ethernet Services Manager (ESM) API
 ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 The ESM API enables Create, Read, Update and Delete (CRUD) lifecycle management
 for Carrier Ethernet services as defined by Metro Ethernet Forum (MEF). The 
 initial use case implemented for Brahmaputra release is Ethernet Private Line 
(EPL) service.

The ESM API Create operation (POST) takes input from its northbound interface 
for a set of attributes and returns an HTTP Status Code. The current ESM API 
implementation obtains Class of Service ID from the Class of Service Manager API
implementation and uses the services of the Ethernet Virtual Connection (EVC) 
Manager API. The ESM API implementation stores attributes it receives and passes 
some of them to the EVC Manager API.

The ESM Read operation (GET) takes input from its northbound interface for a 
single EPL ID or a list of EPL IDs and returns a set of attributes for each EPL 
ID provided.

The ESM API Update operation (PUT) takes input from its northbound interface for
each of a set of attributes and obtains Class of Service ID from the Class of 
Service Manager API implementation. The current ESM API Update implementation 
stores each attribute and passes each one to the EVC Manager API.

The ESM API Delete operation (DELETE) takes input from its northbound interface 
for the EPL ID and returns an HTTP status code. The current ESM API 
implementation initiates deletion of an EPL by calling the EVC API using the 
DELETE operation.
Default values for Ethernet Services Manager attributes are defined in the 
config.json file. Config.json is available in the demo-ui/app folder in the 
LSOAPI project branch of the Git repository.

Ethernet Virtual Connection (EVC) Manager API
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
The EVC Manager API enables Create, Read, Update and Delete (CRUD) lifecycle 
management for the Ethernet Virtual Connection associated with the EPL service 
as defined by Metro Ethernet Forum (MEF) specifications.

The EVC Manager API Create (POST) operation takes input from its northbound 
interface for a set of attributes and returns an HTTP status code. The current 
EVC Manager API implementation obtains Class of Service ID from the Class of 
Service Manager API implementation and uses services of the UNI Manager plug in 
of OpenDaylight SDN Controller to configure two instances of Open vSwitch and 
create a GRE tunnel between them. The current EVC Manager API implementation 
stores all attributes and passes some to the UNI Manager northbound REST 
interface.

The EVC Manager API Read (GET) operation takes input from its northbound 
interface for a single EVC ID or list of EVC IDs and returns an HTTP status code 
and a list of attributes each EVC ID provided.

The EVC Manager Update (PUT) operation takes input from its northbound interface 
for a set of attributes and returns an HTTP status code. The current EVC Manager 
API implementation obtains Class of Service ID from the Class of Service Manager 
API implementation. The EVC Manager implementation passes all parameters to the 
UNI Manager REST interface for the Update operation.

The EVC Manager Delete (DELETE) operation takes input from its northbound 
interface for an EVC ID and returns an HTTP status code. The current EVC Manager
API implementation initiates deletion of the GRE tunnel emulating the EPL by 
calling the UNI Manager northbound REST interface using the DELETE operation.

Class of Service (CoS) Manager API
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
The CoS Manager API enables Create, Read, Update and Delete (CRUD) lifecycle 
management for Class of Service associated with the EPL service as defined by 
Metro Ethernet Forum (MEF) specifications. The CoS parameters define how network 
elements are to handle traffic passed between them including transmission rate,
availability, frame delay, frame loss and jitter. The current LSOAPI CoS 
implementation allows the user to define values for the set of attributes and 
assign a name (identifier) to each unique set of values, stores the CoS sets, 
and passes the CoS name to the ESM API implementation and EVC Manager API 
implementation as requested.

The CoS Manager API Create (POST) operation takes input from its northbound 
interface for a set of attributes and returns an HTTP status code. 

The CoS Manager API Read (GET) operation takes input from its northbound 
interface for a specific CoS ID or a list of CoS IDs and returns HTTP status 
code and attribute values associated with each provided CoS ID.

The CoS Manager API Update (PUT) operation takes input from its northbound 
interface for a specific CoS ID and attributes associated with that ID. The CoS 
API implementation can update its data store and other API implementations with 
changes made to the attributes. The CoS Manager API PUT operation returns an 
HTTP status code.

The CoS Manager API Delete (DELETE) operation takes input from its northbound 
interface for a specific CoS ID and returns an HTTP status code. The current CoS
Manager API implementation deletes all attributes and the CoS ID when it 
receives a request with the Delete operation.

