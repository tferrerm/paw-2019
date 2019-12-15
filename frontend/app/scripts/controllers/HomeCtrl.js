'use strict';
define(['frontend', 'services/restService', 'services/authService', 'services/titleService'], function(frontend) {

	frontend.controller('HomeCtrl', ['$scope', '$filter', '$location', 'restService', 'authService', 'titleService', function($scope, $filter, $location, restService, authService, titleService) {
		
    	$scope.scheduleHeaders = [];

    titleService.setDefaultTitle();

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
					var eventCount = data.schedule[0].eventCount + data.schedule[1].eventCount + data.schedule[2].eventCount + data.schedule[3].eventCount + data.schedule[4].eventCount + data.schedule[5].eventCount + data.schedule[6].eventCount;
					$scope.noParticipations = false;
					// TODO $scope.noParticipations = eventCount === 0;
				}).catch(function(error) {
            alert(error.data || ' Error');
				});
			}
		}

		var now = ($filter('date')(new Date(), 'EEEE'));
		var weekDays = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
		var minDays = ['day_sun', 'day_mon', 'day_tue', 'day_wed', 'day_thu', 'day_fri', 'day_sat'];
		var indexOfToday = weekDays.indexOf(now);
		$scope.scheduleHeaders = minDays.slice(indexOfToday, 7).concat(minDays.slice(0, indexOfToday));

	}]);
});
