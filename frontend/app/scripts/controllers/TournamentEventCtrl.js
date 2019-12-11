'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('TournamentEventCtrl', ['$scope', '$location', 'restService', 'tournament', 'event', 'firstTeamMembers', 'secondTeamMembers', function ($scope, $location, restService, tournament, event, firstTeamMembers, secondTeamMembers) {
		
		$scope.tournament = tournament;
		$scope.event = event;
		$scope.firstTeamMembers = firstTeamMembers.users;
		$scope.secondTeamMembers = secondTeamMembers.users;

		$scope.goToTournament = function() {
			$location.url('tournaments/' + tournament.tournamentid);
		};

	}]);
});
