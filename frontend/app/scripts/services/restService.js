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

		function httpDelete(path, params) {
			var headers = {};
			headers = addAuthHeader(headers);

			params = Object.keys(params).length ? '?' + jQuery.param(params) : '';

			return $http.delete(url + path + params, {headers: headers})
				.then(function(response) {
					return response.data;
				})
				/*.catch(function(response) {
					return $q.reject(response);
				});*/
		}

		function addAuthHeader(headers) {
			var authToken = storageService.getAuthToken();
			if(authToken) {
				headers['X-Auth-Token'] = authToken;
			}
			return headers;
		}

		function _arrayBufferToBase64(buffer) {
		    var binary = '';
		    var bytes = new Uint8Array(buffer);
		    var len = bytes.byteLength;
		    for (var i = 0; i < len; i++) {
		      binary += String.fromCharCode(bytes[i]);
		    }
		    return window.btoa(binary);
		}

		return {
			cancelEvent: function(pitchid, eventid) {
				return httpDelete('/pitches/' + pitchid + '/events/' + eventid, {});
			},
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
			createClub: function(data) {
				var clubData = {name: data.name, location: data.location};
				var formData = new FormData();
				formData.append('name', clubData.name);
				formData.append('location', clubData.location);

				return httpPost('/admin/clubs', formData, {});
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
			createPitch: function(clubid, data) {
				var pitchData = {name: data.name, sport: data.sport /* PICTURE */};
				var formData = new FormData();
				formData.append('name', pitchData.name);
				formData.append('sport', pitchData.sport);
				/* PICTURE */

				return httpPost('/admin/clubs/' + clubid + '/pitches', formData, {});
			},
			createTournament: function(clubid, data) {
				var tournamentData = {name: data.name, maxTeams: data.maxTeams, teamSize: data.teamSize, firstRoundDate: data.firstRoundDate, startsAtHour: data.startsAtHour, endsAtHour: data.endsAtHour, inscriptionEndDate: data.inscriptionEndDate};
				var formData = new FormData();
				formData.append('name', tournamentData.name);
				formData.append('maxTeams', tournamentData.maxTeams);
				formData.append('teamSize', tournamentData.teamSize);
				formData.append('firstRoundDate', tournamentData.firstRoundDate);
				formData.append('startsAtHour', tournamentData.startsAtHour);
				formData.append('endsAtHour', tournamentData.endsAtHour);
				formData.append('inscriptionEndDate', tournamentData.inscriptionEndDate);
				return httpPost('/admin/clubs/' + clubid + '/tournaments', formData, {});
			},
			deleteClub: function(id) {
				return httpDelete('/admin/clubs/' + id, {});
			},
			deleteEvent: function(id) {
				return httpDelete('/admin/events/' + id, {});
			},
			deletePitch: function(clubid, pitchid) {
				return httpDelete('/admin/clubs/' + clubid + '/pitches/' + pitchid, {});
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
			getTournament: function(id) {
		        return httpGet('/tournaments/' + id, {});
		    },
		    getTournamentEvent: function(tournamentid, eventid) {
		        return httpGet('/tournaments/' + tournamentid + '/events/' + eventid, {});
		    },
		    getTournamentEventFirstTeam: function(tournamentid, eventid) {
		        return httpGet('/tournaments/' + tournamentid + '/events/' + eventid + '/first-team-members', {});
		    },
		    getTournamentEventSecondTeam: function(tournamentid, eventid) {
		        return httpGet('/tournaments/' + tournamentid + '/events/' + eventid + '/second-team-members', {});
		    },
		    getTournamentRound: function(id, params) {
		        return httpGet('/tournaments/' + id + '/round', {roundPageNum: params.roundPageNum});
		    },
		    getTournamentTeams: function(id) {
		        return httpGet('/tournaments/' + id + '/teams', {});
		    },
		    getTournamentTeamsInscriptions: function(id) {
		    	return httpGet('/tournaments/' + id + '/inscription', {});
		    },
			getTournaments: function(params) {
		        return httpGet('/tournaments', {pageNum: params.pageNum});
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
			getUserProfilePicture: function(id) {
				var headers = {};
				headers = addAuthHeader(headers);

				return $http({method: 'GET', url: url + '/users/' + id + '/picture', responseType: 'arraybuffer'})
					.then(function(response) {
				    	return _arrayBufferToBase64(response.data);
				    });
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
			joinTournament: function(tournamentid, teamid) {
				return httpPost('/tournaments/' + tournamentid + '/team/' + teamid + '/join', {}, {});
			},
			kickUser: function(pitchid, eventid, userid) {
				return httpPost('/pitches/' + pitchid + '/events/' + eventid + '/kick-user/' + userid, {}, {});
			},
			leaveEvent: function(pitchid, eventid) {
				return httpPost('/pitches/' + pitchid + '/events/' + eventid + '/leave', {}, {});
			},
			leaveTournament: function(id) {
				return httpPost('/tournaments/' + id + '/leave', {}, {});
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
			},
			setTournamentEventResult: function(clubid, tournamentid, eventid, data) {
				var resultData = {firstResult: data.firstTeamScore, secondResult: data.secondTeamScore};
				var formData = new FormData();
				formData.append('firstResult', resultData.firstResult);
				formData.append('secondResult', resultData.secondResult);
				return httpPost('/admin/clubs/' + clubid + '/tournaments/' + tournamentid + '/events/' + eventid + '/result', formData, {});
			}
		}

	}]);

});
