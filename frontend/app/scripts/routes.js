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
            }
            /* ===== yeoman hook ===== */
            /* Do not remove these commented lines! Needed for auto-generation */
        }
    };
});
