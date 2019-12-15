'use strict';
define(['frontend', 'services/restService', 'services/authService', 'services/modalService'], function(frontend) {

	frontend.controller('LoginModalCtrl', ['$scope', '$uibModalInstance', 'restService', 'authService', 'modalService', function($scope, $uibModalInstance, restService, authService, modalService) {
    
    	$scope.usernamePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
	    $scope.user = {};
	    
	    $scope.showRegisterModal = function() {
	    	$uibModalInstance.close(true);
	    	modalService.registerModal();
	    };

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
