'use strict';
define(['frontend', 'services/restService', 'services/authService'], function(frontend) {

	frontend.controller('RegisterModalCtrl', ['$scope', '$uibModalInstance', 'restService', 'authService', 'Upload', function($scope, $uibModalInstance, restService, authService, Upload) {
    
	    $scope.user = {};

	    $scope.registerSubmit = function(picture) {
			//checkPasswordsMatch();
			if ($scope.registerForm.$valid) {
				//$scope.duplicateEmailError = false;
				//$scope.loggingIn = true;
				Upload.urlToBlob(picture.$ngfBlobUrl).then(function(blob) {
				    restService.register($scope.user, blob).then(function(data) {
							return authService.login($scope.user.username, $scope.user.password, true);
						}).then(function() {
							//$scope.loggingIn = false;
							$uibModalInstance.close(true);
							}).catch(function(error) {
								alert(error.data || ' Error');
							});
				});

			};
		};

	}]);

});
