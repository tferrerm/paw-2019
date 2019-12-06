'use strict';
define(['frontend', 'controllers/RegisterModalCtrl', 'controllers/LoginModalCtrl', 'services/restService'], function(frontend) {

	frontend.service('modalService', ['$uibModal', 'restService', function($uibModal, restService) {

		this.registerModal = function() {
			return $uibModal.open({
				templateUrl: 'views/registerModal.html',
				controller: 'RegisterModalCtrl',
				size: 'md'
			});
		};

		this.loginModal = function() {
			return $uibModal.open({
				templateUrl: 'views/loginModal.html',
				controller: 'LoginModalCtrl',
				size: 'md'
			});
		};

	}]);

});