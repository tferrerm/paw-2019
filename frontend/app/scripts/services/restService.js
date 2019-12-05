'use strict';
define(['frontend', 'jquery'], function(frontend) {

	frontend.factory('restService', ['$http','$rootScope', 'url', function($http, $rootScope, url) {

		function httpGet(path, params) {
			params = Object.keys(params).length ? '?' + jQuery.param(params) : '';
			return $http.get(url + path + params)
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
			getAllEvents: function(params) {
				return httpGet('/events', {pageNum: params.pageNum, name: params.name, club: params.club, sport: params.sport, vacancies: params.vacancies, date: params.date});
			},
			getClub: function(id) {
				return httpGet('/clubs/' + id, {});
			},
			getClubs: function(params) {
				return httpGet('/clubs', {pageNum: params.pageNum, name: params.name, location: params.location});
			},
			getPitch: function(id) {
				return httpGet('/pitches/' + id, {});
			},
			getPitches: function(params) {
				return httpGet('/pitches', {pageNum: params.pageNum, name: params.name, sport: params.sport, location: params.location, club: params.club});
			},
			getEvent: function(id) {
				return httpGet('/events/' + id, {});
			},
			getEventInscriptions: function(id) {
				return httpGet('/events/' + id + '/inscriptions', {});
			},
			getHistory: function(id, params) {
				return httpGet('/users/' + id + '/history', {pageNum: params.pageNum});
			},
			getMyFutureEvents: function(id, params) {
				return httpGet('/users/' + id + '/future-owned-events', {pageNum: params.pageNum});
			},
			getMyPastEvents: function(id, params) {
				return httpGet('/users/' + id + '/past-owned-events', {pageNum: params.pageNum});
			},
			getUpcomingEvents: function(id) {
				return httpGet('/users/' + id + '/future-inscriptions', {});
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
