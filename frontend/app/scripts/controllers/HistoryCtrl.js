'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('HistoryCtrl', ['$scope', '$location', 'restService', function($scope, $location, restService) {
	    var params = {pageNum: 1};
	    var userid = 2;

	    restService.getHistory(userid, params).then(function(data) {
			$scope.events = data.events;
			$scope.eventCount = data.eventCount;
			$scope.lastPageNum = data.lastPageNum;
			$scope.initialPageIndex = data.initialPageIndex;
			$scope.pageNum = params.pageNum;
		});

		$scope.getFirstPage = function() {
			params.pageNum = 1;
			restService.getHistory(userid, params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		};

		$scope.getPrevPage = function() {
			params.pageNum--;
			restService.getHistory(userid, params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		};

		$scope.getNextPage = function() {
			params.pageNum++;
			restService.getHistory(userid, params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		};

		$scope.getLastPage = function() {
			params.pageNum = $scope.lastPageNum;
			restService.getHistory(userid, params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		};
	}]);
});
