'use strict';
define(['frontend', 'services/restService', 'services/storageService'], function(frontend) {

	frontend.factory('authService', ['$http', '$rootScope', 'url', 'restService', 'storageService', function($http, $rootScope, url, restService, storageService) {
		var loggedUser = storageService.getUser();

		return {
			getLoggedUser: function() {
				return loggedUser;
			},
			/*isLoggedIn: function() {
				!!loggedUser
			},*/
			login: function(username, password, rememberMe) {
				var credentials = {login_username: username, login_password: password};
				//var self = this;
				return $http.post(url + '/users/login', Object.keys(credentials).length ? jQuery.param(credentials) : '', {transformRequest: angular.identity, headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
					.then(function(response) {
						storageService.setAuthToken(response.headers('X-AUTH-TOKEN'), rememberMe);
						return $http.get(url + '/user', {headers: {'X-AUTH-TOKEN': storageService.getAccessToken()}}); // OJO!
					})
					.then(function(response) {
						return response.data;
					})
					.then(function(data) {
						storageService.setUser(data, rememberMe);
						/*self.*/loggedUser = data;
						//$rootScope.$broadcast('user:updated');
						return data;
					});
			},
			logout: function() {
				storageService.destroy();
				loggedUser = null;
				//$rootScope.$broadcast('user:updated');
			}
		};
	}]);

});