'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('HomeCtrl', ['$scope', '$location', 'restService', function($scope, $location, restService) {
		$scope.loggedUser = {name: "Santiago"};
		$scope.noParticipations = false;
    	$scope.scheduleHeaders = [];
    	$scope.myEvents = []

    	restService.getUpcomingEvents(2).then(function(data) {
			$scope.schedule = data.schedule;
		});
	}]);
});
