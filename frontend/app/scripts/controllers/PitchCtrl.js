'use strict';
define(['frontend', 'services/restService', 'services/authService', 'services/modalService'], function(frontend) {

	frontend.controller('PitchCtrl', ['$scope', '$filter', '$location', 'restService', 'authService', 'modalService', 'pitch', function($scope, $filter, $location, restService, authService, modalService, pitch) {
    	
		$scope.isLoggedIn = authService.isLoggedIn();
		$scope.minHour = 0;
		$scope.maxHour = 0;
	    $scope.pitch = pitch;
	    $scope.schedule = [];
	    $scope.startsAtHours = [];
	    $scope.endsAtHours = [];
	    
	    restService.getHourRange().then(function(data) {
	    	$scope.minHour = data.minHour;
	    	$scope.maxHour = data.maxHour;
	    	
	    	for(var i = $scope.minHour; i <= $scope.maxHour; i++) {
	    		if(i != $scope.maxHour) {
	    			$scope.startsAtHours.push(i);
	    		}
	    		if(i != $scope.minHour) {
	    			$scope.endsAtHours.push(i);
	    		}
	    	}
	    	
	    }).then(function(data) {
	    	restService.getPitchWeekEvents(pitch.pitchid, {}).then(function(data) {
	    		$scope.schedule = Array.apply(null, Array($scope.maxHour - $scope.minHour));
			    for(var i = 0; i < ($scope.maxHour - $scope.minHour); i++) {
			    	$scope.schedule[i] = Array.apply(null, Array(7));
			    }

		    	var weekEvents = data.events;
		    	angular.forEach(weekEvents, function(event, index) {
		    		var startsDate = new Date(Date.parse(event.startsAt));
		    		var endsDate = new Date(Date.parse(event.endsAt));
		    		/* Sunday = 0 --> Sunday = 6 */
		    		var todayDayIndex = (((new Date()).getDay() - 1) + 7) % 7;
		    		var eventDayIndex = (((((startsDate.getDay() - 1) + 7) % 7) - todayDayIndex) + 7) % 7;
		    		var startHourIndex = startsDate.getHours() - $scope.minHour;
		    		var endHourIndex = endsDate.getHours() - $scope.minHour - 1;
		    		
		    		for(var i = startHourIndex; i <= endHourIndex; i++) {
		    			$scope.schedule[i][eventDayIndex] = {name: event.name, maxParticipants: event.maxParticipants, inscriptionCount: event.inscriptionCount};
		    		}
		    	})
		    });
		});

	    $scope.event = {};
	    $scope.showLoginModal = modalService.loginModal;

	    $scope.$on('user:updated', function() {
			$scope.isLoggedIn = authService.isLoggedIn();
		});

		$scope.$watch('event.selectedDate', function (newValue) {
			$scope.event.date = $filter('date')(newValue, "yyyy-MM-dd");
		});

		$scope.$watch('event.selectedInscriptionDate', function (newValue) {
			$scope.event.inscriptionEndDate = $filter('date')(newValue, "yyyy-MM-ddTHH:mm:ss");
		});

	    $scope.createEventSubmit = function() {
			//checkPasswordsMatch();
			//if ($scope.createEventForm.$valid) {
				//$scope.duplicateEmailError = false;
				
				if($scope.isLoggedIn) {
					restService.createEvent($scope.pitch.pitchid, $scope.event).then(function(data) {
						//var createdEvent = data.event;
						$location.url('pitches/' + $scope.pitch.pitchid + '/events/' + data.eventid);
					});

				} else {
					$scope.showLoginModal().result.then(function(data) {
						restService.createEvent($scope.pitch.pitchid, $scope.event).then(function(data) {
							//var createdEvent = data.event;
							$location.url('pitches/' + $scope.pitch.pitchid + '/events/' + data.eventid);
						});
					});
					
				}
			//}
		};

	}]);
});
