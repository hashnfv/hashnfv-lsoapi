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
 
var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(express.static("."));

// defaults
var PORT = 4000;

//  grap the command line arguments
var args = process.argv.slice(2);
if ( args[0] ) {
	PORT = args[0];
}

app.listen(PORT);
console.log('Running on http://localhost:' + PORT);