'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('ClubsCtrl', ['$scope', '$location', 'restService', function($scope, $location, restService) {
		var params = {pageNum: 1};
		$scope.filters = {};
		if($scope.isAdmin) {
			$scope.createdClub = {};
		}

	    updateClubs(params);

		$scope.goToClub = function(id) {
			$location.url('clubs/' + id);
		};

		$scope.getFirstPage = function() {
			params.pageNum = 1;
			updateClubs(params);
		};

		$scope.getPrevPage = function() {
			params.pageNum--;
			updateClubs(params);
		};

		$scope.getNextPage = function() {
			params.pageNum++;
			updateClubs(params);
		};

		$scope.getLastPage = function() {
			params.pageNum = $scope.lastPageNum;
			updateClubs(params);
		};

		$scope.filterClubs = function() {
			params = $scope.filters;
			params.pageNum = 1;
			updateClubs(params);
		};

		function updateClubs(params) {
			restService.getClubs(params).then(function(data) {
				$scope.clubs = data.clubs;
				$scope.clubCount = data.totalClubsMatching;
				$scope.lastPageNum = data.pagesCountMatching;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		};

		$scope.createClubSubmit = function() {
			//checkPasswordsMatch();
			//if ($scope.createEventForm.$valid) {
				//$scope.duplicateEmailError = false;
				
				if($scope.isAdmin) {
					restService.createClub($scope.createdClub).then(function(data) {
						//var createdEvent = data.event;
						$location.url('clubs/' + data.clubid);
					});

				}
			//}
		};

	}]);
});
