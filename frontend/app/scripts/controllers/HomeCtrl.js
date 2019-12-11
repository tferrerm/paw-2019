'use strict';
define(['frontend', 'services/restService', 'services/authService'], function(frontend) {

	frontend.controller('HomeCtrl', ['$scope', '$filter', '$location', 'restService', 'authService', function($scope, $filter, $location, restService, authService) {
		
		$scope.noParticipations = false;
    	$scope.scheduleHeaders = [];

		updateSchedule();

		$scope.goToEvents = function() {
			$location.url('events/');
		};

		$scope.goToPitches = function() {
			$location.url('pitches/');
		};

		$scope.$on('user:updated', function() {
			updateSchedule();
		});

		function updateSchedule() {
			if ($scope.isLoggedIn) {
				restService.getUpcomingEvents(authService.getLoggedUser().userid).then(function(data) {
					$scope.schedule = data.schedule;
				}).catch(function(error) {
alert(error.data || ' Error');
});
			}
		}

		//$scope.now = ($filter('date')(new Date(), "EEEE"));
		//$scope.weekDays = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

	}]);
});
