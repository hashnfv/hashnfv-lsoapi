/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

(function(){
  
   var mefServices = function ($http, dbg) {

       var _evcBasePath = "/evcmgr/webapi/evc"
       var _evcUrl      = "unset";

       var _uniBasePath = "/restconf/config/network-topology:network-topology/topology/unimgr:uni/node"
       var _uniUrl      = "unset";

       var _evcPathBasePath = "/restconf/config/network-topology:network-topology/topology/unimgr:evc/link"
       var _evcPathUrl      = "unset";

       //
       // Configuration services
       // 

       var setEvcUrl = function (evcMgrCfg) {
         dbg.p("in setEvcUrl");
         _evcUrl = "http://"+evcMgrCfg.ip+":"+evcMgrCfg.port+_evcBasePath;
         dbg.p(_evcUrl);
       }

       var setUniUrl = function (uniMgrCfg) {
         dbg.p("in setUniUrl");
         _uniUrl = "http://"+uniMgrCfg.ip+":"+uniMgrCfg.port+_uniBasePath;
         dbg.p(_uniUrl);
       }

       var setEvcPathUrl = function (evcPathMgrCfg) {
         dbg.p("in setEvcPathUrl");
         _evcPathUrl = "http://"+evcPathMgrCfg.ip+":"+evcPathMgrCfg.port+_evcPathBasePath;
         dbg.p(_evcPathUrl);
       }

       //
       // REST services
       // 

      var getEvc = function(evcId) {
         dbg.p("in mefServices.getEvc()",2)
         var url = _evcUrl + "/" + evcId;
         dbg.p("GET: " + url, 2);
         return $http.get(url)
                     .then(function(response){ return response.data; });
      };

      var getUni = function(uniId) {
         dbg.p("in mefServices.getUni()",2)
         var url = _uniUrl + "/" + uniId;
         dbg.p("GET: " + url, 2);
         // return $http.get(url)
         //             .then(function(response){ return response.data; });
     		 return $http({method: 'GET', url: url, 
                       headers: {'Authorization': 'Basic YWRtaW46YWRtaW4='}})
                       .then(function(response){ return response.data; });
      };

      return { // public API
               setEvcUrl     : setEvcUrl,
               setUniUrl     : setUniUrl,
               setEvcPathUrl : setEvcPathUrl,
               getEvc        : getEvc,
               getUni        : getUni             
      };
   };
  
   var module = angular.module("vcpe");
   module.factory("mefServices", mefServices);

}());