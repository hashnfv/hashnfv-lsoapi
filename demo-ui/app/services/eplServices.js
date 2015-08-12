(function(){
  
   var eplServices = function ($http, dbg) {

       var _eplBasePath = "/svcmgr/webapi/svc/epl"
       var _eplUrl      = "unset";

       var setEplUrl = function (eplMgrCfg) {
         dbg.p("in setEplUrl");
         _eplUrl = "http://"+eplMgrCfg.ip+":"+eplMgrCfg.port+_eplBasePath;
         dbg.p(_eplUrl);
       }

       var getEplUrl = function () {
         dbg.p("in setEplUrl");
          return _eplUrl;
       }

      var getEplList = function() {
         dbg.p("in eplServices.getEplList()",2);
         var url = _eplUrl+ "/list;"
         dbg.p("GET: " + url , 2);
         return $http.get(url)
                     .then(function(response){ return response.data; });
      };
      var createEpl = function(epl) {
         dbg.p("in eplServices.createEpl()",2)
         var url = _eplUrl;
         dbg.p("POST: " + url, 2);
         dbg.pj(epl);
         return $http.post(url, epl)
                     .then(function(response){ return response.data; });
      };
      var updateEpl = function(epl) {
         dbg.p("in eplServices.updateEpl()",2)
         var url = _eplUrl + "/" + epl.id;
         dbg.p("PUT: " + url, 2);
         dbg.pj(epl);
         return $http.put(url, epl)
                     .then(function(response){ return response.data; });
      };
      var deleteEpl = function(epl) {
         dbg.p("in eplServices.deleteEpl()",2)
         var url = _eplUrl + "/" + epl.id;
         dbg.p("DELELE: " + url, 2);
         dbg.pj(epl);
         return $http.delete(url)
                     .then(function(response){ return response.data; });
      };

      return { // public API
               setEplUrl  : setEplUrl,
               getEplUrl  : getEplUrl,
               createEpl  : createEpl,
               getEplList : getEplList,
               deleteEpl  : deleteEpl,
               updateEpl  : updateEpl
      };
   };
  
   var module = angular.module("vcpe");
   module.factory("eplServices", eplServices);

}());