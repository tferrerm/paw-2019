'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('MyEventsCtrl', ['$scope', '$location', 'restService', function($scope, $location, restService) {
	    var pastEventParams = {pageNum: 1};
	    var futureEventParams = {pageNum: 1};
	    var userid = 2;

	    restService.getMyPastEvents(userid, pastEventParams).then(function(data) {
			$scope.pastEvents = data.events;
			$scope.pastEventCount = data.eventCount;
			$scope.pastEventsLastPageNum = data.lastPageNum;
			$scope.pastEventsInitialPageIndex = data.initialPageIndex;
			$scope.pastEventsPageNum = pastEventParams.pageNum;
		});

		restService.getMyFutureEvents(userid, futureEventParams).then(function(data) {
			$scope.futureEvents = data.events;
			$scope.futureEventCount = data.eventCount;
			$scope.futureEventsLastPageNum = data.lastPageNum;
			$scope.futureEventsInitialPageIndex = data.initialPageIndex;
			$scope.futureEventsPageNum = futureEventParams.pageNum;
		});

		$scope.noEvents = false;//($scope.pastEvents.length == 0) && ($scope.futureEvents.length == 0);
	}]);
});
