(function() {

var CosController = function($scope, $http, $interval, $log, cosServices, model, dbg ) {

	//
	// Controller Set Up
	//

	dbg.p("---> in CosController()");

	actionState = {
		IDLE : 1, ADD : 2, UPDATE : 3, DEL : 4
	}

	// $scope variables
	$scope.cosList = null;
	$scope.selectedCosIdx = -1;
	$scope.cosToEdit =  null;
	$scope.cosActionButtonText =  "unset";

	$scope.availableBWs = [
		{
			name : "10M",
			cir  : 10000
		},
		{
			name : "100M",
			cir  : 100000
		},
		{
			name : "1G",
			cir  : 1000000
		},
		{
			name : "10G",
			cir  : 10000000
		},
	];

	dbg.p("bandwidths avaialble:")
	dbg.pj($scope.availableBWs);

	// congroller variables
	var cosActionState = actionState.IDLE;
	var newCos =  null;
	var uniqueId = true;
	var nameNum  = 1;


	// cos constructor
	var Cos = function(){
		if ( uniqueId )
			this.id = "new"+ nameNum;
		else
			this.id = "new";
    	this.commitedInfoRate = 50;
    	this.availbility = 95.5;
    	this.frameDelay = 2.2;
    	this.jitter = 3.3;
    	this.frameLoss = 4.4 ;
	}

	//
	// utility fxns
	//

	var validCosIdx = function(idx) {
		return ( idx >= 0 && idx < $scope.cosList.length )
	}

	var cosIdxReset = function() {
		return ( $scope.cosList.length > 0 ? 0 : -1);
	}

	var cosIdxAfter = function( action ) {
		switch ( action ) {
		    case actionState.DEL:
			    if ( validCosIdx($scope.selectedCosIdx) )
			    	return $scope.selectedCosIdx;
			    else if ( validCosIdx($scope.selectedCosIdx-1) )
			    	return $scope.selectedCosIdx-1;
				else
					return cosIdxReset();
		        break;
		    case actionState.ADD:
		    	return ( $scope.cosList.length >= 0 ?
		    		     $scope.cosList.length-1 :  cosIdxReset() );
		        break;
		    case actionState.UPDATE:
		    	return ( validCosIdx($scope.selectedCosIdx) ?
		    		     $scope.selectedCosIdx : cosIdxReset() );
		        break;
		    default:
		        dbg.e("invalid action state");
				break;
		}
	}

	$scope.cosExists = function(cosId) {
		var nameCheck = function (cosElm) { return cosElm.id == cosId; }
		return ( $scope.cosList &&
			     $scope.cosList.filter(nameCheck).length > 0 );
	}

	$scope.bwText = function(cos) {
		if (cos) {
			switch ( cos.commitedInfoRate ) {
				case 10000    : return "10M";
				case 100000   : return "100M";
				case 1000000  : return "1G";
				case 10000000 : return "10G";
			}
			return "invalid BW";
		}
		else {
			return "null cos";
		}
	}

	// return index of avilable BW based cir (-1 if not found)
	var availableBwIdx = function (cir) {
		for ( var i = 0; i < $scope.availableBWs.length; i++ )  {
			if ($scope.availableBWs[i].cir == cir)  {
				return i;
			}
		}
		return -1;
	}

	// return index of cos in current list (-1 if not found)
	var cosNameToIdx = function ( cosName ) {
		if ( !cosName || !$scope.cosList )
			return -1;

		for ( var i = 0; i < $scope.cosList.length; i++ )  {
			if ($scope.cosList[i].id == cosName )  {
				return i;
			}
		}
		return -1;
	}

	//
	// HTTP Repsponse Handlers
	//

	var onRequestError = function(reason){
		dbg.e("HTTP Request Problem\n" + JSON.stringify(reason));
	};
	var onCosListResp = function(data) {
		dbg.p("in onCosListResp()");
		$scope.cosList = data;

		if ( $scope.cosList && $scope.cosList.length > 0   )
			$scope.selectedCosIdx = 0;
		else
			$scope.selectedCosIdx = -1;

		dbg.p("selectedCosIdx = " + $scope.selectedCosIdx,1);
	};
	var onCosCreateResp = function(data) {
		dbg.p("in onCosCreateResp()");
		dbg.p("following cos returned from create cmd", 1);
		dbg.pj(data);
		$scope.cosList.push(newCos);
		$scope.selectedCosIdx = cosIdxAfter(actionState.ADD);
		nameNum++;
		newCos =  null;
		$scope.cosToEdit =  null;
		dbg.p("selectedCosIdx = " + $scope.selectedCosIdx,1);
	};
	var onCosUpdateResp = function(data) {
		dbg.p("in onCosUpdateResp()");
		dbg.p("following cos returned after update", 1);
		dbg.pj(data);
		$scope.selectedCosIdx = cosIdxAfter(actionState.UPDATE);
		$scope.cosToEdit =  null;
		dbg.p("selectedCosIdx = " + $scope.selectedCosIdx,1);
	};
	var onCosDeleteResp = function(data) {
		dbg.p("in onCosDeleteResp()");
		dbg.p("following returned after delete", 1);
		dbg.pj(data);
		if ( validCosIdx($scope.selectedCosIdx) )
			$scope.cosList.splice($scope.selectedCosIdx,1);
		$scope.selectedCosIdx = cosIdxAfter(actionState.DEL);
		dbg.p("selectedCosIdx = " + $scope.selectedCosIdx,1);
	};

	//
 	// CoS Event Handlers & Utils
	//

	$scope.onCosClick = function (i) {
		dbg.p("in onCosClick(): clicked ("+i+")");
		$scope.selectedCosIdx = i;
		cosActionState = actionState.IDLE;
		dbg.p("selectedCosIdx = "+ $scope.selectedCosIdx,1);
	}
	$scope.onAddCos = function () {
		dbg.p("in onAddCos()");
		cosActionState = actionState.ADD;
		$scope.cosActionButtonText =  "Create";
		newCos =  new Cos();
		$scope.cosToEdit = newCos;

		if ($scope.availableBWs.length >=3 )
			$scope.cosToEdit.bwSelected = $scope.availableBWs[2];
		else
			$scope.cosToEdit.bwSelected = $scope.availableBWs[0];

		dbg.pj(newCos);
	}
	$scope.onUpdateCos = function () {
		dbg.p("in onUpdateCos()");
		if ( $scope.cosList.length <= 0 )
		{
			dbg.p("nos CoS's exist, no action being taken");
			return;
		}
		cosActionState = actionState.UPDATE;
		$scope.cosActionButtonText =  "Update";
		$scope.cosToEdit = $scope.cosList[$scope.selectedCosIdx];

		dbg.p("looking for dd list bw: " + $scope.cosToEdit.commitedInfoRate);
		var availBwIdx = availableBwIdx($scope.cosToEdit.commitedInfoRate);
		if ( availBwIdx < 0 || availBwIdx > $scope.availableBWs.length-1 ) {
			dbg.e("invalid selected BW idx: "+availBwIdx+"(setting to 0)");
			$scope.cosToEdit.bwSelected = $scope.availableBWs[0];
		}
		else {
			$scope.cosToEdit.bwSelected = $scope.availableBWs[availBwIdx];
		}

		dbg.p("about to edit cos:");
		dbg.pj($scope.cosToEdit);


	}
	$scope.onDelCos = function () {
		dbg.p("in onDelCos()");
		if ( $scope.cosList.length <= 0 )
		{
			dbg.p("nos CoS's exist, no action being taken");
			return;
		}
		cosActionState = actionState.DEL;
		$scope.cosActionButtonText =  "Delete";
	}

	$scope.onCosInputSubmit = function () {
		dbg.p("in onCosInputSubmit()");

		var cosToOperateOn = null;
		if ( cosActionState === actionState.ADD)
			cosToOperateOn = newCos;
		else
			cosToOperateOn = $scope.cosList[$scope.selectedCosIdx];

		switch(cosActionState) {

		    case actionState.DEL:
		        dbg.p("about to delete " + cosToOperateOn.id,1);
				cosServices.deleteCos(cosToOperateOn)
            				.then(onCosDeleteResp, onRequestError);
		        break;

		    case actionState.ADD:
		    	cosToOperateOn.commitedInfoRate = cosToOperateOn.bwSelected.cir;
				delete cosToOperateOn.bwSelected;
		        dbg.p("about to add " + cosToOperateOn.id, 1);
				cosServices.createCos(cosToOperateOn)
            				.then(onCosCreateResp, onRequestError);
		        break;

		    case actionState.UPDATE:
		    	cosToOperateOn.commitedInfoRate = cosToOperateOn.bwSelected.cir;
				delete cosToOperateOn.bwSelected;
		        dbg.p("about to update " + cosToOperateOn.id, 1);
				cosServices.updateCos(cosToOperateOn)
            				.then(onCosUpdateResp, onRequestError);
		        break;

		    default:
		        dbg.e("invalid action state");
		        break;
		}

		cosActionState = actionState.IDLE;
	}

	//
	// State query fxns
	//

	$scope.cosActionInProgress = function () {
		return ( cosActionState != actionState.IDLE  )
	}
	$scope.cosEditInProgress = function () {
		return ( cosActionState === actionState.ADD  ||
			     cosActionState === actionState.UPDATE  );
	}
	$scope.cosUpdateInProgress = function () {
		return (  cosActionState === actionState.UPDATE  );
	}
	$scope.cosAddInProgress = function () {
		return (  cosActionState === actionState.ADD  );
	}
	$scope.cosDelInProgress = function () {
		return (  cosActionState === actionState.DEL  );
	}
	$scope.cosNameConflict = function () {
		return ( $scope.cosAddInProgress() &&
				 $scope.cosExists($scope.cosToEdit.id));
	}
	$scope.showCosInputs = function () {
		return $scope.cosEditInProgress();
	}
	$scope.showCosValues = function () {
		return (!$scope.showCosInputs() &&
		         validCosIdx($scope.selectedCosIdx));
	}
	$scope.showCosNameInput = function () {
		return $scope.cosAddInProgress();
	}
	$scope.showCosName = function () {
		return (! $scope.showCosNameInput());
	}
	$scope.showCosActionButton = function () {
		return ( $scope.cosAddInProgress()   ||
			   ($scope.cosActionInProgress() &&
				  $scope.cosList.length > 0  ));
	}
	$scope.activateCosActionButton = function () {
		return ( $scope.cosActionInProgress() &&
				!$scope.cosNameConflict()     )
	}

	//
	// Watchers
	//

    // we can't get our initial CoS list until 
    // the URL has been set by config file read in
    cosUrlWatcher = function () { return cosServices.getCosUrl(); }
    onCosUrlChange = function (newCosUrl, oldCosUrl) {
		if (newCosUrl !== oldCosUrl) {
			dbg.p("detected cos url change, getting cos list");
			cosServices.getCosList()
					   .then(onCosListResp, onRequestError);
		}
	}
    $scope.$watch( cosUrlWatcher, onCosUrlChange );

    // Watch for change in selected EPL so tht 
    // we can set the appropraite CoS as selected
    eplWatcher = function () { return model.getCurrentEpl(); }
    onEplChange = function (newEpl, oldEpl) {
        dbg.p("in CosController:onEplChange");
        //dbg.p("oldEpl:"); dbg.pj(oldEpl);
        //dbg.p("newEpl:"); dbg.pj(newEpl);

        // only make a chance if we have an
        if ( newEpl ) {
        	var cosIdx = cosNameToIdx(newEpl.cos);
            dbg.p("changing selected CoS to: " + newEpl.cos + 
            	  " [idx="+cosIdx+"]", 1);
            if ( validCosIdx(cosIdx) ) {
				cosActionState = actionState.IDLE
				$scope.selectedCosIdx = cosIdx;
            }
        }
    }
    $scope.$watch( eplWatcher, onEplChange );

};

// register controller in the module
app.controller("CosController", CosController);

}());