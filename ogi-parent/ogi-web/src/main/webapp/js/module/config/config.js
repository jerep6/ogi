/** Module for configuration. Provide ServiceConfiguration */
angular.module('myApp.config', ["sort-by.html", "checklist-model"])//
    .factory('ServiceConfiguration', function() {
        return {
            API_URL: "http://localhost:8080/ogi-ws",
            MAP_CENTER:  new google.maps.LatLng(43.65942731889631, -1.3027381896972656),
            MAP_ZOOM:  12
        }
    });

// Config $http for CORS
angular.module('myApp.config').config(['$httpProvider', function($httpProvider) {
    // Just setting useXDomain to true is not enough. AJAX request are also send with the X-Requested-With header, which
    // indicate them as being AJAX. Removing the header is necessary, so the server is not rejecting the incoming request.
    $httpProvider.defaults.useXDomain = true;

    // X-Requested-With 	mainly used to identify Ajax requests. Most JavaScript frameworks send this header with value of XMLHttpRequest
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);

/** Interceptor for http response KO. Log with ServiceAlert */
angular.module('myApp.config').config(function ($provide, $httpProvider) {
    // Intercept http calls.
    $provide.factory('HttpInterceptorLog', function ($q, ServiceAlert) {
        return {
            // On response failure
            responseError: function (rejection) {
                ServiceAlert.addError(ServiceAlert.formatResponse(rejection));
                // Return the promise rejection.
                return $q.reject(rejection);
            }
        };
    });

    // Add the interceptor to the $httpProvider.
    $httpProvider.interceptors.push('HttpInterceptorLog');
});