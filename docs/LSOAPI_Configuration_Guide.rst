======================================================
Connectivity Services LSO (LSOAPI) Configuration Guide
======================================================

..toctree::
	:caption: Table of Contents
	:numbered:
	:maxdepth:  3

Configuration Guide
-----------------------------
Installation and configuration information for Connectivity Services LSO APIs 
and their implementations for Brahmaputra is provided in this file, which is 
the LSOAPI_Configuration_Guide.rst file in the docs folder in the LSOAPI project 
branch of the Brahmaputra Git repository. The LSOAPI project configuration guide
is a reproduction of the README.MD file in the root lsoapi folder.


Virtualized Business CPE Services Demo
--------------------------------------
The Connectivity Services LSO APIs and their implementations are part of a 
proof-of-concept demonstration for the provisioning of Ethernet Private Line
(EPL) service using OpenDaylight as the SDN controller. The PoC demo, referred 
to as the Virtualized Business CPE (VCPE) demo, is installed and configured as
described below.

* There are 3 service layers in the VCPE demo, each runs independently
    - Ethernet Services Manager API (svcmgr)
    - Ethernet Virtual Connection Manager API (evcmgr)
    - Class of Service Manager API (cosmgr)
* The build creates war files for each that are deployable on tomcat
* The code is housed in the LSOAPI project gerrit repository: 
    https://gerrit.opnfv.org/gerrit/lsoapi 
* All development and testing to date have been on OSX

Environment Requirements:
^^^^^^^^^^^^^^^^^^^^^^^^^^
* JDK for Java 7
* Tomcat 8
* Maven 3.3

If you need help with environement set-up instructions, see "Environment Set Up"
notes at the bottom of this document

Building and Deploying Connectivity Services LSO API Implementations
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Assumptions
*  You have an account on the LSOAPI gerrit server
*  Your environment has been completely set up as described at the bottom of 
   this document

    $ git clone ssh://<your opnfv id>@gerrit.opnfv.org:29418/lsoapi 
    $ cd lsoapi
    $ chmod +x ./deploy.sh
    $ ./deploy.sh

* This will build cosmgr.war, evcmgr.war, and svcmgr.war, copy them to your 
  tomcat webapps directory, and start tomcat
* If you prefer to set up manually you can follow the following steps
    $ cd lsoapi
    $ mvn clean install
    $ cp ./cos/cosmgr/target/cosmgr.war /Library/Tomcat/webapps/.
    $ cp ./evc/evcmgr/target/evcmgr.war /Library/Tomcat/webapps/.
    $ cp ./svc/svcmgr/target/svcmgr.war /Library/Tomcat/webapps/.
    $ /Library/Tomcat/bin/startup.sh

* The 3 service managers are now running, and will expect/send REST calls 
  on port 9090

Running the OpenDaylight UNI Manager Emulator
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
* The Connectivity Services LSO APIs implementations are designed to call
  the OpenDaylight UNI Manager plug-in.
* If you would like to validate the LSOAPI implementation without running the 
  ODL UNI Manager plug-in, you can do so by running the ODL UNIMgr emulator.
    - This is a nodejs app that is listening for ODL specific REST calls, and 
      when recieved, it prints the deatils of the call to the console
* In order to run the unimgr emulator, you will need to install nodejs and npm.  
  See "Install node/npm" in the "Environment Set Up" section at the bottom of 
  this document for more details.
* Before running the emulator, make sure that the ODL plugin is not running (or
  you will likely get a port conflict on 8181)
* Build the runtime environement, and Start the emulator as follows:

    $ cd lsoapi/uni/unimgr
    $ npm install (if node_modules does not exist)
    $ node uniMgrEmu.js

* Any REST calls targted to ODL will now be responded to by the ODL emulator,  
  and logged to the console in which the emulator was started

Environment Set Up
------------------------

Install JDK
^^^^^^^^^^^^^^
**OSX:**
* Because OpenDaylight requires Java 7, all md-proto development has been 
  against Java 7, so that all components (ODL based, and non ODL based) will be 
  able to run on the same machine if needed
* The md-proto development has been against JDK version 79
    - Download installation package from here:
        http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html
* Tomcat (and other applications) will require a JAVA_HOME environment variable.  
  A convenient way to make sure that it is created correctly is to add the 
  following to your .bashrc
    - export JAVA_HOME=$(/usr/libexec/java_home)

Install Tomcat
^^^^^^^^^^^^^^^^^^
**OSX:**
* We are using Tomcat 8, which is the latest version that support Java7
* Download tar.gz installation package from here
    - https://tomcat.apache.org/download-80.cgi
    - note: md-proto dev was against v 8.022

* Assuming the tar.gz file is in your ~/Downloads folder:

    $ sudo mkdir -p /usr/local (if /usr/local does not already)
    $ cd /usr/local
    $ sudo tar -xvf ~/Downloads/apache-tomcat-8.0.22.tar.gz (or other version)

* For convenience, and to make it simpler to replace this with newer versions 
 you can create a link /Library/Tomcat and point to the version specific tomcat 
 directory (the instructions below assume, and the deployment script assume 
 you have done so)

    $ sudo rm -f /Library/Tomcat (if the link already exists)
    $ sudo ln -s /usr/local/apache-tomcat-8.0.22 /Library/Tomcat
    $ sudo chown -R -H <your_username> /Library/Tomcat
    $ sudo chmod +x /Library/Tomcat/bin/*.sh

* All of the vcpe services sent to  9090, so make sure that your 
  /Library/Tomcat/conf/server.xml file is configured to have tomcat listen 
  on 9090.

    <Connector port="9090" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" />

Install Maven
^^^^^^^^^^^^^^^^^^
**OSX:**
* Maven can be installed on OSX using brew
    - brew is a package manager for OSX
    - if you don't have brew installed, do so as described here
  http://coolestguidesontheplanet.com/installing-homebrew-os-x-yosemite-10-10-package-manager-unix-apps/

* Now, install maven:

    $ brew install maven


Install node/npm
^^^^^^^^^^^^^^^^^^^^^
**OSX:**
* If you plan on running the UNI ODL emluator you will need to install node/npm
* Download and execute the Nodejs Mac OS X Installer (.pkg) from
    https://nodejs.org/download/
