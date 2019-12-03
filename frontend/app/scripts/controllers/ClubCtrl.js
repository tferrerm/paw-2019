'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('ClubCtrl', ['$scope', 'restService', 'club', function($scope, restService, club) {

	    $scope.club = club;

	}]);
});
