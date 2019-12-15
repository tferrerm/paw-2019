'use strict';
define(['frontend', 'services/restService', 'services/titleService'], function(frontend) {

	frontend.controller('TournamentEventCtrl', ['$scope', '$location', 'restService', 'titleService', 'tournament', 'event', function ($scope, $location, restService, titleService, tournament, event) {
		
		$scope.tournament = tournament;
		$scope.event = event;

		restService.getTournamentEventFirstTeam(tournament.tournamentid, event.eventid).then(function(data) {
			$scope.firstTeamMembers = data.users;
		});

		restService.getTournamentEventSecondTeam(tournament.tournamentid, event.eventid).then(function(data) {
			$scope.secondTeamMembers = data.users;
		});

		titleService.setTitle(event.name);

		$scope.goToTournament = function() {
			$location.url('tournaments/' + tournament.tournamentid);
		};

	}]);
});
