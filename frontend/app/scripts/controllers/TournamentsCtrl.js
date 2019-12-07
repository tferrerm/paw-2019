'use strict';
define(['frontend'], function(frontend) {

	frontend.controller('ClubsCtrl', function($scope) {
    $scope.tournaments = [];
    $scope.tournamentQty = 0;
    $scope.pageInitialIndex = 0;
    $scope.pageNum = 0;
    $scope.totalTournamentQty = 0;
    $scope.now = new Date();

    $scope.goToTournament = function(id) {
      $location.url('tournament/' + id);
    };

    $scope.getFirstPage = function() {
      params.pageNum = 1;
      restService.getTournaments(params).then(function(data) {
        $scope.tournaments = data.tournaments;
        $scope.tournamentQty = data.tournamentQty;
        $scope.lastPageNum = data.pagesCountMatching;
        $scope.initialPageIndex = data.initialPageIndex;
        $scope.pageNum = params.pageNum;
      });
    };

    $scope.getPrevPage = function() {
      params.pageNum--;
      restService.getTournaments(params).then(function(data) {
        $scope.tournaments = data.tournaments;
        $scope.tournamentCount = data.totalTournamentsMatching;
        $scope.lastPageNum = data.pagesCountMatching;
        $scope.initialPageIndex = data.initialPageIndex;
        $scope.pageNum = params.pageNum;
      });
    };

    $scope.getNextPage = function() {
      params.pageNum++;
      restService.getClubs(params).then(function(data) {
        $scope.tournaments = data.clubs;
        $scope.tournamentsCount = data.totalTournamentsMatching;
        $scope.lastPageNum = data.pagesCountMatching;
        $scope.initialPageIndex = data.initialPageIndex;
        $scope.pageNum = params.pageNum;
      });
    };

    $scope.getLastPage = function() {
      params.pageNum = $scope.lastPageNum;
      restService.getClubs(params).then(function(data) {
        $scope.tournaments = data.clubs;
        $scope.tournamentsCount = data.totalTournamentsMatching;
        $scope.lastPageNum = data.pagesCountMatching;
        $scope.initialPageIndex = data.initialPageIndex;
        $scope.pageNum = params.pageNum;
      });
    };

	});
});
