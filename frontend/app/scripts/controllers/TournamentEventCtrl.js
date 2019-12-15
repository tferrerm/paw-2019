'use strict';
define(['frontend', 'services/restService', 'services/titleService'], function(frontend) {

	frontend.controller('TournamentEventCtrl', ['$scope', '$location', 'restService', 'titleService', 'tournament', 'event', 'firstTeamMembers', 'secondTeamMembers', function ($scope, $location, restService, titleService, tournament, event, firstTeamMembers, secondTeamMembers) {
		
		$scope.tournament = tournament;
		$scope.event = event;
		$scope.firstTeamMembers = firstTeamMembers.users;
		$scope.secondTeamMembers = secondTeamMembers.users;

		titleService.setTitle(event.name);

		$scope.goToTournament = function() {
			$location.url('tournaments/' + tournament.tournamentid);
		};

	}]);
});
