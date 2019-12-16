'use strict';
define(['frontend', 'services/restService', 'services/titleService'], function(frontend) {

	frontend.controller('TournamentsCtrl', ['$scope', '$location', '$filter','restService', 'titleService', function ($scope, $location, $filter, restService, titleService) {
		var params = {pageNum: 1};

		$scope.now = new Date();

		updateTournaments(params);

		titleService.setTitle($filter('translate')('tournaments'));

		$scope.goToTournament = function (tournament) {
			if (tournament.inscriptionSuccess) {
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

		$scope.inscriptionPhase = function(inscription) {
			var inscriptionEnd = $filter('date')(new Date(Date.parse(inscription)), 'MM/dd/yyyy HH:mm:ss', 'GMT-3');
			var now = $filter('date')(new Date(), 'MM/dd/yyyy HH:mm:ss', 'GMT-3');
			return Date.parse(inscriptionEnd) > Date.parse(now);
		};
	}]);
});
