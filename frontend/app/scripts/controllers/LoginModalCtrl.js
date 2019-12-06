'use strict';
define(['frontend', 'services/restService', 'services/authService'], function(frontend) {

	frontend.controller('LoginModalCtrl', ['$scope', 'restService', 'authService', function($scope, restService, authService) {
    
	    $scope.user = {};

	    $scope.loginSubmit = function() {
			//checkPasswordsMatch();
			if ($scope.loginForm.$valid) {
				//$scope.duplicateEmailError = false;
				//$scope.loggingIn = true;

				authService.login($scope.user.username, $scope.user.password, true)
					.then(function() {
						//$scope.loggingIn = false;
						$uibModalInstance.close(true);
					});
			}
		};

	}]);

});
