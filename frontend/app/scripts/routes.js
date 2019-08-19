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
            }
            /* ===== yeoman hook ===== */
            /* Do not remove these commented lines! Needed for auto-generation */
        }
    };
});
