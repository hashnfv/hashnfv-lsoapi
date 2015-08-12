//
// For data that must be shared accross controllers
//

(function(){
var model = function ($log, dbg) {

	var _shared = {
		availableUnis : [],
		currentEpl    : null
	};

	var getAvailableUnis = function () {
		return _shared.availableUnis;
	}

	var setAvailableUnis = function (availableUnis) {
		dbg.p("in model:setAvailableUnis");
		_shared.availableUnis = availableUnis;
		dbg.pj(_shared.availableUnis);
	}

	// var getCurrentEplId = function () {
	// 	// dbg.p("in model:getCurrentEplId, returning: "+_shared.currentEplId);
	// 	return _shared.currentEplId;
	// }

	// var setCurrentEplId = function (currentEplId) {
	// 	// dbg.p("in model:setCurrentEplId, setting to: "+currentEplId);
	// 	_shared.currentEplId = currentEplId;
	// }

	var getCurrentEpl = function () {
		dbg.p("in model:getCurrentEpl:");
		//dbg.pj(_shared.currentEpl);
		return _shared.currentEpl;
	}

	var setCurrentEpl = function (currentEpl) {
		// dbg.p("in model:setCurrentEplId, setting to: "+currentEplId);
		dbg.p("in model:setCurrentEpl");
		//dbg.pj(currentEpl);
		_shared.currentEpl = currentEpl;
	}


	var dumpShareDdata = function() { dbg.pj(_shared); };

	return { // Public API
			 getAvailableUnis : getAvailableUnis,
			 setAvailableUnis : setAvailableUnis,
			 getCurrentEpl    : getCurrentEpl,
			 setCurrentEpl    : setCurrentEpl,
			 dumpShareDdata   : dumpShareDdata
      };
   };

   var module = angular.module("vcpe");
   module.factory("model", model);

}());