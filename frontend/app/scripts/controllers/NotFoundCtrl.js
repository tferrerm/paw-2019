'use strict';
define(['frontend', 'services/errorService'], function(frontend) {

	frontend.controller('NotFoundCtrl', ['$scope', 'errorService', function($scope, errorService) {
		//$translate('error.title').then(function(title) {
		//	titleService.setTitle(title);
		//});
		$scope.error = errorService.getError();
		errorService.clear();
	}]);

});