'use strict';
define(['frontend'], function(frontend) {

	frontend.controller('PitchesCtrl', function($scope) {
    $scope.pitches = [];
    $scope.pitchQty = 1;
    $scope.pageInitialIndex = 1;
    $scope.pageNum = 2;
    $scope.totalPitchQty = 12;

	});
});
