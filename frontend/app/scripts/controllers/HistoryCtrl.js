'use strict';
define(['frontend', 'services/restService', 'services/authService'], function(frontend) {

	frontend.controller('HistoryCtrl', ['$scope', '$location', '$q', 'restService', 'authService', function($scope, $location, $q, restService, authService) {
	    var params = {pageNum: 1};

	    restService.getHistory($scope.loggedUser.userid, params).then(function(data) {
			$scope.events = data.events;
			$scope.eventCount = data.eventCount;
			$scope.lastPageNum = data.lastPageNum;
			$scope.initialPageIndex = data.initialPageIndex;
			$scope.pageNum = params.pageNum;
		});
	    
		$scope.$on('user:updated', function() {
		    if ($scope.isLoggedIn) {
	    		restService.getHistory($scope.loggedUser.userid, params).then(function(data) {
					$scope.events = data.events;
					$scope.eventCount = data.eventCount;
					$scope.lastPageNum = data.lastPageNum;
					$scope.initialPageIndex = data.initialPageIndex;
					$scope.pageNum = params.pageNum;
				});
		    } else {
	    		var defer = $q.defer();
	    		defer.reject('Access blocked');
				$location.path('/home');
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
			}).catch(function(error) {
alert(error.data || ' Error');
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
			}).catch(function(error) {
alert(error.data || ' Error');
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
			}).catch(function(error) {
alert(error.data || ' Error');
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
			}).catch(function(error) {
alert(error.data || ' Error');
});
		};
	}]);
});
