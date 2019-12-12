'use strict';
define(['frontend', 'services/restService', 'services/modalService'], function(frontend) {

	frontend.controller('TournamentInscriptionCtrl', ['$scope', '$location', 'restService', 'modalService', 'tournament', 'teamInscriptions', function ($scope, $location, restService, modalService, tournament, teamInscriptions) {
		
		$scope.tournament = tournament;
		$scope.hasJoined = teamInscriptions.hasJoined;
		setTournamentTeamPairs(teamInscriptions.teams);
		$scope.showLoginModal = modalService.loginModal;

		$scope.joinTeam = function(tournamentid, teamid) {
			if ($scope.isLoggedIn) {
				restService.joinTournament(tournamentid, teamid).then(function(data) {
	                restService.getTournamentTeamsInscriptions(tournament.tournamentid).then(function(data) {
						$scope.hasJoined = data.hasJoined;
						setTournamentTeamPairs(data.teams);
	                });
				});
			} else {
				$scope.showLoginModal().result.then(function(data) {
					restService.joinTournament(tournamentid, teamid).then(function(data) {
		                restService.getTournamentTeamsInscriptions(tournament.tournamentid).then(function(data) {
							$scope.hasJoined = data.hasJoined;
							setTournamentTeamPairs(data.teams);
		                });
					});
				});
			}
		};

		$scope.leaveTournament = function(id) {
			if ($scope.isLoggedIn) {
				restService.leaveTournament(id).then(function(data) {
	                restService.getTournamentTeamsInscriptions(tournament.tournamentid).then(function(data) {
						$scope.hasJoined = data.hasJoined;
						setTournamentTeamPairs(data.teams);
	                });
				});
			} else {
				$scope.showLoginModal().result.then(function(data) {
					restService.leaveTournament(id).then(function(data) {
		                restService.getTournamentTeamsInscriptions(tournament.tournamentid).then(function(data) {
							$scope.hasJoined = data.hasJoined;
							setTournamentTeamPairs(data.teams);
		                });
					});
				});
			}
		};

		$scope.goToClub = function(id) {
			$location.url('clubs/' + id);
		};

		function setTournamentTeamPairs(teamInscriptions) {
			$scope.tournamentTeams = [];
			for (var i = 0; i < teamInscriptions.length; i += 2) {
				$scope.tournamentTeams.push([teamInscriptions[i], teamInscriptions[i + 1]]);
			}
		}

		$scope.$on('user:updated', function() {
			if ($scope.isLoggedIn) {
		    	restService.getTournamentTeamsInscriptions(tournament.tournamentid).then(function(data) {
					$scope.hasJoined = data.hasJoined;
					//setTournamentTeamPairs(data.teams);
                });
		    } else {
		    	$scope.hasJoined = false;
		    }
		});

	}]);
});
