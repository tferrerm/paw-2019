'use strict';
define(['frontend', 'services/restService', 'services/modalService', 'services/titleService'], function(frontend) {

	frontend.controller('TournamentInscriptionCtrl', ['$scope', '$location', '$filter', 'restService', 'modalService', 'titleService', 'tournament', 'teamInscriptions', function ($scope, $location, $filter, restService, modalService, titleService, tournament, teamInscriptions) {
		
		$scope.tournament = tournament;
		$scope.hasJoined = teamInscriptions.hasJoined;
		setTournamentTeamPairs(teamInscriptions.teams);
		$scope.showLoginModal = modalService.loginModal;
		updateInscriptionEnded();

		titleService.setTitle(tournament.name);

		if ($scope.isAdmin) {
			$scope.showDeleteConfirmModal = modalService.deleteConfirmModal;
		}

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
							updateInscriptionEnded();
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
								updateInscriptionEnded();
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
			}).catch(function(error) {
				if (error.status === 403) {
					if (error.data.error === 'InscriptionClosed') {
						$scope.inscriptionClosedError = true;
						updateInscriptionEnded();
					}
				}
			});
		};

		$scope.kickUser = function(clubid, tournamentid, userid) {
			restService.kickUserFromTournament(clubid, tournamentid, userid).then(function(data) {
				updateTeams();
			}).catch(function(error) {
				if (error.status === 403) {
					if (error.data.error === 'InscriptionClosed') {
						$scope.inscriptionClosedError = true;
						$scope.inscriptionHasEnded = true;
					}
				}
			});
		};

		$scope.deleteTournament = function(clubid, tournamentid) {
			$scope.showDeleteConfirmModal().result.then(function(data) {
				restService.deleteTournament(clubid, tournamentid)
				.then(function(data) {
					$location.url('tournaments');
				}).catch(function(error) {
					if (error.status === 403) {
						if (error.data.error === 'InscriptionClosed') {
							$scope.inscriptionClosedError = true;
							updateInscriptionEnded();
						}
					}
				});
			});
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

		function updateInscriptionEnded() {
			var inscriptionEnd = $filter('date')(tournament.inscriptionEnd, 'dd/MM/yyyy HH:mm:ss', 'GMT-3');
			var now = $filter('date')(new Date(), 'dd/MM/yyyy HH:mm:ss', 'GMT-3');
			$scope.inscriptionHasEnded = Date.parse(inscriptionEnd) < Date.parse(now);
		}

		$scope.$on('user:updated', function() {
			if ($scope.isLoggedIn) {
		    	restService.getTournamentTeamsInscriptions(tournament.tournamentid).then(function(data) {
					$scope.hasJoined = data.hasJoined;
                });
		    } else {
		    	$scope.hasJoined = false;
		    }
		    updateInscriptionEnded();
		});

	}]);
});
