'use strict';
define(['frontend', 'services/restService', 'services/authService'], function(frontend) {

	frontend.controller('HistoryCtrl', ['$scope', '$location', 'restService', 'authService', function($scope, $location, restService, authService) {
	    var params = {pageNum: 1};

	    $scope.isLoggedIn = authService.isLoggedIn();
	    if($scope.isLoggedIn) {
	    	$scope.loggedUser = authService.getLoggedUser();
    		restService.getHistory($scope.loggedUser.userid, params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
	    } else {
	    	// REDIRECCIONAR
	    }

		$scope.$on('user:updated', function() {
			$scope.isLoggedIn = authService.isLoggedIn();
		    if($scope.isLoggedIn) {
		    	$scope.loggedUser = authService.getLoggedUser();
	    		restService.getHistory($scope.loggedUser.userid, params).then(function(data) {
					$scope.events = data.events;
					$scope.eventCount = data.eventCount;
					$scope.lastPageNum = data.lastPageNum;
					$scope.initialPageIndex = data.initialPageIndex;
					$scope.pageNum = params.pageNum;
				});
		    } else {
	    		// REDIRECCIONAR
	    	}
		});

		$scope.goToEvent = function(pitchid, eventid) {
			$location.url('pitches/' + pitchid + '/events/' + eventid);
		};

		$scope.getFirstPage = function() {
			params.pageNum = 1;
			restService.getHistory($scope.loggedUser.userid, params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		};

		$scope.getPrevPage = function() {
			params.pageNum--;
			restService.getHistory($scope.loggedUser.userid, params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		};

		$scope.getNextPage = function() {
			params.pageNum++;
			restService.getHistory($scope.loggedUser.userid, params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		};

		$scope.getLastPage = function() {
			params.pageNum = $scope.lastPageNum;
			restService.getHistory($scope.loggedUser.userid, params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		};
	}]);
});
