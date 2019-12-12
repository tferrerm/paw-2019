'use strict';

define([], function() {
	return {
		defaultRoutePath: '/home',
		routes: {
			'/home': {
				templateUrl: '/views/home.html',
				controller: 'HomeCtrl'
			},
			'/events' : {
				templateUrl: '/views/events.html',
				controller: 'AllEventsCtrl',
				resolve: {
					events: ['$route', 'restService', function($route, restService) {
						var params = $route.current.params;
						params.pageNum = 1;
						return restService.getAllEvents(params);
					}]
				}
			},
			'/my-events' : { // SOLO SI ESTAS LOGEADO
				templateUrl: '/views/myEvents.html',
				controller: 'MyEventsCtrl',
				resolve: {
					validate: ['$q', '$location', 'authService', function($q, $location, authService) {
					  	var defer = $q.defer();
						if (authService.isLoggedIn() && !authService.isAdmin()) {
						  	defer.resolve();
						} else {
						  	defer.reject('Access blocked');
						  	$location.path('/home');
						}

					}],
					pastEvents: ['$route', 'restService', 'authService', function($route, restService, authService) {
						var params = $route.current.params;
						params.pageNum = 1;
						return restService.getMyPastEvents(authService.getLoggedUser().userid, params);
					}],
					futureEvents: ['$route', 'restService', 'authService', function($route, restService, authService) {
						var params = $route.current.params;
						params.pageNum = 1;
						return restService.getMyFutureEvents(authService.getLoggedUser().userid, params);
					}]
				}
			},
			'/history' : {
				templateUrl: '/views/history.html',
				controller: 'HistoryCtrl',
				resolve: {
					validate: ['$q', '$location', 'authService', function($q, $location, authService) {
					  	var defer = $q.defer();
						if (authService.isLoggedIn() && !authService.isAdmin()) {
						  	defer.resolve();
						} else {
						  	defer.reject('Access blocked');
						  	$location.path('/home');
						}

					}]
				}
			},
			'/clubs' : {
				templateUrl: '/views/clubs.html',
				controller: 'ClubsCtrl'
			},
			'/pitches' : {
				templateUrl: '/views/pitches.html',
				controller: 'PitchesCtrl'
			},
			'/pitches/:pitchid/events/:eventid' : {
				templateUrl: '/views/event.html',
				controller: 'EventCtrl',
				resolve: {
					event: ['$route', 'restService', function($route, restService) {
						var params = $route.current.params;
						return restService.getEvent(params.pitchid, params.eventid);
					}],
					inscriptions: ['$route', 'restService', function($route, restService) {
						var params = $route.current.params;
						return restService.getEventInscriptions(params.pitchid, params.eventid);
					}]
				}
			},
			'/clubs/:id' : {
				templateUrl: '/views/club.html',
				controller: 'ClubCtrl',
				resolve: {
					club: ['$route', 'restService', function($route, restService) {
						var params = $route.current.params;
						return restService.getClub(params.id);
					}]
				}
			},
			'/pitches/:id' : {
				templateUrl: '/views/pitch.html',
				controller: 'PitchCtrl',
				resolve: {
					pitch: ['$route', 'restService', function($route, restService) {
						var params = $route.current.params;
						return restService.getPitch(params.id);
					}]
				}
			},
			'/users/:id' : {
				templateUrl: '/views/profile.html',
				controller: 'ProfileCtrl',
				resolve: {
					user: ['$route', 'restService', function($route, restService) {
						var params = $route.current.params;
						return restService.getUserProfile(params.id);
					}]
				}
			},
			'/tournaments' : {
			  templateUrl: '/views/tournaments.html',
			  controller: 'TournamentsCtrl'
			},
			'/tournaments/:id' : {
				templateUrl: '/views/tournament.html',
				controller: 'TournamentCtrl',
				resolve: {
					tournament: ['$route', 'restService', function($route, restService) {
						var params = $route.current.params;
						return restService.getTournament(params.id);
					}],
					teams: ['$route', 'restService', function($route, restService) {
						var params = $route.current.params;
						return restService.getTournamentTeams(params.id);
					}],
					round: ['$route', 'restService', function($route, restService) {
						var params = $route.current.params;
						return restService.getTournamentRound(params.id, {});
					}]
				}
			},
			'/tournaments/:tournamentid/events/:eventid' : {
				templateUrl: '/views/tournamentEvent.html',
				controller: 'TournamentEventCtrl',
				resolve: {
					tournament: ['$route', 'restService', function($route, restService) {
						var params = $route.current.params;
						return restService.getTournament(params.tournamentid);
					}],
					event: ['$route', 'restService', function($route, restService) {
						var params = $route.current.params;
						return restService.getTournamentEvent(params.tournamentid, params.eventid);
					}],
					firstTeamMembers: ['$route', 'restService', function($route, restService) {
						var params = $route.current.params;
						return restService.getTournamentEventFirstTeam(params.tournamentid, params.eventid);
					}],
					secondTeamMembers: ['$route', 'restService', function($route, restService) {
						var params = $route.current.params;
						return restService.getTournamentEventSecondTeam(params.tournamentid, params.eventid);
					}]
				}
			},
			'/tournaments/:id/inscription' : {
				templateUrl: '/views/tournamentInscription.html',
				controller: 'TournamentInscriptionCtrl',
				resolve: {
					tournament: ['$route', 'restService', function($route, restService) {
						var params = $route.current.params;
						return restService.getTournament(params.id);
					}],
					teamInscriptions: ['$route', 'restService', function($route, restService) {
						var params = $route.current.params;
						return restService.getTournamentTeamsInscriptions(params.id);
					}]
				}
			},
			'/admin/clubs/:id/tournaments/new' : {
				templateUrl: '/views/tournamentNew.html',
				controller: 'TournamentNewCtrl',
				resolve: {
					validate: ['$q', '$location', 'authService', function($q, $location, authService) {
					  	var defer = $q.defer();
						if (authService.isAdmin()) {
						  	defer.resolve();
						} else {
						  	defer.reject('Access blocked');
						  	$location.path('/home');
						}

					}],
					club: ['$route', 'restService', function($route, restService) {
						var params = $route.current.params;
						return restService.getClub(params.id);
					}]
				}
			}
			/* ===== yeoman hook ===== */
			/* Do not remove these commented lines! Needed for auto-generation */
		}
	};
});
