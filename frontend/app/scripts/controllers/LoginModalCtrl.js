'use strict';
define(['frontend', 'services/restService', 'services/authService'], function(frontend) {

	frontend.controller('LoginModalCtrl', ['$scope', '$uibModalInstance', 'restService', 'authService', function($scope, $uibModalInstance, restService, authService) {
    
    	$scope.usernamePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
	    $scope.user = {};

	    $scope.loginSubmit = function() {
			if ($scope.loginForm.$valid) {
				authService.login($scope.user.username, $scope.user.password, $scope.user.rememberMe)
          			.then(function() {
						$uibModalInstance.close(true);
					}).catch(function(error) {
					  	if (error.status === 401) {
					    	$scope.invalidCredentials = true;
						}
        	});
			}
		};

	}]);

});
