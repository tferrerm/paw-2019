'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('PitchesCtrl', ['$scope', '$location', 'restService', function($scope, $location, restService) {
    	var params = {pageNum: 1};
	    $scope.pitchQty = 1;
	    $scope.pageInitialIndex = 1;
	    $scope.pageNum = 2;
	    $scope.totalPitchQty = 12;

	    restService.getPitches(params).then(function(data) {
			$scope.pitches = data.pitches;
		});

		$scope.goToPitch = function(id) {
			$location.url('pitches/' + id);
		};

	}]);
});
