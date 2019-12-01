'use strict';
define(['frontend', 'services/sampleService'], function(frontend) {

	frontend.controller('AllEventsCtrl', ['$scope', 'sampleService', function($scope, sampleService) {
		
		$scope.currentDate = new Date();
    	$scope.aWeekFromNow = new Date();
		$scope.sports = [];
		
		/*$http({
			method: 'GET',
			url: 'http://localhost:8080/webapp/events'
		});*/

		sampleService.sampleFunction().then(function(data) {
			$scope.events = data.events;
			$scope.eventCount = data.eventCount;
			$scope.lastPageNum = data.lastPageNum;
			$scope.initialPageIndex = data.initialPageIndex;
		});

  	}]);

});
