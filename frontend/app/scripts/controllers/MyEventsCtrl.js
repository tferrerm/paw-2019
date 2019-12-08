'use strict';
define(['frontend', 'services/restService', 'services/authService'], function(frontend) {

	frontend.controller('MyEventsCtrl', ['$scope', '$location', 'restService', 'authService', function($scope, $location, restService, authService) {
	    var pastEventParams = {pageNum: 1};
	    var futureEventParams = {pageNum: 1};

	    $scope.isLoggedIn = authService.isLoggedIn();
	    if($scope.isLoggedIn) {
	    	$scope.loggedUser = authService.getLoggedUser();
    		
    		restService.getMyPastEvents($scope.loggedUser.userid, pastEventParams).then(function(data) {
				$scope.pastEvents = data.events;
				$scope.pastEventCount = data.eventCount;
				$scope.pastEventsLastPageNum = data.lastPageNum;
				$scope.pastEventsInitialPageIndex = data.initialPageIndex;
				$scope.pastEventsPageNum = pastEventParams.pageNum;
			});

			restService.getMyFutureEvents($scope.loggedUser.userid, futureEventParams).then(function(data) {
				$scope.futureEvents = data.events;
				$scope.futureEventCount = data.eventCount;
				$scope.futureEventsLastPageNum = data.lastPageNum;
				$scope.futureEventsInitialPageIndex = data.initialPageIndex;
				$scope.futureEventsPageNum = futureEventParams.pageNum;
			});
	    } else {
	    	// REDIRECCIONAR
	    	alert('LOGIN');
	    }

		$scope.$on('user:updated', function() {
			$scope.isLoggedIn = authService.isLoggedIn();
		    if($scope.isLoggedIn) {
		    	$scope.loggedUser = authService.getLoggedUser();

	    		restService.getMyPastEvents($scope.loggedUser.userid, pastEventParams).then(function(data) {
					$scope.pastEvents = data.events;
					$scope.pastEventCount = data.eventCount;
					$scope.pastEventsLastPageNum = data.lastPageNum;
					$scope.pastEventsInitialPageIndex = data.initialPageIndex;
					$scope.pastEventsPageNum = pastEventParams.pageNum;
				});

				restService.getMyFutureEvents($scope.loggedUser.userid, futureEventParams).then(function(data) {
					$scope.futureEvents = data.events;
					$scope.futureEventCount = data.eventCount;
					$scope.futureEventsLastPageNum = data.lastPageNum;
					$scope.futureEventsInitialPageIndex = data.initialPageIndex;
					$scope.futureEventsPageNum = futureEventParams.pageNum;
				});
		    } else {
	    		// REDIRECCIONAR
	    		alert('LOGIN');
	    	}
		});

		$scope.goToEvent = function(pitchid, eventid) {
			$location.url('pitches/' + pitchid + '/events/' + eventid);
		};

		$scope.getPastEventsFirstPage = function() {
			pastEventParams.pageNum = 1;
			updatePastEvents(pastEventParams);
		};

		$scope.getPastEventsPrevPage = function() {
			pastEventParams.pageNum--;
			updatePastEvents(pastEventParams);
		};

		$scope.getPastEventsNextPage = function() {
			pastEventParams.pageNum++;
			updatePastEvents(pastEventParams);
		};

		$scope.getPastEventsLastPage = function() {
			pastEventParams.pageNum = $scope.lastPageNum;
			updatePastEvents(pastEventParams);
		};

		function updatePastEvents(params) {
			restService.getMyPastEvents(userid, params).then(function(data) {
				$scope.pastEvents = data.events;
				$scope.pastEventCount = data.eventCount;
				$scope.pastEventsLastPageNum = data.lastPageNum;
				$scope.pastEventsInitialPageIndex = data.initialPageIndex;
				$scope.pastEventsPageNum = params.pageNum;
			});
		}

		$scope.getFutureEventsFirstPage = function() {
			futureEventParams.pageNum = 1;
			updatePastEvents(futureEventParams);
		};

		$scope.getFutureEventsPrevPage = function() {
			futureEventParams.pageNum--;
			updatePastEvents(futureEventParams);
		};

		$scope.getFutureEventsNextPage = function() {
			futureEventParams.pageNum++;
			updatePastEvents(futureEventParams);
		};

		$scope.getFutureEventsLastPage = function() {
			futureEventParams.pageNum = $scope.lastPageNum;
			updatePastEvents(futureEventParams);
		};

		function updatePastEvents(params) {
			restService.getMyFutureEvents(userid, params).then(function(data) {
				$scope.futureEvents = data.events;
				$scope.futureEventCount = data.eventCount;
				$scope.futureEventsLastPageNum = data.lastPageNum;
				$scope.futureEventsInitialPageIndex = data.initialPageIndex;
				$scope.futureEventsPageNum = params.pageNum;
			});
		}

	}]);
});
