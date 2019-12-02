'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('ClubsCtrl', ['$scope', 'restService', function($scope, restService) {

	    $scope.clubQty = 1;
	    $scope.pageInitialIndex = 1;
	    $scope.pageNum = 2;
	    $scope.totalClubQty = 12;

	    restService.getClubs().then(function(data) {
				$scope.clubs = data.clubs;
		});

	}]);
});
