'use strict';
define(['frontend', 'services/authService', 'services/storageService', 'services/restService', 'services/modalService'], function(frontend) {

	frontend.controller('IndexCtrl', ['$scope', '$filter', '$location', 'authService', 'storageService', 'restService', 'modalService', function($scope, $filter, $location, authService, storageService, restService, modalService) {
		
		$scope.sidebarElements = [
			{name: 'home', link: '#/home'},
			{name: 'allEvents', link: '#/events'},
			{name: 'all_clubs', link: '#/clubs'},
			{name: 'pitches', link: '#/pitches'},
			{name: 'tournaments', link: '#/tournaments'}
		];

		$scope.isLoggedIn = authService.isLoggedIn();
		$scope.loggedUser = authService.getLoggedUser();
		$scope.isAdmin = authService.isAdmin();

		if ($scope.isLoggedIn && !$scope.isAdmin) {
			$scope.sidebarElements.push({name: 'myEvents', link: '#/my-events'});
			$scope.sidebarElements.push({name: 'history', link: '#/history'});
		}

		$scope.showRegisterModal = modalService.registerModal;
		$scope.showLoginModal = modalService.loginModal;

		if ($scope.isLoggedIn) {
			restService.getUserProfilePicture($scope.loggedUser.userid).then(function(data) {
	    		$scope.profilePicture = 'data:image/png;base64,' + _arrayBufferToBase64(data);
	    	}).catch(function(error) {
	    		$scope.profilePicture = 'images/profile_default.png';
	    	});
    	}

		$scope.logout = function() {
			authService.logout();
		};
		
		$scope.$on('user:updated', function() {
			$scope.isLoggedIn = authService.isLoggedIn();
			$scope.loggedUser = authService.getLoggedUser();
			$scope.isAdmin = authService.isAdmin();
			if ($scope.isLoggedIn && !$scope.isAdmin) {
				$scope.sidebarElements.push({name: 'myEvents', link: '#/my-events'});
				$scope.sidebarElements.push({name: 'history', link: '#/history'});
			} else {
				$scope.sidebarElements = $filter('filter')($scope.sidebarElements, {name: '!myEvents'});
				$scope.sidebarElements = $filter('filter')($scope.sidebarElements, {name: '!history'});
			}
			if ($scope.isLoggedIn) {
				restService.getUserProfilePicture($scope.loggedUser.userid).then(function(data) {
		    		$scope.profilePicture = 'data:image/png;base64,' + _arrayBufferToBase64(data);
		    	}).catch(function(error) {
		    		$scope.profilePicture = 'images/profile_default.png';
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

		$scope.goToHome = function() {
			$location.url('home');
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
