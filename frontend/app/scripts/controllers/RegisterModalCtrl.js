'use strict';
define(['frontend', 'services/restService', 'services/authService'], function(frontend) {

	frontend.controller('RegisterModalCtrl', ['$scope', '$uibModalInstance', 'restService', 'authService', function($scope, uibModalInstance, restService, authService) {
    
	    $scope.user = {};

	    $scope.registerSubmit = function() {
			//checkPasswordsMatch();
			if ($scope.registerForm.$valid) {
				//$scope.duplicateEmailError = false;
				//$scope.loggingIn = true;

				restService.register($scope.user)
					.then(function(data) {
						return authService.login($scope.user.username, $scope.user.password, true);
					})
					.then(function() {
						//$scope.loggingIn = false;
						$uibModalInstance.close(true);
					});
			}
		};

		$scope.uploadFile = function() {
		    var f = $scope.user.picture,
		        r = new FileReader();

		    r.onloadend = function(e) {
		      var data = e.target.result;
		      //send your binary data via $http or $resource or do anything else with it
		    }

		    r.readAsBinaryString(f);
		}

	}]);

});
