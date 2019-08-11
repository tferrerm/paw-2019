'use strict';
define(['frontend'], function(frontend) {

	frontend.controller('IndexCtrl', function($scope) {
		$scope.welcomeText = 'Welcome to your frontend page';
    $scope.sidebarElements = ['Home', 'All events', 'My events', 'History', 'Clubs', 'Pitches', 'Tournaments'];
  });
});
