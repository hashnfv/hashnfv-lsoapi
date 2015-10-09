/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

(function(){
  
	var dbgFlag = true;
	var pre = ""

	var on  = function() { dbgFlag = true; };
	var off = function() { dbgFlag = false; };

	var p   = function(str, tab) { 
		if ( dbgFlag ) 
		{
			var tabStr = "";
			if (tab) {
			  for (var i = 0; i < tab; i++ )
				 tabStr += "    ";
			}
			console.log(pre + tabStr + str);
		}
	};

	var pj  = function(obj) { 
		if ( dbgFlag ) 
			console.log(JSON.stringify(obj,null,3)) ;
	};

	var e = function(eMsg) {
	  console.log("!! ERROR: " + eMsg);
	}

	var w = function(eMsg) {
	  console.log("** WARNIING: " + eMsg);
	}
		
	// for testing.  Write regaurdless of dbgFlag
	var t   = function(str, tab) { 
		var tabStr = "";
		if (tab) {
		  for (var i = 0; i < tab; i++ )
			 tabStr += "    ";
		}
		console.log(pre + tabStr + str);
	}

function curTime() {

    var date = new Date();

    var hour = date.getHours();
    hour = (hour < 10 ? "0" : "") + hour;

    var min  = date.getMinutes();
    min = (min < 10 ? "0" : "") + min;

    var sec  = date.getSeconds();
    sec = (sec < 10 ? "0" : "") + sec;

    // var year = date.getFullYear();

    // var month = date.getMonth() + 1;
    // month = (month < 10 ? "0" : "") + month;

    // var day  = date.getDate();
    // day = (day < 10 ? "0" : "") + day;

    // return year + ":" + month + ":" + day + ":" + hour + ":" + min + ":" + sec;

    return hour + ":" + min + ":" + sec;


}

	module.exports.curTime  = curTime; // returns str w current time

	module.exports.on  = on;   //   enable DBG printing
	module.exports.off = off;  //   squelch DBG printing 
	module.exports.p   = p;    //   print a debug string
	module.exports.pj  = pj;   //   print JSON version of an object
	module.exports.w   = w;    //   print an warning msg
	module.exports.e   = e;    //   print an error msg
	module.exports.t   = t;    //   print an error msg



}());