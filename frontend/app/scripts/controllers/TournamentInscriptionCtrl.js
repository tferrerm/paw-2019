'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('TournamentInscriptionCtrl', ['$scope', '$location', 'restService', 'tournament', 'teamInscriptions', function ($scope, $location, restService, tournament, teamInscriptions) {
		
		$scope.tournament = tournament;
		$scope.hasJoined = teamInscriptions.hasJoined;
		setTournamentTeamPairs(teamInscriptions.teams);

		$scope.joinTeam = function(tournamentid, teamid) {
			restService.joinTournament(tournamentid, teamid).then(function(data) {
                restService.getTournamentTeamsInscriptions(tournament.tournamentid).then(function(data) {
					$scope.hasJoined = data.hasJoined;
					setTournamentTeamPairs(data.teams);
                });
			});
		};

		$scope.leaveTournament = function(id) {
			restService.leaveTournament(id).then(function(data) {
                restService.getTournamentTeamsInscriptions(tournament.tournamentid).then(function(data) {
					$scope.hasJoined = data.hasJoined;
					setTournamentTeamPairs(data.teams);
                });
			});
		};

		$scope.goToClub = function(id) {
			$location.url('clubs/' + id);
		};

		function setTournamentTeamPairs(teamInscriptions) {
			$scope.tournamentTeams = [];
			for(var i = 0; i < teamInscriptions.length; i+=2) {
				$scope.tournamentTeams.push([teamInscriptions[i], teamInscriptions[i+1]]);
			}
		}

	}]);
})
