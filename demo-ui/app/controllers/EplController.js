(function() {

var EplController = function($scope, $http, $interval, $log, 
                             eplServices, cosServices, model, dbg ) {

    //
    // Controller Set Up
    //
    //

    dbg.p("---> in EplController()");

    actionState = {
        IDLE : 1, ADD : 2, UPDATE : 3, DEL : 4
    };

    // $scope variables
    $scope.eplList = [];
    $scope.selectedEplIdx = -1;
    $scope.eplToEdit =  null;
    $scope.eplActionButtonText =  "unset";
    $scope.availableUnis = [];
    $scope.availableCosIds = [];

    // for trouble shooting
    $scope.availableCosIds.push("unset cos 0");
    $scope.availableCosIds.push("unset cos 1");
    $scope.availableCosIds.push("unset cos 2");

    // controller variables
    var _uniqueId = true;
    var _nameNum  = 1;
    var _eplActionState = actionState.IDLE;
    var _newEpl =  null;

    // start out with null until we do list query)
    model.setCurrentEpl(null);

    //
    // utility fxns
    //

    var validEplIdx = function(idx) {
        return ( idx >= 0 && idx < $scope.eplList.length )
    }
    var eplIdxReset = function() {
        return ( $scope.eplList.length > 0 ? 0 : -1);
    }
    var cosIdxReset = function() {
        return ( $scope.eplList.length > 0 ? 0 : -1);
    }
    var eplIdxAfter = function( action ) {
        switch ( action ) {
            case actionState.DEL:
                if ( validEplIdx($scope.selectedEplIdx) )
                    return $scope.selectedEplIdx;
                else if ( validEplIdx($scope.selectedEplIdx-1) )
                    return $scope.selectedEplIdx-1;
                else
                    return eplIdxReset();
                break;
            case actionState.ADD:
                return ( $scope.eplList.length >= 0 ?
                         $scope.eplList.length-1 :  eplIdxReset() );
                break;
            case actionState.UPDATE:
                return ( validEplIdx($scope.selectedEplIdx) ?
                         $scope.selectedEplIdx : eplIdxReset() );
                break;
            default:
                dbg.e("invalid action state");
                return -1;
                break;
        }
    }
    $scope.eplExists = function(eplId) {
        var nameCheck = function (eplElm) { return eplElm.id == eplId; }
        return ( $scope.eplList &&
                 $scope.eplList.filter(nameCheck).length > 0 );
    }
    var setEplUniInfo = function(epl, uniIdx1, uniIdx2) {

        if ( $scope.availableUnis.length < 2 ) {
            dbg.e("Can't set unis in constructor, avaulable uni list too short")
            return;
        }

        epl.numCustLocations = 2;

        if ( uniIdx1 < 0 || uniIdx1 > 1) {
            epl.uniHostIpList.push($scope.availableUnis[0].ip);
            epl.uniHostMacList.push($scope.availableUnis[0].mac);
            epl.custAddressList.push($scope.availableUnis[0].address);
        }
        else {
            epl.uniHostIpList.push($scope.availableUnis[uniIdx1].ip);
            epl.uniHostMacList.push($scope.availableUnis[uniIdx1].mac);
            epl.custAddressList.push($scope.availableUnis[uniIdx1].address);
        }

        if ( uniIdx2 < 0 || uniIdx2 > 1) {
            epl.uniHostIpList.push($scope.availableUnis[0].ip);
            epl.uniHostMacList.push($scope.availableUnis[0].mac);
            epl.custAddressList.push($scope.availableUnis[0].address);
        }
        else {
            epl.uniHostIpList.push($scope.availableUnis[uniIdx2].ip);
            epl.uniHostMacList.push($scope.availableUnis[uniIdx2].mac);
            epl.custAddressList.push($scope.availableUnis[uniIdx2].address);
        }
    }

    // return index of avilable Uni based on IP (-1 if not found)
    var availableUniIdx = function (uniIp) {
        for ( var i = 0; i < $scope.availableUnis.length; i++ )  {
            if ($scope.availableUnis[i].ip == uniIp)  {
                return i;
            }
        }
        return -1;
    }

    var updateEplWithSelectedUnis = function(epl) {

        if ( epl == null || $scope.eplToEdit == null ) {
            dbg.e("can't update EPL with UNIS, missing object");
            return
        }

        epl.uniHostIpList = [];
        epl.uniHostIpList.push($scope.eplToEdit.uni1Selected.ip);
        epl.uniHostIpList.push($scope.eplToEdit.uni2Selected.ip);

        epl.uniHostMacList = [];
        epl.uniHostMacList.push($scope.eplToEdit.uni1Selected.mac);
        epl.uniHostMacList.push($scope.eplToEdit.uni2Selected.mac);

        epl.custAddressList = [];
        epl.custAddressList.push($scope.eplToEdit.uni1Selected.address);
        epl.custAddressList.push($scope.eplToEdit.uni2Selected.address);
    }

    //
    // epl constructor
    //

    var Epl = function(){
        dbg.pj("constructing EPL");
        if ( _uniqueId )
            this.id = "new"+ _nameNum;
        else
            this.id = "new";

        // Don't set COS here, we have to wait until we get the latest cos list
        this.cos = "unset";

        // EVC ID set by lower layers
        // TODO : how does evc ID cat capture at EPL layer??
        this.evcId = "unset";

        this.uniHostIpList = [];
        this.uniHostMacList = [];
        this.custAddressList = [];

        if ( $scope.availableUnis.length >= 2 )
            setEplUniInfo(this, 0, 1);
        else {
            dbg.e("can't construct EPL, not enough uni's in the available uni list");
            return;
        }

        dbg.pj(this);
    }

    var copyReturnedEplParams = function(srcEpl, destEpl ) {
        destEpl.evcId = srcEpl.evcId;
    }

    var setModalActiveEpl =function () {
        if ( $scope.selectedEplIdx >= 0 && 
             $scope.selectedEplIdx <  $scope.eplList.length )
        {
            model.setCurrentEpl($scope.eplList[$scope.selectedEplIdx]);
        }
        else
        {
            model.setCurrentEpl(null);            
        } 
    }

    //
    // EPL HTTP Response Handlers
    //

    var onRequestError = function(reason){
        dbg.e("HTTP Request Problem\n" + JSON.stringify(reason));
    };

    var onEplListResp = function(data) {
        dbg.p("in onEplListResp()");
        $scope.eplList = data;

        if ( $scope.eplList && $scope.eplList.length > 0   )  {
            $scope.selectedEplIdx = 0;
            model.setCurrentEpl($scope.eplList[$scope.selectedEplIdx]);
            dbg.p("selectedEplIdx/Id = "+ $scope.selectedEplIdx + "/" +
                                          $scope.eplList[$scope.selectedEplIdx].id, 1);
        }
        else {
            $scope.selectedEplIdx = -1;
            model.setCurrentEpl(null);
            dbg.p("selectedEplIdx = "+ $scope.selectedEplIdx, 1);
        }
        _eplActionState = actionState.IDLE;
    };
    var onEplCreateResp = function(returnedEpl) {
        dbg.p("in onEplCreateResp()");
        dbg.p("following epl returned from create cmd", 1);
        dbg.pj(returnedEpl);

        // grab entire returned Epl, since we don't know what
        // the svcmgr may have changed on creation
        $scope.eplList.push(returnedEpl);

        // a safer alternative?
        //copyReturnedEplParams(returnedEpl, _newEpl );
        //$scope.eplList.push(_newEpl);

        $scope.selectedEplIdx = eplIdxAfter(actionState.ADD);
        setModalActiveEpl();

        _nameNum++;
        _newEpl =  null;
        $scope.eplToEdit =  null;
        dbg.p("selectedEplIdx = " + $scope.selectedEplIdx,1);
    };
    var onEplUpdateResp = function(returnedEpl) {
        dbg.p("in onEplUpdateResp()");
        dbg.p("following epl returned after update", 1);
        dbg.pj(returnedEpl);

        // splice in entire returned Epl, since we don't know what
        // the svcmgr may have changed on update (esp evc)
        $scope.eplList[$scope.selectedEplIdx] = returnedEpl;

        // a safer alternative?
        // copyReturnedEplParams(returnedEpl, eplToEdit );



        $scope.selectedEplIdx = eplIdxAfter(actionState.UPDATE);

        // To force a refresh
        model.setCurrentEpl(null);
        setModalActiveEpl();

        $scope.eplToEdit =  null;
        dbg.p("selectedEplIdx = " + $scope.selectedEplIdx,1);
    };
    var onEplDeleteResp = function(data) {
        dbg.p("in onEplDeleteResp()");
        dbg.p("following returned after delete", 1);
        dbg.pj(data);
        if ( validEplIdx($scope.selectedEplIdx) )
            $scope.eplList.splice($scope.selectedEplIdx,1);
        $scope.selectedEplIdx = eplIdxAfter(actionState.DEL);
        setModalActiveEpl();
        dbg.p("selectedEplIdx = " + $scope.selectedEplIdx,1);
    };

    //
    // Epl Event Handlers
    //

    $scope.onEplClick = function (i) {
        dbg.p("in onEplClick(): clicked ("+i+")");
        $scope.selectedEplIdx = i;
        _eplActionState = actionState.IDLE;
        eplToOperateOn = $scope.eplList[$scope.selectedEplIdx]
        model.setCurrentEpl(eplToOperateOn);
        dbg.p("selectedEplIdx/Id = "+ $scope.selectedEplIdx + "/" +
                                       eplToOperateOn.id, 1);
    }
    $scope.onAddEpl = function () {
        dbg.p("in onAddEpl()");
        _eplActionState = actionState.ADD;
        $scope.eplActionButtonText =  "Create";

        var updateEplToAddOnCosListResponse = function(data) {
            dbg.p("in updateNewEplCosOnCosListResponse()", 1);
            var cosList = data;

            $scope.availableCosIds = [];
            for ( var i = 0; i < cosList.length; i++ )
                $scope.availableCosIds.push(cosList[i].id);

            dbg.p("Avilable CoS List");
            dbg.pj($scope.availableCosIds);

            // defaults for drop down lists
            if ( $scope.availableCosIds.length > 0 )
                 $scope.eplToEdit.cos = $scope.availableCosIds[0];

            $scope.eplToEdit.uni1Selected = $scope.availableUnis[0];
            $scope.eplToEdit.uni2Selected = $scope.availableUnis[1];

            dbg.p("Newly created EPL to add after CoS list retrieved");
            dbg.pj($scope.eplToEdit);
        };

        _newEpl =  new Epl();
        $scope.eplToEdit = _newEpl;

        // need to get the current cos list before adding new EPL
        cosServices.getCosList()
                   .then(updateEplToAddOnCosListResponse,
                         onRequestError);
    }
    $scope.onUpdateEpl = function () {
        dbg.p("in onUpdateEpl()");
        if ( $scope.eplList.length <= 0 )
        {
            dbg.p("nos Epl's exist, no action being taken");
            return;
        }
        _eplActionState = actionState.UPDATE;
        $scope.eplActionButtonText =  "Update";

        var updateEplToEditOnCosListResponse = function(data) {
            dbg.p("in onCosListResp()");
            var cosList = data;
            $scope.availableCosIds = [];
            for ( var i = 0; i < cosList.length; i++ )
                $scope.availableCosIds.push(cosList[i].id);
            dbg.p("Avilable CoS List");
            dbg.pj($scope.availableCosIds);

            $scope.eplToEdit = $scope.eplList[$scope.selectedEplIdx];

            // TODO (if time): put below stuff in fxn since duplicated twice
            dbg.p("looking for dd list ip: " + $scope.eplToEdit.uniHostIpList[0] );
            var availUniIdx = availableUniIdx($scope.eplToEdit.uniHostIpList[0]);
            if ( availUniIdx < 0 || availUniIdx > $scope.availableUnis.length-1 ) {
                dbg.e("invalid selected uni-1 idx: "+availUniIdx+"(setting to 0)");
                $scope.eplToEdit.uni1Selected = $scope.availableUnis[0];
            }
            else {
                $scope.eplToEdit.uni1Selected = $scope.availableUnis[availUniIdx];
            }

            dbg.p("looking for dd list ip: " + $scope.eplToEdit.uniHostIpList[1] );
            availUniIdx = availableUniIdx($scope.eplToEdit.uniHostIpList[1]);
            if ( availUniIdx < 0 || availUniIdx > $scope.availableUnis.length-1 ) {
                dbg.e("invalid selected uni-2 idx: "+availUniIdx+"(setting to 0)");
                $scope.eplToEdit.uni2Selected = $scope.availableUnis[0]
            }
            else {
                $scope.eplToEdit.uni2Selected = $scope.availableUnis[availUniIdx];
            }

            dbg.p("about to edit epl:");
            dbg.pj($scope.eplToEdit);
        };

        // need to get the current cos list before editing
        cosServices.getCosList()
                   .then(updateEplToEditOnCosListResponse,
                         onRequestError);
    }

    $scope.onDelEpl = function () {
        dbg.p("in onDelEpl()");
        if ( $scope.eplList.length <= 0 )
        {
            dbg.p("nos Epl's exist, no action being taken");
            return;
        }
        _eplActionState = actionState.DEL;
        $scope.eplActionButtonText =  "Delete";
    }

    $scope.onEplInputSubmit = function () {
        dbg.p("in onEplInputSubmit()");

        var eplToOperateOn = null;
        if ( _eplActionState === actionState.ADD)
            eplToOperateOn = _newEpl;
        else
            eplToOperateOn = $scope.eplList[$scope.selectedEplIdx];

        switch(_eplActionState) {

            case actionState.DEL:

                dbg.p("about to delete " + eplToOperateOn.id,1);
                eplServices.deleteEpl(eplToOperateOn)
                            .then(onEplDeleteResp, onRequestError);
                break;

            case actionState.ADD:

                updateEplWithSelectedUnis(eplToOperateOn);

                delete $scope.eplToEdit.uni1Selected;
                delete $scope.eplToEdit.uni2Selected;

                dbg.p("about to add " + eplToOperateOn.id, 1);
                dbg.pj(eplToOperateOn);
                eplServices.createEpl(eplToOperateOn)
                           .then(onEplCreateResp, onRequestError);
                break;

            case actionState.UPDATE:

                updateEplWithSelectedUnis(eplToOperateOn);

                delete $scope.eplToEdit.uni1Selected;
                delete $scope.eplToEdit.uni2Selected;

                dbg.p("about to update " + eplToOperateOn.id, 1);
                eplServices.updateEpl(eplToOperateOn)
                            .then(onEplUpdateResp, onRequestError);
                break;

            default:
                dbg.e("invalid action state");
                break;
        }

        _eplActionState = actionState.IDLE;
    }

    //
    // State query fxns
    //

    $scope.eplActionInProgress = function () {
        return ( _eplActionState != actionState.IDLE  )
    }
    $scope.eplEditInProgress = function () {
        return ( _eplActionState === actionState.ADD  ||
                 _eplActionState === actionState.UPDATE  );
    }
    $scope.eplUpdateInProgress = function () {
        return (  _eplActionState === actionState.UPDATE  );
    }
    $scope.eplAddInProgress = function () {
        return (  _eplActionState === actionState.ADD  );
    }
    $scope.eplDelInProgress = function () {
        return (  _eplActionState === actionState.DEL  );
    }
    $scope.cosExists = function () {
        return ( $scope.availableCosIds	&&
                 $scope.availableCosIds.length > 0 );
    }
    $scope.cosConflict = function () {
        return ( $scope.eplAddInProgress() &&
                 !$scope.cosExists()        );
    }
    $scope.eplNameConflict = function () {
        return ( $scope.eplAddInProgress() &&
                 $scope.eplExists($scope.eplToEdit.id));
    }
    $scope.uniConflict = function () {
        return ( $scope.eplToEdit &&
                 $scope.eplToEdit.uni1Selected &&
                 $scope.eplToEdit.uni2Selected &&
                 $scope.eplEditInProgress() &&
                     ( $scope.eplToEdit.uni1Selected.ip ==
                       $scope.eplToEdit.uni2Selected.ip  ));
    }
    $scope.showEplInputs = function () {
        return $scope.eplEditInProgress();
    }
    $scope.showEplValues = function () {
        return (!$scope.showEplInputs() &&
                 validEplIdx($scope.selectedEplIdx));
    }
    $scope.showEplNameInput = function () {
        return $scope.eplAddInProgress();
    }
    $scope.showEplName = function () {
        return (! $scope.showEplNameInput());
    }
    $scope.showEplActionButton = function () {
        return ( $scope.eplAddInProgress()   ||
               ($scope.eplActionInProgress() &&
                  $scope.eplList.length > 0  ));
    }
    $scope.activateEplActionButton = function () {
        return ( $scope.eplActionInProgress() &&
                 !$scope.eplNameConflict()    &&
                 !$scope.uniConflict()        &&
                 !$scope.cosConflict()         )
    }

    //
    // Initial Page Set Up
    //

    eplUrlWatcher = function () { return eplServices.getEplUrl(); }
    onEplUrlChange = function (newEplUrl, oldEplUrl) {
        if (newEplUrl !== oldEplUrl) {
            dbg.p("detected epl url change, getting epl list");
            eplServices.getEplList()
                       .then(onEplListResp, onRequestError);
        }
    }
    $scope.$watch( eplUrlWatcher, onEplUrlChange );


    uniListWatcher = function () { return model.getAvailableUnis(); }
    onUniListChange = function (newUniList, oldUniList) {
        if (newUniList !== oldUniList) {
            dbg.p("detected uni list change")
            dbg.p("new avilable uni list:")
            $scope.availableUnis = newUniList;
            dbg.pj($scope.availableUnis);
        }
    }
    $scope.$watch( uniListWatcher, onUniListChange );
};

// register controller in the module
app.controller("EplController", EplController);

}());