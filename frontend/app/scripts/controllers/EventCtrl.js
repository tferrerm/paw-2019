'use strict';
define(['frontend', 'services/restService', 'services/authService', 'services/modalService', 'services/titleService'], function(frontend) {

	frontend.controller('EventCtrl', ['$scope', '$filter', '$location', '$route', 'restService', 'authService', 'modalService', 'titleService', 'event', 'inscriptions', function($scope, $filter, $location, $route, restService, authService, modalService, titleService, event, inscriptions) {
		
		$scope.event = event;
		$scope.inscriptions = inscriptions.inscriptions;
		$scope.showLoginModal = modalService.loginModal;

		titleService.setTitle(event.name);

		updateOwner();
		updateDates();

		$scope.$on('user:updated', function() {
			if ($scope.isLoggedIn) {
				updateEvent(event.pitch.pitchid, event.eventid);
				updateOwner();
				if ($scope.isOwner || $scope.isAdmin) {
					$scope.showDeleteConfirmModal = modalService.deleteConfirmModal;
				}
			}
		});

		if ($scope.isOwner || $scope.isAdmin) {
			$scope.showDeleteConfirmModal = modalService.deleteConfirmModal;
		}

		$scope.kickUser = function(pitchid, eventid, userid) {
			restService.kickUser(pitchid, eventid, userid).then(function(data) {
				updateEvent(pitchid, eventid);
				updateInscriptions(pitchid, eventid);
			}).catch(function(error) {
				if (error.status === 403) {
					if (error.data.error === 'InscriptionClosed') {
						$scope.inscriptionClosedError = true;
						$scope.inscriptionHasEnded = true;
					}
				}
			});
		};

		$scope.leaveEvent = function(pitchid, eventid) {
			restService.leaveEvent(pitchid, eventid).then(function(data) {
				updateEvent(pitchid, eventid);
				updateInscriptions(pitchid, eventid);
			}).catch(function(error) {
				if (error.status === 403) {
					if (error.data.error === 'InscriptionClosed') {
						$scope.inscriptionClosedError = true;
						$scope.inscriptionHasEnded = true;
					}
				}
			});
		};

		$scope.joinEvent = function(pitchid, eventid) {
			if ($scope.isLoggedIn) {
				restService.joinEvent(pitchid, eventid).then(function(data) {
					updateEvent(pitchid, eventid);
					updateInscriptions(pitchid, eventid);
				}).catch(function(error) {
					if (error.status === 403) {
						if (error.data.error === 'UserBusy') {
							$scope.userBusyError = true;
						} else if (error.data.error === 'EventFull') {
							$scope.eventFullError = true;
						} else if (error.data.error === 'InscriptionClosed') {
							$scope.inscriptionClosedError = true;
							$scope.inscriptionHasEnded = true;
						} else if (error.data.error === 'AlreadyJoined') {
							$scope.alreadyJoinedError = true;
						}
					}
				});
			} else {
				$scope.showLoginModal().result.then(function(data) {
					restService.joinEvent(pitchid, eventid).then(function(data) {
						updateEvent(pitchid, eventid);
						updateInscriptions(pitchid, eventid);
					}).catch(function(error) {
						if (error.status === 403) {
							if (error.data.error === 'UserBusy') {
								$scope.userBusyError = true;
							} else if (error.data.error === 'EventFull') {
								$scope.eventFullError = true;
							} else if (error.data.error === 'InscriptionClosed') {
								$scope.inscriptionClosedError = true;
								$scope.inscriptionHasEnded = true;
							} else if (error.data.error === 'AlreadyJoined') {
								$scope.alreadyJoinedError = true;
							}
						}
					});
				});
			}
		};

		$scope.cancelEvent = function(pitchid, eventid) {
			$scope.showDeleteConfirmModal().result.then(function(data) {
				restService.cancelEvent(pitchid, eventid)
					.then(function(data) {
						$location.url('events');
					})
					.catch(function(error) {
						if (error.status === 403) {
							if (error.data.error === 'InscriptionClosed') {
								$scope.inscriptionClosedError = true;
								$scope.inscriptionHasEnded = true;
							}
						}
					});
			});
			
		};

		function updateEvent(pitchid, eventid) {
			restService.getEvent(pitchid, eventid).then(function(data) {
				event = Object.assign(event, data);
				updateDates();
			});
		}

		function updateInscriptions(pitchid, eventid) {
			restService.getEventInscriptions(pitchid, eventid).then(function(data) {
				$scope.inscriptions = data.inscriptions;
			});
		}

		function updateOwner() {
			if ($scope.isLoggedIn) {
				$scope.isOwner = event.owner.userid === $scope.loggedUser.userid;
			} else {
				$scope.isOwner = false;
			}
		}

		$scope.deleteEvent = function(eventid) {
			$scope.showDeleteConfirmModal().result.then(function(data) {
				restService.deleteEvent(eventid)
					.then(function(data) {
						$location.url('events');
					}).catch(function(error) {
						if (error.status === 403) {
							if (error.data.error === 'EventStarted') {
								$scope.eventStartedError = true;
								$scope.eventHasStarted = true;
							}
						}
					});
			});
		};

		$scope.upvote = function(pitchid, eventid) {
			restService.upvote(pitchid, eventid).then(function(data) {
				updateEvent(pitchid, eventid);
			});
		};

		$scope.downvote = function(pitchid, eventid) {
			restService.downvote(pitchid, eventid).then(function(data) {
				updateEvent(pitchid, eventid);
			});
		};

		function updateDates() {
			var inscriptionEnd = $filter('date')(new Date(Date.parse(event.inscriptionEnd)), 'MM/dd/yyyy HH:mm:ss', 'GMT-3');
			var eventStart = $filter('date')(new Date(Date.parse(event.startsAt)), 'MM/dd/yyyy HH:mm:ss', 'GMT-3');
			var eventEnd = $filter('date')(new Date(Date.parse(event.endsAt)), 'MM/dd/yyyy HH:mm:ss', 'GMT-3');
			var now = $filter('date')(new Date(), 'MM/dd/yyyy HH:mm:ss', 'GMT-3');
			$scope.inscriptionHasEnded = Date.parse(inscriptionEnd) < Date.parse(now);
			$scope.eventHasStarted = Date.parse(eventStart) < Date.parse(now);
			$scope.eventHasEnded = Date.parse(eventEnd) < Date.parse(now);
		}

  	}]);

});
