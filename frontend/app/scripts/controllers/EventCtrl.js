'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('EventCtrl', ['$scope', '$filter', 'restService', 'event', function($scope, $filter, restService, event) {
		
		$scope.event = event;

		var inscriptionEnd = $filter('date')(event.inscriptionEnd, "dd/MM/yyyy HH:mm:ss", "GMT-3");
		var eventEnd = $filter('date')(event.endsAt, "dd/MM/yyyy HH:mm:ss", "GMT-3");
		var now = $filter('date')(new Date(), "dd/MM/yyyy HH:mm:ss", "GMT-3");
		$scope.inscriptionHasEnded = Date.parse(inscriptionEnd) < Date.parse(now);
		$scope.eventHasEnded = Date.parse(eventEnd) < Date.parse(now);

		//$scope.isOwner = event.owner.userid = 


		restService.getEventInscriptions(event.eventid).then(function(data) {
			$scope.inscriptions = data.inscriptions;
		});

		$scope.leaveEvent = function(id) {
			
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
		}

  	}]);

});
