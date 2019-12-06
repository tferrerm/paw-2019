'use strict';
define(['frontend', 'jquery', 'services/storageService'], function(frontend) {

	frontend.factory('restService', ['$http','$rootScope', 'url', 'storageService', function($http, $rootScope, url, storageService) {

		function httpGet(path, params) {
			var headers = {};
			headers = addAuthHeader(headers);

			params = Object.keys(params).length ? '?' + jQuery.param(params) : '';
			return $http.get(url + path + params, headers)
				.then(function(response) { 
					return response.data; 
				});
		}

		function httpPost(path, data, params) {
			var headers = {'Content-Type': undefined};
			headers = addAuthHeader(headers);

			return $http.post(url + path, data, {transformRequest: angular.identity, headers: headers})
				.then(function(response) { 
					return response.data; 
				});
		}

		function addAuthHeader(headers) {
			var authToken = storageService.getAuthToken();
			if(authToken) {
				headers['X-Auth-Token'] = authToken;
			}
			return headers;
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
				return httpPost('/events/' + id + '/join', {}, {});
			},
			leaveEvent: function(id) {
				return httpPost('/events/' + id + '/leave', {}, {});
			},
			register: function(data) {
				var userData = {username: data.username, password: data.password, firstname: data.firstName, lastname: data.lastName/*, picture: data.picture*/};
				var formData = new FormData();
				formData.append('username', userData.username);
				formData.append('password', userData.password);
				formData.append('firstname', userData.firstname);
				formData.append('lastname', userData.lastname);
				//formData.append('picture', userData.picture);
				return httpPost('/users', formData, {});
			}
		}

	}]);

});
