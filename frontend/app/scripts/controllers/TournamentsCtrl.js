'use strict';
define(['frontend'], function(frontend) {

	frontend.controller('TournamentsCtrl', ['$scope', '$location', 'restService', function ($scope, $location, restService) {
		var params = {pageNum: 1};

		$scope.now = new Date();

		updateTournaments(params);

		$scope.goToTournament = function (tournament) {
			if(tournament.inscriptionSuccess) {
				$location.url('tournaments/' + tournament.tournamentid);
			} else {
				$location.url('tournaments/' + tournament.tournamentid + '/inscription');
			}
		};

		$scope.getFirstPage = function () {
			params.pageNum = 1;
			updateTournaments(params);
		};

		$scope.getPrevPage = function () {
			params.pageNum--;
			updateTournaments(params);
		};

		$scope.getNextPage = function () {
			params.pageNum++;
			updateTournaments(params);
		};

		$scope.getLastPage = function () {
			params.pageNum = $scope.lastPageNum;
			updateTournaments(params);
		};

		function updateTournaments(params) {
			restService.getTournaments(params).then(function(data) {
				$scope.tournaments = data.tournaments;
				$scope.tournamentCount = data.tournamentCount;
				$scope.lastPageNum = data.lastPageNum;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		}
	}]);
})
