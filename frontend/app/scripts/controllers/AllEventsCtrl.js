'use strict';
define(['frontend', 'services/sampleService'], function(frontend) {

	frontend.controller('AllEventsCtrl', ['$scope', 'sampleService', function($scope, sampleService) {
		$scope.eventQty = 3;
		$scope.pageInitialIndex =  1;
		$scope.totalEventQty = 2;
		$scope.lastPageNum = 1;
    	$scope.currentDate = new Date();
    	$scope.aWeekFromNow = new Date();
		$scope.sports = [];
		
		/*$http({
			method: 'GET',
			url: 'http://localhost:8080/webapp/events'
		});*/

		sampleService.sampleFunction().then(function(data) {
			$scope.events = data.events;
		});

  	}]);

});
