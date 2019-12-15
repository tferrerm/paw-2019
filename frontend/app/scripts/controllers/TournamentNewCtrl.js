'use strict';
define(['frontend', 'services/restService', 'services/authService', 'services/titleService'], function(frontend) {

	frontend.controller('TournamentNewCtrl', ['$scope', '$filter', '$location', 'restService', 'authService', 'titleService', 'club', function ($scope, $filter, $location, restService, authService, titleService, club) {
		
		$scope.namePattern = '^[a-zA-Z0-9 ]+$';
		$scope.minHour = 0;
		$scope.maxHour = 0;
	    $scope.club = club;
	    $scope.schedule = [];
	    $scope.startsAtHours = [];
	    $scope.endsAtHours = [];

	    titleService.setTitle($filter('translate')('create_tournament'));
	    
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
	    	restService.getClubWeekEvents(club.clubid).then(function(data) {
	    		$scope.pitchCount = data.pitchCount;
	    		$scope.schedule = Array.apply(null, Array($scope.maxHour - $scope.minHour));
			    for (var i = 0; i < ($scope.maxHour - $scope.minHour); i++) {
			    	var arr = [0, 0, 0, 0, 0, 0, 0];
			    	$scope.schedule[i] = arr;
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
		    			$scope.schedule[i][eventDayIndex] += 1;
		    		}
		    	});
		    });
		});

	    $scope.tournament = {};

		$scope.$watch('tournament.selectedDate', function (newValue) {
			$scope.tournament.firstRoundDate = $filter('date')(newValue, 'yyyy-MM-dd');
		});

		$scope.$watch('tournament.selectedInscriptionDate', function (newValue) {
			$scope.tournament.inscriptionEndDate = $filter('date')(newValue, 'yyyy-MM-ddTHH:mm:ss');
		});

	    $scope.createTournamentSubmit = function() {
			if ($scope.evenInputTeams() && $scope.createTournamentForm.$valid) {
				if ($scope.isAdmin) {
					resetErrors();
					restService.createTournament($scope.club.clubid, $scope.tournament).then(function(data) {
						$location.url('tournaments/' + data.tournamentid + '/inscription');
					}).catch(function(error) {
						validateForm(error);
					});
				}

			}
		};

		function validateForm(error) {
			console.log(error);
			console.log($scope.tournament.firstRoundDate);
			if (error.status === 422) {
				if (error.data.constraintViolations !== null) {
					/* Controller violation */
					angular.forEach(error.data.constraintViolations, function(cv) {
						switch (cv.propertyPath) {
							case 'firstRoundDate':
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
					} else if (error.data.error === 'InsufficientPitches') {
						$scope.insufficientPitchesError = true;
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
			$scope.insufficientPitchesError = false;
		}

		$scope.evenInputTeams = function() {
			return $scope.tournament.maxTeams % 2 === 0;
		};
		
	}]);
});
