'use strict';
define(['frontend'], function(frontend) {

	frontend.factory('restService', ['$http', 'url', function($http, url) {

		function httpGet(path) {
			return $http.get(url + path)
				.then(function(response) { 
					return response.data; 
				});
		}

		function httpPost(path) {
			return $http.post(url + path, {}, {})
				.then(function(response) { 
					return response.data; 
				});
		}

		return {
			getAllEvents: function() {
				return httpGet('/events');
			},
			getClub: function(id) {
				return httpGet('/clubs/' + id);
			},
			getClubs: function() {
				return httpGet('/clubs');
			},
			getPitch: function(id) {
				return httpGet('/pitches/' + id);
			},
			getPitches: function() {
				return httpGet('/pitches');
			},
			getEvent: function(id) {
				return httpGet('/events/' + id);
			},
			getEventInscriptions: function(id) {
				return httpGet('/events/' + id + '/inscriptions');
			},
			joinEvent: function(id) {
				return httpPost('/events/' + id + '/join');
			},
			leaveEvent: function(id) {
				return httpPost('/events/' + id + '/leave');
			}
		}

	}]);

});
