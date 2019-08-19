'use strict';
define(['frontend'], function(frontend) {

	frontend.controller('IndexCtrl', function($scope) {
		$scope.welcomeText = 'Welcome to your frontend page';
    $scope.sidebarElements = [
      {name: 'Home', link: "#/home"},
      {name: 'All events', link: "#/events"},
      {name: 'My events', link: "#/home"},
      {name: 'History', link: "#/home"},
      {name: 'Clubs', link: "#/home"},
      {name: 'Pitches', link: "#/home"},
      {name: 'Tournaments', link: "#/home"}
    ]
  });
});
