'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('PitchesCtrl', ['$scope', 'restService', function($scope, restService) {
    
	    $scope.pitchQty = 1;
	    $scope.pageInitialIndex = 1;
	    $scope.pageNum = 2;
	    $scope.totalPitchQty = 12;

	    restService.getPitches().then(function(data) {
			$scope.pitches = data.pitches;
		});

	}]);
});
