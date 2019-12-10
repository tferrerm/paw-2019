'use strict';
define(['frontend'], function(frontend) {

	frontend.controller('TournamentInscriptionCtrl', ['$scope', '$location', 'restService', 'tournament', 'teamInscriptions', function ($scope, $location, restService, tournament, teamInscriptions) {
		//roundsAmount
		//userJoined

		$scope.tournament = tournament;
		$scope.teamInscriptions = teamInscriptions;

		$scope.tournamentTeams = [];
		for(var i = 0; i < teamInscriptions.teams.length; i+=2) {
			$scope.tournamentTeams.push([teamInscriptions.teams[i], teamInscriptions.teams[i+1]]);
		}

		$scope.joinTeam = function(id) {
			// JOIN TEAM
		};

		$scope.goToClub = function(id) {
			$location.url('clubs/' + id);
		};

		$scope.getNumber = function(num) {
		    return new Array(num);   
		}

	}]);
})
