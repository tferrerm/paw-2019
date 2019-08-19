'use strict';
define(['frontend'], function(frontend) {

	frontend.controller('HomeCtrl', function($scope) {
		$scope.loggedUser = {name: "Santiago"};
		$scope.noParticipations = true;
    $scope.scheduleHeaders = [];
    $scope.myEvents = []
	});
});
