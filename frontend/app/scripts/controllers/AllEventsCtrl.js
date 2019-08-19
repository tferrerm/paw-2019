'use strict';
define(['frontend'], function(frontend) {

	frontend.controller('AllEventsCtrl', function($scope) {
		$scope.eventQty = 2;
		$scope.pageInitialIndex =  1;
		$scope.totalEventQty = 2;
		$scope.lastPageNum = 1;
    $scope.currentDate = new Date();
    $scope.aWeekFromNow = new Date();
		$scope.sports = [];
    $scope.events = [{name: 'Event'},{name: 'Event'}]
  })
});
