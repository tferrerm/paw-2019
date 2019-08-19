'use strict';
define(['frontend'], function(frontend) {

	frontend.controller('MyEventsCtrl', function($scope) {
    $scope.past_events = [];
    $scope.future_events = [];
    $scope.no_events = false;
    $scope.page = 2;
    $scope.lastPageNum = 3;
	});
});
