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
            '/events/:id' : {
                templateUrl: '/views/event.html',
                controller: 'EventCtrl',
                resolve: {
                    event: ['$route', 'restService', function($route, restService) {
                        var params = $route.current.params;
                        return restService.getEvent(params.id);
                    }]
                }
            }
            /* ===== yeoman hook ===== */
            /* Do not remove these commented lines! Needed for auto-generation */
        }
    };
});
