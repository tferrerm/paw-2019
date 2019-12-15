'use strict';
define(['frontend', 'services/errorService', 'services/titleService'], function(frontend) {

	frontend.controller('NotFoundCtrl', ['$scope', '$filter', 'errorService', 'titleService', function($scope, $filter, errorService, titleService) {
		//$translate('error.title').then(function(title) {
		//	titleService.setTitle(title);
		//});
		$scope.error = errorService.getError();
    titleService.setTitle($filter('translate')('not_found'));
		errorService.clear();
	}]);

});