'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('HistoryCtrl', ['$scope', '$location', 'restService', function($scope, $location, restService) {
	    var params = {pageNum: 1};

	    restService.getHistory(params).then(function(data) {
			$scope.events = data.events;
			$scope.eventCount = data.eventCount;
			$scope.lastPageNum = data.lastPageNum;
			$scope.initialPageIndex = data.initialPageIndex;
			$scope.pageNum = params.pageNum;
		});
	}]);
});
