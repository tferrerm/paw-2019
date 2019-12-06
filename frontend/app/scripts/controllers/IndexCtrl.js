'use strict';
define(['frontend', 'services/authService', 'services/storageService', 'services/restService'], function(frontend) {

	frontend.controller('IndexCtrl', ['$scope', '$location', 'authService', 'storageService', 'restService', function($scope, $location, authService, storageService, restService) {
		$scope.welcomeText = 'Welcome to your frontend page';
		$scope.sidebarElements = [
			{name: 'Home', link: "#/home"},
			{name: 'All events', link: "#/events"},
			{name: 'My events', link: "#/my-events"},
			{name: 'History', link: "#/history"},
			{name: 'Clubs', link: "#/clubs"},
			{name: 'Pitches', link: "#/pitches"},
			{name: 'Tournaments', link: "#/home"}
		]


		//$scope.isLoggedIn = authService.isLoggedIn();
		$scope.loggedUser = authService.getLoggedUser();
		
		/*$scope.$on('user:updated', function() {
			$scope.isLoggedIn = auth.isLoggedIn();
			$scope.loggedUser = auth.getLoggedUser();
		});*/
	}]);
});
