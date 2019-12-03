'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('ClubsCtrl', ['$scope', '$location', 'restService', function($scope, $location, restService) {

	    $scope.clubQty = 1;
	    $scope.pageInitialIndex = 1;
	    $scope.pageNum = 2;
	    $scope.totalClubQty = 12;

	    restService.getClubs().then(function(data) {
				$scope.clubs = data.clubs;
		});

		$scope.goToClub = function(id) {
			$location.url('clubs/' + id);
		};

	}]);
});
