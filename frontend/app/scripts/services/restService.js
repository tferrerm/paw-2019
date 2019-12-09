'use strict';
define(['frontend', 'jquery', 'services/storageService'], function(frontend) {

	frontend.factory('restService', ['$http','$rootScope', 'url', 'storageService', function($http, $rootScope, url, storageService) {

		function httpGet(path, params) {
			var headers = {};
			headers = addAuthHeader(headers);
			
			params = Object.keys(params).length ? '?' + jQuery.param(params) : '';
			return $http.get(url + path + params, {headers: headers})
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
			commentClub: function(id, comment) {
				var formData = new FormData();
				formData.append('comment', comment);
				return httpPost('/clubs/' + id + '/comment', formData, {});
			},
			commentUser: function(id, comment) {
				var formData = new FormData();
				formData.append('comment', comment);
				return httpPost('/users/' + id + '/comment', formData, {});
			},
			createEvent: function(pitchid, data) {
				var eventData = {name: data.name, description: data.description, maxParticipants: data.maxParticipants, date: data.date, startsAtHour: data.startsAtHour, endsAtHour: data.endsAtHour, inscriptionEndDate: data.inscriptionEndDate};
				var formData = new FormData();
				formData.append('name', eventData.name);
				formData.append('description', eventData.description);
				formData.append('maxParticipants', eventData.maxParticipants);
				formData.append('date', eventData.date);
				formData.append('startsAtHour', eventData.startsAtHour);
				formData.append('endsAtHour', eventData.endsAtHour);
				formData.append('inscriptionEndDate', eventData.inscriptionEndDate);
				return httpPost('/pitches/' + pitchid + '/events', formData, {});
			},
			deleteEvent: function(id) {

			},
			getAllEvents: function(params) {
				return httpGet('/events', {pageNum: params.pageNum, name: params.name, club: params.club, sport: params.sport, vacancies: params.vacancies, date: params.date});
			},
			getClub: function(id) {
				return httpGet('/clubs/' + id, {});
			},
			getClubComments: function(id, params) {
				return httpGet('/clubs/' + id + '/comments', {pageNum: params.pageNum});
			},
			getClubPitches: function(id, params) {
				return httpGet('/clubs/' + id + '/pitches', {pageNum: params.pageNum});
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
			getEvent: function(pitchid, eventid) {
				return httpGet('/pitches/' + pitchid + '/events/' + eventid, {});
			},
			getEventInscriptions: function(pitchid, eventid) {
				return httpGet('/pitches/' + pitchid + '/events/' + eventid + '/inscriptions', {});
			},
			getHistory: function(id, params) {
				return httpGet('/users/' + id + '/history', {pageNum: params.pageNum});
			},
			getHourRange: function() {
				return httpGet('/pitches/hour-range', {});
			},
			getMyFutureEvents: function(id, params) {
				return httpGet('/users/' + id + '/future-owned-events', {pageNum: params.pageNum});
			},
			getMyPastEvents: function(id, params) {
				return httpGet('/users/' + id + '/past-owned-events', {pageNum: params.pageNum});
			},
			getPitchWeekEvents: function(id, params) {
				return httpGet('/pitches/' + id + '/week-events', {});
			},
			getSports: function() {
				return httpGet('/pitches/sports', {});
			},
			getTournaments: function(params) {
		        return httpGet('/tournaments', {pageNum: params.pageNum, name: params.name, sport: params.sport, location: params.location, club: params.club});
		    },
			getUpcomingEvents: function(id) {
				return httpGet('/users/' + id + '/future-inscriptions', {});
			},
			getUserComments: function(id, params) {
				return httpGet('/users/' + id + '/comments', {pageNum: params.pageNum});
			},
			getUserProfile: function(id) {
				return httpGet('/users/' + id, {});
			},
			hasRelationshipWithClub: function(id) {
				return httpGet('/clubs/' + id + '/has-relationship', {});
			},
			hasRelationshipWithUser: function(id) {
				return httpGet('/users/' + id + '/has-relationship', {});
			},
			joinEvent: function(pitchid, eventid) {
				return httpPost('/pitches/' + pitchid + '/events/' + eventid + '/join', {}, {});
			},
			kickUser: function(pitchid, eventid, userid) {
				return httpPost('/pitches/' + pitchid + '/events/' + eventid + '/kick-user/' + userid, {}, {});
			},
			leaveEvent: function(pitchid, eventid) {
				return httpPost('/pitches/' + pitchid + '/events/' + eventid + '/leave', {}, {});
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
