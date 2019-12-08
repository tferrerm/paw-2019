'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('ClubCtrl', ['$scope', '$location', 'restService', 'club', function($scope, $location, restService, club) {
		var pitchParams = {pageNum: 1};
		var commentParams = {pageNum: 1};
	    $scope.club = club;
	    $scope.haveRelationship = true;
	    //past_events_count
		//haveRelationship

	    restService.getClubPitches(club.clubid, pitchParams)
		    .then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = pitchParams.pageNum;
			});

		restService.getClubComments(club.clubid, commentParams)
		    .then(function(data) {
				$scope.comments = data.comments;
				$scope.commentCount = data.commentCount;
				$scope.commentsLastPageNum = data.pageCount;
				$scope.commentsPageInitIndex = data.commentsPageInitIndex;
				$scope.commentsPageNum = commentParams.pageNum;
			});

		$scope.getFirstPage = function() {
			pitchParams.pageNum = 1;
			restService.getClubPitches(club.clubid, pitchParams).then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = pitchParams.pageNum;
			});
		};

		$scope.getPrevPage = function() {
			pitchParams.pageNum--;
			restService.getClubPitches(club.clubid, pitchParams).then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = pitchParams.pageNum;
			});
		};

		$scope.getNextPage = function() {
			pitchParams.pageNum++;
			restService.getClubPitches(club.clubid, pitchParams).then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = pitchParams.pageNum;
			});
		};

		$scope.getLastPage = function() {
			pitchParams.pageNum = $scope.lastPageNum;
			restService.getClubPitches(club.clubid, pitchParams).then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = pitchParams.pageNum;
			});
		};

		$scope.commentSubmit = function() {
			if ($scope.commentForm.$valid) {
				restService.commentClub(club.clubid, $scope.comment); // broadcast
			}
		};

		$scope.goToPitch = function(id) {
			$location.url('pitches/' + id);
		};

	}]);
});
