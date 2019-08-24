'use strict';
define(['frontend'], function(frontend) {

	frontend.controller('ClubsCtrl', function($scope) {
    $scope.clubs = [];
    $scope.clubQty = 1;
    $scope.pageInitialIndex = 1;
    $scope.pageNum = 2;
    $scope.totalClubQty = 12;

	});
});
