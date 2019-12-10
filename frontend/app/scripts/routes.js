'use strict';

define([], function() {
    return {
        defaultRoutePath: '/',
        routes: {
            '/home': {
                templateUrl: '/views/home.html',
                controller: 'HomeCtrl'
            },
            '/events' : {
                templateUrl: '/views/events.html',
                controller: 'AllEventsCtrl'
            },
            '/my-events' : {
              templateUrl: '/views/myEvents.html',
              controller: 'MyEventsCtrl'
            },
            '/history' : {
              templateUrl: '/views/history.html',
              controller: 'HistoryCtrl'
            },
            '/clubs' : {
              templateUrl: '/views/clubs.html',
              controller: 'ClubsCtrl'
            },
            '/pitches' : {
              templateUrl: '/views/pitches.html',
              controller: 'PitchesCtrl'
            },
            '/tournaments' : {
              templateUrl: '/views/tournaments.html',
              controller: 'TournamentsCtrl'
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
            }
            /* ===== yeoman hook ===== */
            /* Do not remove these commented lines! Needed for auto-generation */
        }
    };
});
