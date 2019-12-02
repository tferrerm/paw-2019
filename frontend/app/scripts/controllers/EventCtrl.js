'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('EventCtrl', ['$scope', 'restService', 'event', function($scope, restService, event) {
		
		$scope.event = event;
		$scope.has_ended = false;

  	}]);

});
