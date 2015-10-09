/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

(function() {

var MefController = function($scope, $log, mefServices, model, dbg ) {

    //
    // Controller Set Up
    //

    dbg.p("---> in MefController()");
    $scope.currentEpl    = null;
    $scope.currentEplEvc = null;
    $scope.currentEvcUnis = [];

    //
    // Utils
    //

    $scope.curEplEvcToJson = function () {
        return angular.toJson($scope.currentEplEvc, true);
    };


     $scope.uniToSpeedString = function(uni) {
        if (uni) {
            speed = uni.uni[0].speed;
            return Object.getOwnPropertyNames(speed)[0];s            
        }
        else
            return "";
    }
    //
    // HTTP Response Handlers
    //

    var onRequestError = function(reason){
        dbg.e("HTTP Request Problem\n" + JSON.stringify(reason));
    };


    var onUniGetResp = function(data) {
        dbg.p("in onUniGetResp()");
        dbg.pj(data);

        // dbg.p("current EVC UNI list before adding");
        //oSpeedString($scop dbg.pj($scope.currentEvcUnis);

        $scope.currentEvcUnis.push(data);

        // dbg.p("current EVC UNI list after adding");
        // dbg.pj($scope.currentEvcUnis);
    }

    var onEvcGetResp = function(data) {
        dbg.p("in onEvcGetResp()");
        dbg.pj(data);
        $scope.currentEplEvc = data;

        // out with the old unis
        $scope.currentEvcUnis = [];

        // in with the new unis
        mefServices.getUni($scope.currentEplEvc.uniIdList[0])
                   .then(onUniGetResp, onRequestError);
        mefServices.getUni($scope.currentEplEvc.uniIdList[1])
                   .then(onUniGetResp, onRequestError);
    };

    //
    // State query fxns
    //

    $scope.showEvcValues = function () {
        return ( $scope.currentEplEvc != null );
    }

    $scope.showUniValues = function () {
        return ( $scope.currentEvcUnis != null );
    }

    //
    // Watch for change in selected EPL so tht we can
    // update the corresponding EVC info
    //

    eplWatcher = function () { return model.getCurrentEpl(); }
    onEplChange = function (newEpl, oldEpl) {
        dbg.p("in MefController:onEplChange");
        // dbg.p("oldEpl:"); dbg.pj(oldEpl);
        // dbg.p("newEpl:"); dbg.pj(newEpl);

        // only make a chance if we have a new EPL object
        if ( newEpl ) {
            var newEplId = newEpl.id;
            var oldEplId = "null";
            if (oldEpl) oldId = oldEpl.id

            dbg.p("detected selected EPL change from " +
                   oldEplId + " to " + newEplId, 1);
            $scope.currentEpl = newEpl;

            // get the EVC info
            mefServices.getEvc($scope.currentEpl.evcId)
                   .then(onEvcGetResp, onRequestError);                   
        }
        else {
            $scope.currentEplEvc = null;
            $scope.currentEvcUnis = null;
        }

    }
    $scope.$watch( eplWatcher, onEplChange );
};

// register controller in the module
app.controller("MefController", MefController);

}());