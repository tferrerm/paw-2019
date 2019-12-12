'use strict';
define(['frontend', 'services/authService', 'services/storageService', 'services/restService', 'services/modalService'], function(frontend) {

	frontend.controller('IndexCtrl', ['$scope', '$location', 'authService', 'storageService', 'restService', 'modalService', function($scope, $location, authService, storageService, restService, modalService) {
		$scope.welcomeText = 'Welcome to your frontend page'; // ELIMINAR
		$scope.sidebarElements = [
			{name: 'Home', link: '#/home'},
			{name: 'All events', link: '#/events'},
			{name: 'My events', link: '#/my-events'},
			{name: 'History', link: '#/history'},
			{name: 'Clubs', link: '#/clubs'},
			{name: 'Pitches', link: '#/pitches'},
			{name: 'Tournaments', link: '#/tournaments'}
		];

		$scope.isLoggedIn = authService.isLoggedIn();
		$scope.loggedUser = authService.getLoggedUser();
		$scope.isAdmin = authService.isAdmin();

		$scope.showRegisterModal = modalService.registerModal;
		$scope.showLoginModal = modalService.loginModal;

		if ($scope.isLoggedIn) {
			restService.getUserProfilePicture($scope.loggedUser.userid).then(function(data) {
	    		$scope.profilePicture = 'data:image/png;base64,' + _arrayBufferToBase64(data);
	    	}).catch(function(error) {
	    		$scope.profilePicture = '../../images/profile_default.png';
	    	});
    	}

		$scope.logout = function() {
			authService.logout();
		};
		
		$scope.$on('user:updated', function() {
			$scope.isLoggedIn = authService.isLoggedIn();
			$scope.loggedUser = authService.getLoggedUser();
			$scope.isAdmin = authService.isAdmin();
			if ($scope.isLoggedIn) {
				restService.getUserProfilePicture($scope.loggedUser.userid).then(function(data) {
		    		$scope.profilePicture = 'data:image/png;base64,' + _arrayBufferToBase64(data);
		    	}).catch(function(error) {
		    		$scope.profilePicture = '../../images/profile_default.png';
		    	});
	    	}
		});

		$scope.goToEvent = function(pitchid, eventid) {
			$location.url('pitches/' + pitchid + '/events/' + eventid);
		};

		$scope.goToClub = function(id) {
			$location.url('clubs/' + id);
		};

		$scope.goToPitch = function(id) {
			$location.url('pitches/' + id);
		};

		$scope.goToProfile = function(id) {
			$location.url('users/' + id);
		};

		function _arrayBufferToBase64(buffer) {
		    var binary = '';
		    var bytes = new Uint8Array(buffer);
		    var len = bytes.byteLength;
		    for (var i = 0; i < len; i++) {
		      binary += String.fromCharCode(bytes[i]);
		    }
		    return window.btoa(binary);
		}

	}]);
});
