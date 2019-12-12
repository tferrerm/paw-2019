'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('PitchesCtrl', ['$scope', '$location', 'restService', function($scope, $location, restService) {
    	var params = {pageNum: 1};
	    $scope.filters = {};

	    restService.getPitches(params).then(function(data) {
			$scope.pitches = data.pitches;
			$scope.pitchCount = data.pitchCount;
			$scope.lastPageNum = data.pageCount;
			$scope.initialPageIndex = data.initialPageIndex;
			$scope.pageNum = params.pageNum;
		}).catch(function(error) {
alert(error.data || ' Error');
});

		restService.getSports().then(function(data) {
			$scope.sports = data.sports;
		}).catch(function(error) {
alert(error.data || ' Error');
});

		$scope.goToPitch = function(id) {
			$location.url('pitches/' + id);
		};

		$scope.getFirstPage = function() {
			params.pageNum = 1;
			restService.getPitches(params).then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			}).catch(function(error) {
alert(error.data || ' Error');
});
		};

		$scope.getPrevPage = function() {
			params.pageNum--;
			restService.getPitches(params).then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			}).catch(function(error) {
alert(error.data || ' Error');
});
		};

		$scope.getNextPage = function() {
			params.pageNum++;
			restService.getPitches(params).then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			}).catch(function(error) {
alert(error.data || ' Error');
});
		};

		$scope.getLastPage = function() {
			params.pageNum = $scope.lastPageNum;
			restService.getPitches(params).then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			}).catch(function(error) {
alert(error.data || ' Error');
});
		};

		$scope.filterPitches = function() {
			params = $scope.filters;
			params.pageNum = 1;
			restService.getPitches(params).then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			}).catch(function(error) {
alert(error.data || ' Error');
});
		};

	}]);
});
