'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('AllEventsCtrl', ['$scope', '$location', 'restService', function($scope, $location, restService) {
		var params = {pageNum: 1};
		$scope.filters = {};
		$scope.currentDate = new Date();
    	
		restService.getSports().then(function(data) {
			$scope.sports = data.sports;
		});

		restService.getAllEvents(params).then(function(data) {
			$scope.events = data.events;
			$scope.eventCount = data.eventCount;
			$scope.lastPageNum = data.lastPageNum;
			$scope.initialPageIndex = data.initialPageIndex;
			$scope.pageNum = params.pageNum;
		});

		$scope.goToEvent = function(pitchid, eventid) {
			$location.url('pitches/' + pitchid + '/events/' + eventid);
		};

		$scope.getFirstPage = function() {
			params.pageNum = 1;
			restService.getAllEvents(params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			}).catch((error) => alert(error.data || "Error"));
		};

		$scope.getPrevPage = function() {
			params.pageNum--;
			restService.getAllEvents(params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			}).catch((error) => alert(error.data || "Error"));
		};

		$scope.getNextPage = function() {
			params.pageNum++;
			restService.getAllEvents(params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			}).catch((error) => alert(error.data || "Error"));
		};

		$scope.getLastPage = function() {
			params.pageNum = $scope.lastPageNum;
			restService.getAllEvents(params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			}).catch((error) => alert(error.data || "Error"));
		};

		$scope.filterEvents = function() {
			params = $scope.filters;
			params.pageNum = 1;
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
