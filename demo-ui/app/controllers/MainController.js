/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

// Create our module
var app = angular.module("vcpe", []);

(function() {
var MainController = function($http, $log, cosServices, eplServices, mefServices, model, dbg ) {

	dbg.p("---> in MainController()");

	// config our app
	var cfgFile = "../config.json";
	$http.get(cfgFile).
	  success(function(data) {
			dbg.p("in cfgFile read callback");
			dbg.p("incoming data");
			dbg.pj(data);
			cosServices.setCosUrl(data.cosMgr);
			eplServices.setEplUrl(data.eplMgr);
			mefServices.setEvcUrl(data.evcMgr);
			mefServices.setUniUrl(data.uniMgr);
			mefServices.setEvcPathUrl(data.evcPathMgr);
			model.setAvailableUnis(data.uniList);
	  }).
	  error(function() {
	  		dbg.e("Could not read " + cfgFile);
	  });
};

// register controller in the module
app.controller("MainController", MainController);

}());