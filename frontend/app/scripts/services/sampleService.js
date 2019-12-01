'use strict';
define(['frontend'], function(frontend) {

	frontend.factory('sampleService', ['$http', 'url', function($http, url) {

		function httpGet(path) {
			return $http.get(url + path)
				.then(function(response) { 
					return response.data; 
				});
		}

		return {
			sampleFunction: function() {
				return httpGet('/events');
			}
		}

	}]);

});
