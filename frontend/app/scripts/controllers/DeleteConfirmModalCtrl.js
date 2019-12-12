'use strict';
define(['frontend'], function(frontend) {

	frontend.controller('DeleteConfirmModalCtrl', ['$scope', '$uibModalInstance', function($scope, $uibModalInstance) {
    
	    $scope.confirmDelete = function() {
			$uibModalInstance.close(true);
		};

		$scope.cancelDelete = function() {
			$uibModalInstance.dismiss('cancel');
		};

	}]);

});
