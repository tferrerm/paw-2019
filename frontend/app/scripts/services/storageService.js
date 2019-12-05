'use strict';
define(['frontend'], function(frontend) {

	frontend.factory('storageService', ['$window', function($window) {
		var user = JSON.parse($window.localStorage.getItem('session.user')) || JSON.parse($window.sessionStorage.getItem('session.user'));
		var authToken = JSON.parse($window.localStorage.getItem('session.authToken'))
			|| JSON.parse($window.sessionStorage.getItem('session.authToken'));

		return {
			getUser: function() {
				return user;
			},
			setUser: function(usr, rememberMe) {
				user = usr;
				if(rememberMe)
					$window.localStorage.setItem('session.user', JSON.stringify(usr));
				else
					$window.sessionStorage.setItem('session.user', JSON.stringify(usr));
			},
			getAuthToken: function() {
				return authToken;
			},
			setAuthToken: function(token, rememberMe) {
				authToken = token;
				if(rememberMe)
					$window.localStorage.setItem('session.authToken', JSON.stringify(token));
				else
					$window.sessionStorage.setItem('session.authToken', JSON.stringify(token));
			},
			destroy: function() {
				setUser(null, false);
				setUser(null, true);
				setAuthToken(null, false);
				setAuthToken(null, true);
			}
		};
	}]);
	
});
