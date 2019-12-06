'use strict';
define(['frontend', 'services/restService', 'services/authService'], function(frontend) {

	frontend.controller('HomeCtrl', ['$scope', '$location', 'restService', 'authService', function($scope, $location, restService, authService) {
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

	}]);
});
