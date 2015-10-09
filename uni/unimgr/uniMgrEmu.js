/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/


// Create express HTTP server app
var express = require('express');
var app = express();
 
// have express parse http request JSON bodies into POJO's
var bodyParser = require('body-parser');
app.use(bodyParser.json());

// local modules
var dbg = require('./dbg.js');

// enable cross domain requests
app.all('*', function(req, res, next) {
             res.header("Access-Control-Allow-Origin", "*");
             res.header("Access-Control-Allow-Headers", "X-Requested-With");
             res.header('Access-Control-Allow-Headers', 'Content-Type');
             res.header('Access-Control-Allow-Headers', 'Authorization');             
             res.header('Access-Control-Allow-Methods', 'POST, GET, PUT, DELETE, OPTIONS');
             next();
});


// ---------------------------------------------
// UNI Manager Services
// ---------------------------------------------
// PUT: Create/update UNI
//      /restconf/config/cl-vcpe-mef:unis/uni/    
//      BODY:
//          {
//              "uni": {
//                "uni:id": "822f7eec-2b35-11e5-b345-feff819cdc9f",
//                  "speed": {
//                      "speed-1G": 1
//                  },
//                  "uni:mac-layer": "IEEE 802.3-2005",
//                  "uni:physical-medium": "UNI TypeFull Duplex 2 Physical Interface",
//                  "uni:mtu-size": 0,
//                  "uni:type": "",
//                  "uni:mac-address": "68:5b:35:bb:f8:3e",
//                  "uni:ip-address": "192.168.0.22",
//                  "uni:mode": "Full Duplex"
//              }
//          }
//
// GET: Query UNI
//      /restconf/operational/cl-vcpe-mef:unis/uni/
//      RESPONSE:
//          {
//           "uni": [
//             {
//               "id": "822f7eec-2b35-11e5-b345-feff819cdc9f",
//               "type": "",
//               "speed": {
//                 "speed-1G": [
//                   null
//                 ]
//               },
//               "ip-address": "192.168.1.30",
//               "physical-medium": "UNI TypeFull Duplex 2 Physical Interface",
//               "mode": "Full Duplex",
//               "mac-address": "08:00:27:35:64:90",
//               "mtu-size": 0,
//               "mac-layer": "IEEE 802.3-2005"
//             }
//           ]
//         }
//
//
// DELETE: Delete a UNI
//      /restconf/config/cl-vcpe-mef:unis/uni/
//
// ---------------------------------------------


var uniMgrOpPath = "/restconf/operational/cl-vcpe-mef:unis/uni/";
var uniMgrOpId   = uniMgrOpPath+":uniId"

var uniMgrCfgPath = "/restconf/config/cl-vcpe-mef:unis/uni/";
var uniMgrCfgId   = uniMgrCfgPath+":uniId"

dbg.p("uni Op  REST path : " + uniMgrOpPath);
dbg.p("uni Op  REST id   : " + uniMgrOpId);
dbg.p("uni Cfg REST path : " + uniMgrCfgPath);
dbg.p("uni Cfg REST id   : " + uniMgrCfgId);


// We will maintain a map of UNI's created
var uniMap = {};


var removeUniNameSpace = function(uniIn ) {
    //dbg.p("in removeUniNameSpace()");
    var uniOut = {};
    
    // remove pesky ODL namespace qualifiers, as they are not present in ODL responses
    uniOut["uni"] = 
    [ 
        {
            "id"              : uniIn["uni"]["uni:id"],
            "speed"           : uniIn["uni"]["speed"],
            "mac-layer"       : uniIn["uni"]["uni:mac-layer"],
            "physical-medium" : uniIn["uni"]["uni:physical-medium"],
            "mtu-size"        : uniIn["uni"]["uni:mtu-size"],
            "type"            : uniIn["uni"]["uni:type"],
            "mac-address"     : uniIn["uni"]["uni:mac-address"],
            "ip-address"      : uniIn["uni"]["uni:ip-address"],
            "mode"            : uniIn["uni"]["uni:mode"]
        }
    ]

    // dbg.p("Clean uni"); dbg.pj(uniOut);
    return uniOut;
}    

// Create/update a UNI
// ---------------------------------------------
app.put( uniMgrCfgId, function(req, resp){
    dbg.p ("... [" + dbg.curTime() + "] made it to PUT: " + uniMgrCfgPath + req.params.uniId );

    var uni = req.body;
    dbg.p("Creating /Updating Uni: ");
    dbg.pj(uni);

    uniClean = removeUniNameSpace(uni);
    uniMap[uniClean.uni[0].id] = uniClean;

    // dbg.p("uni map after push of clean"); dbg.pj(uniMap);
    resp.send( { "message": 
                 "... made it to PUT: " + uniMgrCfgId } );
});



// Query UNI Info
// ---------------------------------------------
app.get( uniMgrOpId, function(req, resp){

    var uniId = req.params.uniId;
    dbg.p ("... [" + dbg.curTime() + "] made it to GET: " + uniMgrOpPath + uniId );

    var uniToReturn = null;
    if ( uniMap[uniId] )
            uniToReturn =  uniMap[uniId];
    else
            uniToReturn = { "error" : "uni not in the DB: " + uniId }

    resp.send( uniToReturn );
});

// Delete UNI
// ---------------------------------------------
app.delete( uniMgrCfgId, function(req, resp){
    var uniId = req.params.uniId;
    dbg.p ("... [" + dbg.curTime() + "] made it to DELETE: " + uniMgrCfgPath + uniId );

    // dbg.p("uni map prior to delete");
    // dbg.pj(uniMap);
    delete uniMap[uniId];
    // dbg.p("-----------------------------------");
    // dbg.p("uni map after delete");
    // dbg.pj(uniMap);

    resp.send( { "message": 
                 "... made it to DELETE: " + uniMgrCfgPath + uniId } );
});

// ---------------------------------------------
// EVC Path Services
// ---------------------------------------------
// POST/PUT: Create/update UNI
//      /restconf/operational/cl-vcpe-mef:evcs/evc/
//      BODY:
//      {
//         "evc":
//         {
//             "evc:id": "822f8284-2b35-11e5-b345-feff819cdc9f",
//             "evc:uni-dest":
//             [
//                 {
//                     "order": 0,
//                     "uni": "822f7eec-2b35-11e5-b345-feff819cdc9f"
//                 }
//             ],
//             "evc:uni-source":
//             [
//                 {
//                     "order": 0,
//                     "uni": "111f7eec-2c35-11e5-b345-feff819cdc9f"
//                 }
//             ],
//             "evc:cos-id": "string",
//             "evc:ingress-bw":
//             {
//                 "speed-1G": {}
//             },
//             "evc:egress-bw":
//             {
//                 "speed-1G": {}
//             }
//         }
//      }
//
// ---------------------------------------------

var evcMgrCfgPath = "/restconf/config/cl-vcpe-mef:evcs/evc/";
var evcMgrCfgId   = evcMgrCfgPath+":evcId"
dbg.p("evc cfg REST path : " + evcMgrCfgPath);
dbg.p("evc cfg REST id   : " + evcMgrCfgId);

// We will maintain a map of EVCs created
var evcMap = {};


// Create/update  EVC Path
// ---------------------------------------------
app.put( evcMgrCfgId, function(req, resp){
    var evcId = req.params.evcId;
    dbg.p ("... [" + dbg.curTime() + "] made it to PUT: " + evcMgrCfgPath + evcId );
    dbg.p("req body = ");
    dbg.pj(req.body);
    dbg.p("----------------------------------------------------")
    resp.send( { "message": 
                 "... made it to PUT: " + evcMgrCfgId } );
});


// Query EVC Info
// ---------------------------------------------
// TBD


// Delete EVC
// ---------------------------------------------
app.delete( evcMgrCfgId, function(req, resp){
    var evcId = req.params.evcId;
    dbg.p ("... [" + dbg.curTime() + "] made it to DELETE: " + evcMgrCfgPath + evcId );

    // currently not keeping a map of EVCs, just acknowledge recept of REST msg
    resp.send( { "message": 
                 "... made it to DELETE: " + evcMgrCfgPath + evcId } );
});


var PORT = 8181;
app.listen(PORT);
console.log('Running on http://localhost:' + PORT);
