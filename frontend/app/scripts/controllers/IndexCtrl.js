'use strict';
define(['frontend'], function(frontend) {

	frontend.controller('IndexCtrl', function($scope) {
		$scope.welcomeText = 'Welcome to your frontend page';
    $scope.sidebarElements = [
      {name: 'Home', link: "#/home"},
      {name: 'All events', link: "#/events"},
      {name: 'My events', link: "#/my-events"},
      {name: 'History', link: "#/history"},
      {name: 'Clubs', link: "#/home"},
      {name: 'Pitches', link: "#/home"},
      {name: 'Tournaments', link: "#/home"}
    ]
  });
});
