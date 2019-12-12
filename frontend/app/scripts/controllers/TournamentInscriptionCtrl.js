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
	                updateTeams();
				}).catch(function(error) {
					if (error.status === 403) {
						if (error.data.error === 'UserBusy') {
							$scope.userBusyError = true;
						} else if (error.data.error === 'TeamFull') {
							$scope.teamFullError = true;
							updateTeams();
						} else if (error.data.error === 'InscriptionClosed') {
							$scope.inscriptionClosedError = true;
							// REDIRIGIR
						} else if (error.data.error === 'AlreadyJoined') {
							$scope.alreadyJoinedError = true;
							updateTeams();
						}
					}
				});
			} else {
				$scope.showLoginModal().result.then(function(data) {
					restService.joinTournament(tournamentid, teamid).then(function(data) {
		                updateTeams();
					}).catch(function(error) {
						if (error.status === 403) {
							if (error.data.error === 'UserBusy') {
								$scope.userBusyError = true;
							} else if (error.data.error === 'TeamFull') {
								$scope.teamFullError = true;
								updateTeams();
							} else if (error.data.error === 'InscriptionClosed') {
								$scope.inscriptionClosedError = true;
								// REDIRIGIR
							} else if (error.data.error === 'AlreadyJoined') {
								$scope.alreadyJoinedError = true;
								updateTeams();
							}
						}
					});
				});
			}
		};

		$scope.leaveTournament = function(id) {
			restService.leaveTournament(id).then(function(data) {
	            updateTeams();
			});
		};

		$scope.goToClub = function(id) {
			$location.url('clubs/' + id);
		};

		function updateTeams() {
			restService.getTournamentTeamsInscriptions(tournament.tournamentid).then(function(data) {
				$scope.hasJoined = data.hasJoined;
				setTournamentTeamPairs(data.teams);
            });
		}

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
