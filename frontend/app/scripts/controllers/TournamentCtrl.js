'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('TournamentCtrl', ['$scope', '$location', 'restService', 'tournament', 'teams', 'round', function ($scope, $location, restService, tournament, teams, round) {
		$scope.tournament = tournament;
		$scope.teams = teams.teams;
		$scope.roundEvents = round.events;
		$scope.currentRound = round.currentRound;
		$scope.roundPageNum = round.round;
		$scope.now = Date.parse(new Date());
		var params = {roundPageNum: round.round};

		$scope.goToTournamentEvent = function (id) {
			//$location.url('tournament/' + id);
		};

		$scope.goToTournamentTeam = function (id) {
			//$location.url('tournament/' + id);
		};

		$scope.getFirstPage = function () {
			params.roundPageNum = 1;
			updateRound(params);
		};

		$scope.getPrevPage = function () {
			params.roundPageNum--;
			updateRound(params);
		};

		$scope.getNextPage = function () {
			params.roundPageNum++;
			updateRound(params);
		};

		$scope.getLastPage = function () {
			params.roundPageNum = tournament.rounds;
			updateRound(params);
		};

		function updateRound(params) {
			restService.getTournamentRound(tournament.tournamentid, params).then(function(data) {
				$scope.roundEvents = data.events;
				$scope.currentRound = data.currentRound;
				$scope.roundPageNum = params.roundPageNum;
			});
		}

		function updateTeams() {
			restService.getTournamentTeams(tournament.tournamentid).then(function(data) {
				$scope.teams = data.teams;
			});
		}

		$scope.setTournamentResultSubmit = function(event) {
			//checkPasswordsMatch();
			//if ($scope.createEventForm.$valid) {
				//$scope.duplicateEmailError = false;
				
				if($scope.isAdmin) {
					restService.setTournamentEventResult(tournament.tournamentClub.clubid, tournament.tournamentid, event.eventid, event).then(function(data) {
						updateTeams();
						updateRound(params);
					});

				}

			//}
		};

		$scope.parseDate = function(date) {
			return Date.parse(date);
		}

	}]);
})
