'use strict';
define(['frontend', 'services/restService', 'services/authService', 'services/modalService'], function(frontend) {

	frontend.controller('HomeCtrl', ['$scope', '$location', 'restService', 'authService', 'modalService', function($scope, $location, restService, authService, modalService) {
		//$scope.loggedUser = {name: "Santiago"};
		$scope.noParticipations = false;
    	$scope.scheduleHeaders = [];
    	$scope.myEvents = []

    	/*restService.getUpcomingEvents(2).then(function(data) {
			$scope.schedule = data.schedule;
		});*/

		/*$scope.$on('user:updated', function() {
			restService.isLoggedIn().then(function(data) {
	        	$scope.isLoggedIn = data.isLoggedIn;
	        	if ($scope.isLoggedIn) {
					restService.getLoggedUser().then(function(data) {
			        	$scope.loggedUser = data;
			        });
					getLoggedUserSubsPageCount();
				} else {
					$scope.loggedUser = null;
				}
	        });
		});*/

    	$scope.loginUser = {};
		$scope.login = function() {
			authService.login($scope.loginUser.username, $scope.loginUser.password, false/*$scope.loginUser.rememberMe*/);
		}

		$scope.registerModal = modalService.registerModal;

	}]);
});
