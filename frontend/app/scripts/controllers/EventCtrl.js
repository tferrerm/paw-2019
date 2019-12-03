'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('EventCtrl', ['$scope', '$filter', 'restService', 'event', 'inscriptions', function($scope, $filter, restService, event, inscriptions) {
		
		$scope.event = event;
		$scope.inscriptions = inscriptions.inscriptions;

		var inscriptionEnd = $filter('date')(event.inscriptionEnd, "dd/MM/yyyy HH:mm:ss", "GMT-3");
		var eventEnd = $filter('date')(event.endsAt, "dd/MM/yyyy HH:mm:ss", "GMT-3");
		var now = $filter('date')(new Date(), "dd/MM/yyyy HH:mm:ss", "GMT-3");
		$scope.inscriptionHasEnded = Date.parse(inscriptionEnd) < Date.parse(now);
		$scope.eventHasEnded = Date.parse(eventEnd) < Date.parse(now);

		//$scope.isOwner = event.owner.userid = 

		$scope.leaveEvent = function(id) {
			restService.leaveEvent(id).then(function(data) {
				updateEvent(id);
			});
		};

		$scope.joinEvent = function(id) {
			restService.joinEvent(id).then(function(data) {
				updateEvent(id);
			});
		};

		$scope.deleteEvent = function(id) {
			
		};

		function updateEvent(id) {
			restService.getEvent(id).then(function(data) {
				event = Object.assign(event, data);
			});
			restService.getEventInscriptions(id).then(function(data) {
				$scope.inscriptions = data.inscriptions;
			});
		}

  	}]);

});
