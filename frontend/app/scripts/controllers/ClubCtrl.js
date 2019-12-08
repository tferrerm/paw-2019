'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('ClubCtrl', ['$scope', 'restService', 'club', function($scope, restService, club) {
		var pitchParams = {pageNum: 1};
	    $scope.club = club;
	    $scope.haveRelationship = true;
	    //past_events_count
				//haveRelationship

	    restService.getClubPitches(club.clubid, pitchParams)
		    .then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				//$scope.lastPageNum = data.pageCount;
				//$scope.initialPageIndex = data.initialPageIndex;
				//$scope.pageNum = pitchParams.pageNum;
			});

		restService.getClubComments(club.clubid, commentParams)
		    .then(function(data) {
				$scope.comments = data.comments;
				$scope.commentCount = data.commentCount;
				$scope.commentsLastPageNum = data.pageCount;
				$scope.commentsPageInitIndex = data.commentsPageInitIndex;
				$scope.commentsPageNum = commentParams.pageNum;
			});

		$scope.commentSubmit = function() {
			if ($scope.commentForm.$valid) {
				restService.commentClub(club.clubid, $scope.comment); // broadcast
			}
		};

	}]);
});
