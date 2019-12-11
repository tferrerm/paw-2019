'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('AllEventsCtrl', ['$scope', '$location', 'restService', 'events', function($scope, $location, restService, events) {
		var params = {pageNum: 1};
		
		$scope.events = events.events;
		$scope.eventCount = events.eventCount;
		$scope.lastPageNum = events.lastPageNum;
		$scope.initialPageIndex = events.initialPageIndex;
		$scope.pageNum = params.pageNum;

		$scope.filters = {};
		$scope.currentDate = new Date();
    	
		restService.getSports().then(function(data) {
			$scope.sports = data.sports;
		}).catch((error) => alert(error.data || "Error"));

		$scope.getFirstPage = function() {
			params.pageNum = 1;
			updateEvents(params);
		};

		$scope.getPrevPage = function() {
			params.pageNum--;
			updateEvents(params);
		};

		$scope.getNextPage = function() {
			params.pageNum++;
			updateEvents(params);
		};

		$scope.getLastPage = function() {
			params.pageNum = $scope.lastPageNum;
			updateEvents(params);
		};

		$scope.filterEvents = function() {
			params = $scope.filters;
			params.pageNum = 1;
			updateEvents(params);
		}

		function updateEvents(params) {
			restService.getAllEvents(params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			}).catch((error) => alert(error.data || "Error"));
		}

  	}]);

});
