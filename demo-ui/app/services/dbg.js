/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

(function(){
  
   var dbg = function ($log) {

      var dbgFlag = true;

      var on  = function()    { dbgFlag = true; };
      var off = function()    { dbgFlag = false; };

      var p   = function(str, tab, emphasize) { 
         if ( dbgFlag ) 
         {
            var tabStr = "";
            if (tab) {
              for (var i = 0; i < tab; i++ )
                tabStr += "    ";
            }
            var preStr = "... " + tabStr;
            var postStr = "";
            if ( emphasize ) {
              preStr = tabStr + "####################### ";
              postStr = " ####################### ";
            }                                  
            $log.info(preStr + str + postStr);
         }
      };

      var pj  = function(obj) { 
         if ( dbgFlag ) 
            $log.info(JSON.stringify(obj,null,3)) 
      };

      var e = function(eMsg) {
        $log.error("!! ERROR: " + eMsg);
      }

      var w = function(eMsg) {
        $log.warn("!! WARNIING: " + eMsg);
      }

      return {      // Public API
        on  : on,   //   squelch DBG printing
        off : off,  //   enable DBG printing
        p   : p,    //   print a debug string
        pj  : pj,   //   print JSON version of an object
        w   : w,    //   print an warning msg
        e   : e     //   print an error msg
      };
   };
  
   var module = angular.module("vcpe");
   module.factory("dbg", dbg);

}());