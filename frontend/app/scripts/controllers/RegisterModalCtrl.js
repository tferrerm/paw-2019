'use strict';
define(['frontend', 'services/restService', 'services/authService'], function(frontend) {

	frontend.controller('RegisterModalCtrl', ['$scope', '$uibModalInstance', 'restService', 'authService', 'Upload', function($scope, $uibModalInstance, restService, authService, Upload) {
    
	    $scope.usernamePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
	    $scope.namePattern = '^[a-zA-Z]+$';
	    $scope.user = {};

	    $scope.registerSubmit = function(picture) {
			if ($scope.passwordsMatch() && $scope.registerForm.$valid) {
				$scope.pictureProcessingError = false;
				$scope.duplicateUsernameError = false;
				Upload.urlToBlob(picture.$ngfBlobUrl).then(function(blob) {
				    restService.register($scope.user, blob)
				    .then(function(data) {
						return authService.login($scope.user.username, $scope.user.password, true);
					}).then(function() {
						$uibModalInstance.close(true);
					}).catch(function(error) {
						if (error.status === 422) {
							if (error.data.constraintViolations == null) {
								/* Service violation */
								if (error.data.error === 'PictureProcessingError') {
									$scope.pictureProcessingError = true;
								}
							}
						} else if (error.status === 409) {
							if (error.data.constraintViolations == null) {
								/* Service violation */
								if (error.data.error === 'UserAlreadyExists') {
									$scope.duplicateUsernameError = true;
								}
							}
						}
					});
				});

			};
		};

		$scope.passwordsMatch = function() {
			return $scope.user.password === $scope.user.repeatPassword;
		};

	}]);

});
