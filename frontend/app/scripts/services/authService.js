'use strict';
define(['frontend', 'services/restService', 'services/storageService'], function(frontend) {

	frontend.factory('authService', ['$http', '$rootScope', 'url', 'restService', 'storageService', function($http, $rootScope, url, restService, storageService) {
		var loggedUser = storageService.getUser();

		return {
			getLoggedUser: function() {
				return loggedUser;
			},
			isAdmin: function() {
				return this.isLoggedIn() && loggedUser.role === 'admin';
			},
			isLoggedIn: function() {
				return !!loggedUser;
			},
			login: function(username, password, rememberMe) {
				var credentials = {login_username: username, login_password: password};

				return $http.post(url + '/users/login', Object.keys(credentials).length ? jQuery.param(credentials) : '', {transformRequest: angular.identity, headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
					.then(function(response) {
						storageService.setAuthToken(response.headers('X-Auth-Token'), rememberMe);
						return $http.get(url + '/users/profile', {headers: {'X-Auth-Token': storageService.getAuthToken()}});
					})
					.then(function(response) {
						return response.data;
					})
					.then(function(data) {
						storageService.setUser(data, rememberMe);
						loggedUser = data;
						$rootScope.$broadcast('user:updated');
						return data;
					});
			},
			logout: function() {
				storageService.destroy();
				loggedUser = null;
				$rootScope.$broadcast('user:updated');
			}
		};
	}]);

});
