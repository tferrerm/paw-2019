'use strict';
define(['frontend', 'services/restService', 'services/authService'], function(frontend) {

	frontend.controller('RegisterModalCtrl', ['$scope', '$uibModalInstance', 'restService', 'authService', function($scope, $uibModalInstance, restService, authService) {
    
	    $scope.user = {};

	    $scope.registerSubmit = function(picture) {
			//checkPasswordsMatch();
			if ($scope.registerForm.$valid) {
				//$scope.duplicateEmailError = false;
				//$scope.loggingIn = true;
				var xhr = new XMLHttpRequest();
				xhr.open('GET', picture.$ngfBlobUrl);
				xhr.responseType = 'blob';
				xhr.onload = function(e) {
				if (this.status == 200) {
				    var blobFileData = this.response;
				    
				    restService.register($scope.user, blobFileData)
						.then(function(data) {
							return authService.login($scope.user.username, $scope.user.password, true);
						})
						.then(function() {
							//$scope.loggingIn = false;
							$uibModalInstance.close(true);
						}).catch(function(error) {
							alert(error.data || ' Error');
						});
				  	}
				};
				xhr.send();

			}
		};

	}]);

});
