'use strict';
define(['frontend', 'services/restService', 'services/authService'], function(frontend) {

	frontend.controller('HomeCtrl', ['$scope', '$location', 'restService', 'authService', function($scope, $location, restService, authService) {
		
		$scope.noParticipations = false;
    	$scope.scheduleHeaders = [];

		$scope.isLoggedIn = authService.isLoggedIn();

		updateSchedule();

		$scope.goToEvents = function() {
			$location.url('events/');
		};

		$scope.goToPitches = function() {
			$location.url('pitches/');
		};

		$scope.$on('user:updated', function() {
			$scope.isLoggedIn = authService.isLoggedIn();
			updateSchedule();
		});

		function updateSchedule() {
			if($scope.isLoggedIn) {
				restService.getUpcomingEvents(authService.getLoggedUser().userid).then(function(data) {
					$scope.schedule = data.schedule;
				}).catch((error) => alert(error.data || "Error"));;
			}
		}

	}]);
});
