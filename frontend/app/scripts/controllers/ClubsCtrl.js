'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('ClubsCtrl', ['$scope', '$location', 'restService', function($scope, $location, restService) {
		var params = {pageNum: 1};
		$scope.filters = {};

	    restService.getClubs(params).then(function(data) {
			$scope.clubs = data.clubs;
			$scope.clubCount = data.totalClubsMatching;
			$scope.lastPageNum = data.pagesCountMatching;
			$scope.initialPageIndex = data.initialPageIndex;
			$scope.pageNum = params.pageNum;
		});

		$scope.goToClub = function(id) {
			$location.url('clubs/' + id);
		};

		$scope.getFirstPage = function() {
			params.pageNum = 1;
			restService.getClubs(params).then(function(data) {
				$scope.clubs = data.clubs;
				$scope.clubCount = data.totalClubsMatching;
				$scope.lastPageNum = data.pagesCountMatching;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		};

		$scope.getPrevPage = function() {
			params.pageNum--;
			restService.getClubs(params).then(function(data) {
				$scope.clubs = data.clubs;
				$scope.clubCount = data.totalClubsMatching;
				$scope.lastPageNum = data.pagesCountMatching;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		};

		$scope.getNextPage = function() {
			params.pageNum++;
			restService.getClubs(params).then(function(data) {
				$scope.clubs = data.clubs;
				$scope.clubCount = data.totalClubsMatching;
				$scope.lastPageNum = data.pagesCountMatching;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		};

		$scope.getLastPage = function() {
			params.pageNum = $scope.lastPageNum;
			restService.getClubs(params).then(function(data) {
				$scope.clubs = data.clubs;
				$scope.clubCount = data.totalClubsMatching;
				$scope.lastPageNum = data.pagesCountMatching;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		};

		$scope.filterClubs = function() {
			params = $scope.filters;
			params.pageNum = 1;
			restService.getClubs(params).then(function(data) {
				$scope.clubs = data.clubs;
				$scope.clubCount = data.totalClubsMatching;
				$scope.lastPageNum = data.pagesCountMatching;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		}

	}]);
});
