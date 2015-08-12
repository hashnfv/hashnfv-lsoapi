(function(){
  
   var cosServices = function ($http, dbg) {

       var _cosBasePath = "/cosmgr/webapi/cos"
       var _cosUrl      = "unset";

       var setCosUrl = function (cosMgrCfg) {
         dbg.p("in setCosUrl");
         _cosUrl = "http://"+cosMgrCfg.ip+":"+cosMgrCfg.port+_cosBasePath;
         dbg.p(_cosUrl);
       }

       var getCosUrl = function () {
         dbg.p("in getCosUrl");
         return _cosUrl;
       }

      var getCosList = function() {
         dbg.p("in cosServices.getCosList()",2);
         var url = _cosUrl+ "/list;"
         dbg.p("GET: " + url , 2);
         return $http.get(url)
                     .then(function(response){ return response.data; });
      };
      var createCos = function(cos) {
         dbg.p("in cosServices.createCos()",2)
         var url = _cosUrl;
         dbg.p("POST: " + url, 2);
         dbg.pj(cos);
         return $http.post(url, cos)
                     .then(function(response){ return response.data; });
      };
      var updateCos = function(cos) {
         dbg.p("in cosServices.updateCos()",2)
         var url = _cosUrl + "/" + cos.id;
         dbg.p("PUT: " + url, 2);
         dbg.pj(cos);
         return $http.put(url, cos)
                     .then(function(response){ return response.data; });
      };
      var deleteCos = function(cos) {
         dbg.p("in cosServices.deleteCos()",2)
         var url = _cosUrl + "/" + cos.id;
         dbg.p("DELELE: " + url, 2);
         dbg.pj(cos);
         return $http.delete(url)
                     .then(function(response){ return response.data; });
      };

      return { // public API
               setCosUrl  : setCosUrl,
               getCosUrl  : getCosUrl,
               createCos  : createCos,
               getCosList : getCosList,
               deleteCos  : deleteCos,
               updateCos  : updateCos
      };
   };
  
   var module = angular.module("vcpe");
   module.factory("cosServices", cosServices);

}());