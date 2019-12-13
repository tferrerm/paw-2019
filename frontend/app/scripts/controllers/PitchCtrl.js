'use strict';
define(['frontend', 'services/restService', 'services/authService', 'services/modalService', 'directives/tooltip'], function(frontend) {

	frontend.controller('PitchCtrl', ['$scope', '$filter', '$location', 'restService', 'authService', 'modalService', 'pitch', function($scope, $filter, $location, restService, authService, modalService, pitch) {
    	
		$scope.isLoggedIn = authService.isLoggedIn();
		$scope.minHour = 0;
		$scope.maxHour = 0;
	    $scope.pitch = pitch;
	    $scope.schedule = [];
	    $scope.startsAtHours = [];
	    $scope.endsAtHours = [];

    	restService.getPitchPicture(pitch.pitchid).then(function(data) {
    		$scope.picture = 'data:image/png;base64,' + _arrayBufferToBase64(data);
    	}).catch(function(error) {
    		$scope.picture = '../../images/pitch_default.jpg';
    	});

			function _arrayBufferToBase64(buffer) {
		    var binary = '';
		    var bytes = new Uint8Array(buffer);
		    var len = bytes.byteLength;
		    for (var i = 0; i < len; i++) {
		      binary += String.fromCharCode(bytes[i]);
		    }
		    return window.btoa(binary);
			}
	    
	    restService.getHourRange().then(function(data) {
	    	$scope.minHour = data.minHour;
	    	$scope.maxHour = data.maxHour;
	    	
	    	for (var i = $scope.minHour; i <= $scope.maxHour; i++) {
	    		if (i !== $scope.maxHour) {
	    			$scope.startsAtHours.push(i);
	    		}
	    		if (i !== $scope.minHour) {
	    			$scope.endsAtHours.push(i);
	    		}
	    	}
	    	
	    }).then(function(data) {
	    	restService.getPitchWeekEvents(pitch.pitchid, {}).then(function(data) {
	    		$scope.schedule = Array.apply(null, Array($scope.maxHour - $scope.minHour));
			    for (var i = 0; i < ($scope.maxHour - $scope.minHour); i++) {
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
		    		
		    		for (var i = startHourIndex; i <= endHourIndex; i++) {
		    			$scope.schedule[i][eventDayIndex] = {eventid: event.eventid, pitchid: event.pitch.pitchid, name: event.name, maxParticipants: event.maxParticipants, inscriptionCount: event.inscriptionCount};
		    		}
		    	});
		    });
		}).catch(function(error) {
			alert(error.data || ' Error');
		});

	    $scope.event = {};
	    $scope.showLoginModal = modalService.loginModal;

	    $scope.$on('user:updated', function() {
			$scope.isLoggedIn = authService.isLoggedIn();
		});

		$scope.$watch('event.selectedDate', function (newValue) {
			$scope.event.date = $filter('date')(newValue, 'yyyy-MM-dd');
		});

		$scope.$watch('event.selectedInscriptionDate', function (newValue) {
			$scope.event.inscriptionEndDate = $filter('date')(newValue, 'yyyy-MM-ddTHH:mm:ss');
		});

		$scope.goToClub = function(id) {
			$location.url('clubs/' + id);
		};

	    $scope.createEventSubmit = function() {
			if ($scope.createEventForm.$valid) {
				resetErrors();
				if ($scope.isLoggedIn) {
					restService.createEvent($scope.pitch.pitchid, $scope.event).then(function(data) {
						$location.url('pitches/' + $scope.pitch.pitchid + '/events/' + data.eventid);
					}).catch(function(error) {
						validateForm(error);
					});

				} else {
					$scope.showLoginModal().result.then(function(data) {
						restService.createEvent($scope.pitch.pitchid, $scope.event).then(function(data) {
							$location.url('pitches/' + $scope.pitch.pitchid + '/events/' + data.eventid);
						}).catch(function(error) {
							validateForm(error);
						});
					});
					
				}
			}
		};

		function validateForm(error) {
			if (error.status === 422) {
				if (error.data.constraintViolations != null) {
					/* Controller violation */
					angular.forEach(error.data.constraintViolations, function(cv) {
							switch (cv.propertyPath) {
								case 'date':
									$scope.dateError = true;
									break;
								case 'inscriptionEndDate':
									$scope.inscriptionDateError = true;
									break;
								default:
							}
					});
				} else {
					/* Service violation */
					if (error.data.error === 'EndsBeforeStarts') {
						$scope.endsBeforeStartsError = true;
					} else if (error.data.error === 'MaximumStartDateExceeded') {
						$scope.maximumStartDateExceededError = true;
					} else if (error.data.error === 'MaximumInscriptionDateExceeded') {
						$scope.maximumInscriptionDateExceededError = true;
					} else if (error.data.error === 'EventOverlap') {
						$scope.eventOverlapError = true;
					}
				}
			}
		}

		function resetErrors() {
			$scope.dateError = false;
			$scope.inscriptionDateError = false;
			$scope.endsBeforeStartsError = false;
			$scope.maximumStartDateExceededError = false;
			$scope.maximumInscriptionDateExceededError = false;
			$scope.eventOverlapError = false;
		}

	}]);
});
