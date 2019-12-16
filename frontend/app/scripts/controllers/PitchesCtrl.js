'use strict';
define(['frontend', 'services/restService', 'services/titleService'], function(frontend) {

	frontend.controller('PitchesCtrl', ['$scope', '$location', '$filter', 'restService', 'titleService', function($scope, $location, $filter, restService, titleService) {
    	var params = {pageNum: 1};
	    $scope.filters = {};

	    titleService.setTitle($filter('translate')('pitches'));

	    updatePitches(params);

		restService.getSports().then(function(data) {
			$scope.sports = data.sports;
		}).catch(function(error) {
			$scope.sports = [];
		});

		$scope.goToPitch = function(id) {
			$location.url('pitches/' + id);
		};

		$scope.getFirstPage = function() {
			params.pageNum = 1;
			updatePitches(params);
		};

		$scope.getPrevPage = function() {
			params.pageNum--;
			updatePitches(params);
		};

		$scope.getNextPage = function() {
			params.pageNum++;
			updatePitches(params);
		};

		$scope.getLastPage = function() {
			params.pageNum = $scope.lastPageNum;
			updatePitches(params);
		};

		$scope.filterPitches = function() {
			params = $scope.filters;
			params.pageNum = 1;
			updatePitches(params);
		};

		function updatePitches(params) {
			restService.getPitches(params).then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		}

	}]);
});
