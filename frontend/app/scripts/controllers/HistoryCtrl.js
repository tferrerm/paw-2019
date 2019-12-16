'use strict';
define(['frontend', 'services/restService', 'services/authService', 'services/titleService'], function(frontend) {

	frontend.controller('HistoryCtrl', ['$scope', '$location', '$q', '$filter', 'restService', 'authService', 'titleService', function($scope, $location, $q, $filter, restService, authService, titleService) {
		var params = {pageNum: 1};

		titleService.setTitle($filter('translate')('history'));

		updateHistory($scope.loggedUser, params);

		$scope.goToEvent = function(pitchid, eventid) {
			$location.url('pitches/' + pitchid + '/events/' + eventid);
		};

		$scope.goToEvents = function() {
			$location.url('events/');
		};

		$scope.getFirstPage = function() {
			params.pageNum = 1;
			updateHistory($scope.loggedUser, params);
		};

		$scope.getPrevPage = function() {
			params.pageNum--;
			updateHistory($scope.loggedUser, params);
		};

		$scope.getNextPage = function() {
			params.pageNum++;
			updateHistory($scope.loggedUser, params);
		};

		$scope.getLastPage = function() {
			params.pageNum = $scope.lastPageNum;
			updateHistory($scope.loggedUser, params);
		};

		function updateHistory(user, params) {
			restService.getHistory(user.userid, params).then(function(data) {
				$scope.events = data.events;
				$scope.eventCount = data.eventCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		}

	}]);
});
