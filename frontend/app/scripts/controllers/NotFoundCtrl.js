'use strict';
define(['frontend', 'services/errorService', 'services/titleService'], function(frontend) {

	frontend.controller('NotFoundCtrl', ['$scope', '$filter', 'errorService', 'titleService', function($scope, $filter, errorService, titleService) {
		$scope.error = errorService.getError();
    	titleService.setDefaultTitle();
		errorService.clear();
	}]);

});