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
		$scope.isLoggedIn = authService.isLoggedIn();
		$scope.showLoginModal = modalService.loginModal;

		if($scope.isLoggedIn) {
			$scope.isOwner = event.owner.userid == authService.getLoggedUser().userid;
		} else {
			$scope.isOwner = false;
		}

		$scope.goToClub = function(id) {
			$location.url('clubs/' + id);
		};

		$scope.goToPitch = function(id) {
			$location.url('pitches/' + id);
		};

		$scope.goToProfile = function(id) {
			$location.url('users/' + id);
		}

		$scope.kickUser = function(id) {
			restService.kickUser(id).then(function(data) {
				updateEvent(id);
			});
		}

		$scope.leaveEvent = function(id) {
			restService.leaveEvent(id).then(function(data) {
				updateEvent(id);
			});
		};

		$scope.joinEvent = function(id) {
			if($scope.isLoggedIn) {
				restService.joinEvent(id).then(function(data) {
					updateEvent(id);
				});
			} else {
				$scope.showLoginModal();
			}
		};

		$scope.deleteEvent = function(id) {
			restService.deleteEvent(id);
		};

		function updateEvent(id) {
			restService.getEvent(id).then(function(data) {
				event = Object.assign(event, data);
			});
			restService.getEventInscriptions(id).then(function(data) {
				$scope.inscriptions = data.inscriptions;
			});
		}

		$scope.$on('user:updated', function() {
			$route.reload();
		});

  	}]);

});
