'use strict';
define(['frontend'], function(frontend) {

	frontend.factory('restService', ['$http', 'url', function($http, url) {

		function httpGet(path) {
			return $http.get(url + path)
				.then(function(response) { 
					return response.data; 
				});
		}

		return {
			getAllEvents: function() {
				return httpGet('/events');
			},
			getClubs: function() {
				return httpGet('/clubs');
			},
			getPitches: function() {
				return httpGet('/pitches');
			}
		}

	}]);

});
