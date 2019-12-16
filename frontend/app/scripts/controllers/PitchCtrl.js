'use strict';
define(['frontend', 'services/restService', 'services/authService', 'services/modalService', 'services/titleService', 'directives/tooltip'], function(frontend) {

	frontend.controller('PitchCtrl', ['$scope', '$filter', '$location', 'restService', 'authService', 'modalService', 'titleService', 'pitch', function($scope, $filter, $location, restService, authService, modalService, titleService, pitch) {
    
    titleService.setTitle(pitch.name);

		$scope.minHour = 0;
		$scope.maxHour = 0;
	    $scope.pitch = pitch;
	    $scope.schedule = [];
	    $scope.startsAtHours = [];
	    $scope.endsAtHours = [];
	    $scope.tableHours = [];

    var today = new Date();
    var tomorrow = new Date(today);
    $scope.todayDate = tomorrow.getFullYear() + '-' + (tomorrow.getMonth() + 1 < 10 ? '0' : '') + (tomorrow.getMonth() + 1) + '-' + (tomorrow.getDate() < 10 ? '0' : '') + tomorrow.getDate();
    tomorrow.setDate(today.getDate() + 1);
    $scope.tomorrowDate = tomorrow.getFullYear() + '-' + (tomorrow.getMonth() + 1 < 10 ? '0' : '') + (tomorrow.getMonth() + 1) + '-' + (tomorrow.getDate() < 10 ? '0' : '') + tomorrow.getDate();
    var sevenDaysadd = new Date(tomorrow);
    sevenDaysadd.setDate(sevenDaysadd.getDate() + 6);
    $scope.sevenDaysFromTomorrow = sevenDaysadd.getFullYear() + '-' + (sevenDaysadd.getMonth() + 1 < 10 ? '0' : '') + (sevenDaysadd.getMonth() + 1) + '-' + (sevenDaysadd.getDate() < 10 ? '0' : '') + sevenDaysadd.getDate();
    sevenDaysadd.setDate(sevenDaysadd.getDate() - 1);
    $scope.maxInscriptionDate = sevenDaysadd.getFullYear() + '-' + (sevenDaysadd.getMonth() + 1 < 10 ? '0' : '') + (sevenDaysadd.getMonth() + 1) + '-' + (sevenDaysadd.getDate() < 10 ? '0' : '') + sevenDaysadd.getDate();


    restService.getPitchPicture(pitch.pitchid).then(function(data) {
    		$scope.picture = 'data:image/png;base64,' + _arrayBufferToBase64(data);
    	}).catch(function(error) {
    		$scope.picture = 'images/pitch_default.jpg';
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
	    			$scope.tableHours.push(i + ':00');
	    			$scope.startsAtHours.push({id: i, format: i + ':00'});
	    		}
	    		if (i !== $scope.minHour) {
	    			$scope.endsAtHours.push({id: i, format: i + ':00'});
	    		}
	    	}

	    }).then(function(data) {
	    	restService.getPitchWeekEvents(pitch.pitchid).then(function(data) {
	    		$scope.schedule = Array.apply(null, Array($scope.maxHour - $scope.minHour));
			    for (var i = 0; i < ($scope.maxHour - $scope.minHour); i++) {
			    	$scope.schedule[i] = Array.apply(null, Array(7));
			    }

		    	var weekEvents = data.events;
		    	angular.forEach(weekEvents, function(event, index) {
		    		var startsDate = new Date(Date.parse(event.startsAt));
		    		var endsDate = new Date(Date.parse(event.endsAt));
		    		/* Sunday = 0 */
		    		var todayDayIndex = (new Date()).getDay();
		    		var eventDayIndex = ((startsDate.getDay() - (todayDayIndex + 1)) + 7) % 7;
		    		var startHourIndex = startsDate.getHours() - $scope.minHour;
		    		var endHourIndex = endsDate.getHours() - $scope.minHour - 1;
		    		
		    		for (var i = startHourIndex; i <= endHourIndex; i++) {
		    			$scope.schedule[i][eventDayIndex] = {eventid: event.eventid, pitchid: event.pitch.pitchid, name: event.name, maxParticipants: event.maxParticipants, inscriptionCount: event.inscriptionCount};
		    		}
		    	});
		    });
		});

	    $scope.event = {};
	    $scope.showLoginModal = modalService.loginModal;

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
			if ($scope.createEventForm.$valid && isFinite($scope.event.endsAtHour) && isFinite($scope.event.startsAtHour)) {
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
				if (error.data.constraintViolations != null) { // eslint-disable-line eqeqeq
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
					if (error.data.error === 'EndsBeforeStarts') { // eslint-disable-line no-lonely-if
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

		var now = ($filter('date')(new Date(), 'EEEE'));
		var weekDays = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
		var minDays = ['day_sun', 'day_mon', 'day_tue', 'day_wed', 'day_thu', 'day_fri', 'day_sat'];
		var indexOfTomorrow = (weekDays.indexOf(now) + 1) % 7;
		$scope.dayHeaders = minDays.slice(indexOfTomorrow, 7).concat(minDays.slice(0, indexOfTomorrow));

	    $scope.$watch('event.selectedEndsAtHour', function (newValue) {
	    	var startVal = parseInt($('#startsAtHour').val(), 10);
	       	var endVal = parseInt($('#endsAtHour').val(), 10);
	       	if (startVal >= endVal || !isFinite(startVal)) {
	         	var endIndex = $('#endsAtHour').prop('selectedIndex');
	         	$('#startsAtHour').prop('selectedIndex', endIndex);
	         	$scope.event.startsAtHour = endVal - 1;
	       	}
			$scope.event.endsAtHour = endVal;
		});

		$scope.$watch('event.selectedStartsAtHour', function (newValue) {
	    	var startVal = parseInt($('#startsAtHour').val(), 10);
	       	var endVal = parseInt($('#endsAtHour').val(), 10);
	       	if (startVal >= endVal || !isFinite(endVal)) {
	         	var startIndex = $('#startsAtHour').prop('selectedIndex');
	         	$('#endsAtHour').prop('selectedIndex', startIndex);
	         	$scope.event.endsAtHour = startVal + 1;
	       	}
			$scope.event.startsAtHour = startVal;
		});

		$scope.isFinite = function(n) {
			return isFinite(n);
		};

	}]);
});
