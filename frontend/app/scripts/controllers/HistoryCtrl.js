'use strict';
define(['frontend'], function(frontend) {

	frontend.controller('HistoryCtrl', function($scope) {
    $scope.eventQty = 2;
    $scope.past_participations = [];
    $scope.pageInitialIndex = 1;
    $scope.eventQty = 3;
    $scope.totalEventQty = 5;
	});
});
