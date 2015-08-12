===================================
Virtual Business CPE Demo UI Portal
===================================
* This portal communicates with the vcpe service manager defined in vcpe-services repo (cosmgr, evcmgr, svcmgr)
* It enbles the creation of Service Levels (Cos) and Eth Private Lines (EPL)
* As part of the EPL creation, EVCs and UNIs are also created 
* Currently the code is housed in private VCPE gerrit repo: 
    http://euca-10-5-5-106.cirrus.cloud.cablelabs.com:8080/#/admin/projects/vcpe-ui
* All development and testing have been on OSX
* The portal currently must be run within Chrome

Environment Requirements:
=========================
* node.js / npm
* bower
* grunt
* less

If you need help with environement set-up instrucitons, see "Environment Set Up" notes at the bottom of this document

Building the runtime environement:
==================================
* Once your environemnt set up (per Environment Set Up instructions at the bottom of this document)

    $ git clone http://yourusername@euca-10-5-5-106.cirrus.cloud.cablelabs.com:8080/vcpe-ui
    $ cd vcpe-ui
    $ npm install
        - reads package.json to install dependencies for the vcpeUiServer.js node app
        - dependencies are installed in ./node_modules directory
    $ bower install
        - reads bower.json to install dependencies for the webapp (angular,etc)
        - dependencies are installed in ./bower_components directory
    $ grunt
        - reads gruntfile.js and carries out specified "build" steps
        - in our case, we compile the .less file to .css

Starting the web app
===================
* Start the vcpe server managers (so the app has someting to talk to)
    - see README.md in vcpe-services repo
* The webapp can be served by any web server. vcpeUiServer.js is a nodejs app that can be used as an easy way to serve the webapp.  
* Start the server (within the vcpe-ui directory)

    $ node vcpeUiServer.js [portnum] &

     NOTES: 
        - portnum is optional, and if not provided the app will be served on port 4000
        - An issue that you may run into when you start the server is a port conflict if the server is already running from a previous session (Error: listen EADDRINUSE).  To solve the issue run 'pkill node', and then start the server.
        
* Bring up the web app
    - To render properly, Chrome must be used (safari has an html flexbox issue, to be fixed in the future)
    - In the chrome address bar enter the following

    http://localhost:4000/app/views/vcpe-portal.html

Using the web app
===================
* Config the app via the vcpe-ui/app/config.json file.  This allows configuration of:
    - Communications with the service managers (cosmgr, evcmgr, eplmgr)
    - UNI's to be used for Eth Private Line creation
* You will need to start the VCPE services (cosmgr, evcmgr, eplmgr) in order for the web app to run.  Please refer to vcpe-services/README.md for instrutcioons on buiding and running these services
* In order for the web app to work, either the vcpe ODL plugin, or the ODL emulator must be running, to handle ODL rest call sent by the UI.
    - To run the ODL Uni Manager Emulator, please see the section "Running the ODL Uni Manager Emulator" section the vcpe-services/README.md
    - To run the ODL plugin please see the vcpe-odl repsoitory
* Start the app:  http://localhost:4000/app/views/vcpe-portal.html
* Two panels will be visible
    - Service Levels (CoS in MEF Speak)
    - Ethernet Private Lines
* w/in each panel you can CRUD Service Levels (CoS) and Eth Private Lines (EPL)
    - creation (+), modification (/) and delete (-)
* Notes
    - Service levels must be created before creating Eth Private Lines
    - Eth private lines present drop down boxes with info as configured in config.json and created service levels

Debugging the Web App
=====================
* The webapp provides farily extensive logging of events and issues
* To see the logging and reported errors
    - within the chrome menu: view/developer/developer tools

To Look Behind the Scenes
=========================
* The web portal sends REST messages to CoS MGR, evc MGR, and epl MGR
* Using postman (or similar tool), you can easily see a list of MEF entities that have been created w/in each of the service layers:
    - NOTES: 
        header: Accept application/json
        body: no body required
    - To get list of created MEF entities (no request body required)
        cos: GET http://localhost:9090/cosmgr/webapi/cos/list
        evc: GET http://localhost:9090/evcmgr/webapi/evc/list
        epl: GET http://localhost:9090/svcmgr/webapi/svc/epl/list

==================
Environment Set Up
==================

Install node/npm
================
**OSX:**
* Download and execute the Nodejs Mac OS X Installer (.pkg) from
    https://nodejs.org/download/

**Windows/Cygwin:**
TBD

**Linux:**
TBD

Install Bower
=============
**OSX:**
 * Bower is a web front end dependency mangament tool
 * Once you have installed nodejs/npm, install bower as follows
 
    $ sudo npm install -g bower

**Windows/Cygwin:**
TBD

**Linux:**
TBD

Install Grunt Client
====================
**OSX:**
 * Grunt is a web front end build utility
 * Once you have installed nodejs/npm, install grunt client as follows
 
    $ sudo npm install -g grunt-cli

**Windows/Cygwin:**
TBD

**Linux:**
TBD

Install Less
====================
**OSX:**
 * Less is a css enhancment environement
 * Once you have installed nodejs/npm, install less as follows
 
    $ sudo npm install -g less

**Windows/Cygwin:**
TBD

**Linux:**
TBD
