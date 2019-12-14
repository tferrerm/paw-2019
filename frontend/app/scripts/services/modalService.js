'use strict';
define(['frontend', 'controllers/RegisterModalCtrl', 'controllers/DeleteConfirmModalCtrl', 'controllers/LoginModalCtrl', 'services/restService'], function(frontend) {

	frontend.service('modalService', ['$uibModal', 'restService', function($uibModal, restService) {

		this.registerModal = function() {
			return $uibModal.open({
				templateUrl: 'views/registerModal.html',
				controller: 'RegisterModalCtrl',
				windowClass: 'registerModal',
				size: 'md'
			});
		};

		this.loginModal = function() {
			return $uibModal.open({
				templateUrl: 'views/loginModal.html',
				controller: 'LoginModalCtrl',
				windowClass: 'loginModal',
				size: 'md'
			});
		};

		this.deleteConfirmModal = function() {
			return $uibModal.open({
				templateUrl: 'views/deleteConfirmModal.html',
				controller: 'DeleteConfirmModalCtrl',
				windowClass: 'deleteConfirmModal',
				size: 'md'
			});
		};

	}]);

});