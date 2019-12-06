'use strict';
define(['frontend', 'services/restService', 'services/authService'], function(frontend) {

	frontend.controller('PitchCtrl', ['$scope', '$filter', 'restService', 'authService', 'pitch', function($scope, $filter, restService, authService, pitch) {
    	
		$scope.isLoggedIn = authService.isLoggedIn();

	    $scope.pitch = pitch;
	    $scope.minHour = 9; // PEDIR DE BACK
	    $scope.maxHour = 23; // PEDIR DE BACK
	    $scope.schedule = Array.apply(null, Array($scope.maxHour - $scope.minHour));
	    for(var i = 0; i < ($scope.maxHour - $scope.minHour); i++) {
	    	$scope.schedule[i] = Array.apply(null, Array(7));
	    }
	    
	    restService.getPitchWeekEvents(pitch.pitchid, {}).then(function(data) {
	    	var weekEvents = data.events;
	    	angular.forEach(weekEvents, function(event, index) {
	    		var startsDate = new Date(Date.parse(event.startsAt));
	    		var endsDate = new Date(Date.parse(event.endsAt));
	    		/* Sunday = 0 --> Sunday = 6 */
	    		var todayDayIndex = (((new Date()).getUTCDay() - 1) + 7) % 7;
	    		var eventDayIndex = (((((startsDate.getUTCDay() - 1) + 7) % 7) - todayDayIndex) + 7) % 7;
	    		var startHourIndex = startsDate.getUTCHours() - $scope.minHour;
	    		var endHourIndex = endsDate.getUTCHours() - $scope.minHour - 1;
	    		
	    		for(var i = startHourIndex; i <= endHourIndex; i++) {
	    			$scope.schedule[i][eventDayIndex] = {name: event.name, maxParticipants: event.maxParticipants, inscriptionCount: event.inscriptionCount};
	    		}
	    	});
	    	
	    });

	    $scope.event = {};

	    $scope.createEventSubmit = function() {
			//checkPasswordsMatch();
			if ($scope.createEventForm.$valid) {
				//$scope.duplicateEmailError = false;
				//$scope.loggingIn = true;

				restService.createEvent($scope.event); // then redirect to event
			}
		};

	}]);
});
