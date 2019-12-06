'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('PitchCtrl', ['$scope', '$filter', 'restService', 'pitch', function($scope, $filter, restService, pitch) {
    
	    $scope.pitch = pitch;
	    $scope.minHour = 9; // PEDIR DE BACK
	    $scope.maxHour = 23; // PEDIR DE BACK
	    $scope.schedule = Array.apply(null, Array($scope.maxHour - $scope.minHour));
	    for(var i = 0; i < ($scope.maxHour - $scope.minHour); i++) {
	    	$scope.schedule[i] = Array.apply(null, Array(7));
	    }
	    
	    restService.getPitchWeekEvents(pitch.pitchid, {}).then(function(data) {
	    	$scope.weekEvents = data.events;
	    	angular.forEach($scope.weekEvents, function(event, index) {
	    		var startsDate = new Date(Date.parse(event.startsAt));
	    		var endsDate = new Date(Date.parse(event.endsAt));
	    		/* Sunday = 0 --> Sunday = 6 */
	    		var todayDayIndex = (((new Date()).getUTCDay() - 1) + 7) % 7;
	    		var eventDayIndex = (((((startsDate.getUTCDay() - 1) + 7) % 7) - todayDayIndex) + 7) % 7;
	    		var startHourIndex = startsDate.getUTCHours() - $scope.minHour;
	    		var endHourIndex = endsDate.getUTCHours() - $scope.minHour - 1;
	    		
	    		for(var i = startHourIndex; i <= endHourIndex; i++) {
	    			$scope.schedule[i][eventDayIndex] = {name: event.name, inscriptionCount: event.inscriptionCount};
	    		}
	    	});
	    	
	    });

	}]);
});
