'use strict';
define(['frontend', 'services/restService', 'services/authService'], function(frontend) {

	frontend.controller('HomeCtrl', ['$scope', '$location', 'restService', 'authService', function($scope, $location, restService, authService) {
		$scope.loggedUser = {name: "Santiago"};
		$scope.noParticipations = false;
    	$scope.scheduleHeaders = [];
    	$scope.myEvents = []

    	restService.getUpcomingEvents(2).then(function(data) {
			$scope.schedule = data.schedule;
		});

    	$scope.loginUser = {};
		$scope.login = function() {
			authService.login($scope.loginUser.username, $scope.loginUser.password, false/*$scope.loginUser.rememberMe*/);
		}
	}]);
});
