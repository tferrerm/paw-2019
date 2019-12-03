'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('PitchCtrl', ['$scope', 'restService', 'pitch', function($scope, restService, pitch) {
    
	    $scope.pitch = pitch;

	}]);
});
