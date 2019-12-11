'use strict';
define(['frontend', 'services/restService', 'services/authService', 'services/modalService'], function(frontend) {

	frontend.controller('EventCtrl', ['$scope', '$filter', '$location', '$route', 'restService', 'authService', 'modalService', 'event', 'inscriptions', function($scope, $filter, $location, $route, restService, authService, modalService, event, inscriptions) {
		
		$scope.event = event;
		$scope.inscriptions = inscriptions.inscriptions;

		var inscriptionEnd = $filter('date')(event.inscriptionEnd, "dd/MM/yyyy HH:mm:ss", "GMT-3");
		var eventEnd = $filter('date')(event.endsAt, "dd/MM/yyyy HH:mm:ss", "GMT-3");
		var now = $filter('date')(new Date(), "dd/MM/yyyy HH:mm:ss", "GMT-3");
		$scope.inscriptionHasEnded = Date.parse(inscriptionEnd) < Date.parse(now);
		$scope.eventHasEnded = Date.parse(eventEnd) < Date.parse(now);

		$scope.showLoginModal = modalService.loginModal;

		updateOwner();

		$scope.kickUser = function(pitchid, eventid, userid) {
			restService.kickUser(pitchid, eventid, userid).then(function(data) {
				updateEvent(pitchid, eventid);
			}).catch((error) => alert(error.data || "Error"));;
		}

		$scope.leaveEvent = function(pitchid, eventid) {
			restService.leaveEvent(pitchid, eventid).then(function(data) {
				updateEvent(pitchid, eventid);
			}).catch((error) => alert(error.data || "Error"));;
		};

		$scope.joinEvent = function(pitchid, eventid) {
			if($scope.isLoggedIn) {
				restService.joinEvent(pitchid, eventid).then(function(data) {
					updateEvent(pitchid, eventid);
				}).catch((error) => alert(error.data || "Error"));;
			} else {
				$scope.showLoginModal().result.then(function(data) {
					restService.joinEvent(pitchid, eventid).then(function(data) {
						updateEvent(pitchid, eventid);
					}).catch((error) => alert(error.data || "Error"));;
				});
			}
		};

		$scope.cancelEvent = function(pitchid, eventid) {
			restService.cancelEvent(pitchid, eventid)
				.then(function(data) {
					$location.url('events');
				});
		};

		function updateEvent(pitchid, eventid) {
			restService.getEvent(pitchid, eventid).then(function(data) {
				event = Object.assign(event, data);
			});
			restService.getEventInscriptions(pitchid, eventid).then(function(data) {
				$scope.inscriptions = data.inscriptions;
			}).catch((error) => alert(error.data || "Error"));;
			updateOwner();
		}

		function updateOwner() {
			if($scope.isLoggedIn) {
				$scope.isOwner = event.owner.userid == $scope.loggedUser.userid;
			} else {
				$scope.isOwner = false;
			}
		}

		$scope.deleteEvent = function(eventid) {
			restService.deleteEvent(eventid)
				.then(function(data) {
					$location.url('events');
				});
		};

		$scope.$on('user:updated', function() {
			updateOwner();
		});

  	}]);

});
