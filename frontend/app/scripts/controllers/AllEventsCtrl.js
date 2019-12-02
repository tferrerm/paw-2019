'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('AllEventsCtrl', ['$scope', '$location', 'restService', function($scope, $location, restService) {
		
		$scope.currentDate = new Date();
    	$scope.aWeekFromNow = new Date();
		$scope.sports = [];

		restService.getAllEvents().then(function(data) {
			$scope.events = data.events;
			$scope.eventCount = data.eventCount;
			$scope.lastPageNum = data.lastPageNum;
			$scope.initialPageIndex = data.initialPageIndex;
		});

		$scope.goToEvent = function(id) {
			alert('hola ' + id);
			$location.url('events/' + id);
		};

  	}]);

});
