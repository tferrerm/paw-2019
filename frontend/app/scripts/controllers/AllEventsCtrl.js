'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('AllEventsCtrl', ['$scope', '$location', 'restService', function($scope, $location, restService) {
		var params = {pageNum: 1};
		$scope.currentDate = new Date();
    	$scope.aWeekFromNow = new Date();
		$scope.sports = [];

		restService.getAllEvents(params).then(function(data) {
			$scope.events = data.events;
			$scope.eventCount = data.eventCount;
			$scope.lastPageNum = data.lastPageNum;
			$scope.initialPageIndex = data.initialPageIndex;
			$scope.pageNum = params.pageNum;
		});

		$scope.goToEvent = function(id) {
			$location.url('events/' + id);
		};

		$scope.getPrevPage = function() {
			params.pageNum--;
			restService.getAllEvents(params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		};

		$scope.getNextPage = function() {
			params.pageNum++;
			restService.getAllEvents(params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		};

  	}]);

});
