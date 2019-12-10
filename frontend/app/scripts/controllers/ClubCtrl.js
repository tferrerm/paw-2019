'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('ClubCtrl', ['$scope', '$location', 'restService', 'club', function($scope, $location, restService, club) {
		var pitchParams = {pageNum: 1};
		var commentParams = {pageNum: 1};
	    $scope.club = club;
	    
	    restService.hasRelationshipWithClub($scope.club.clubid)
	    	.then(function(data) {
	    		$scope.hasRelationship = data.relationship;
	    	}).catch((error) => alert(error.data || "Error"));

	    restService.getClubPitches(club.clubid, pitchParams)
		    .then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = pitchParams.pageNum;
			}).catch((error) => alert(error.data || "Error"));

		restService.getClubComments(club.clubid, commentParams)
		    .then(function(data) {
				$scope.comments = data.comments;
				$scope.commentCount = data.commentCount;
				$scope.commentsLastPageNum = data.pageCount;
				$scope.commentsPageInitIndex = data.commentsPageInitIndex;
				$scope.commentsPageNum = commentParams.pageNum;
			}).catch((error) => alert(error.data || "Error"));

		$scope.$on('user:updated', function() {
			$scope.hasRelationship = restService.hasRelationshipWithClub($scope.club.clubid);
		});

		$scope.getFirstPage = function() {
			pitchParams.pageNum = 1;
			restService.getClubPitches(club.clubid, pitchParams).then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = pitchParams.pageNum;
			}).catch((error) => alert(error.data || "Error"));
		};

		$scope.getPrevPage = function() {
			pitchParams.pageNum--;
			restService.getClubPitches(club.clubid, pitchParams).then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = pitchParams.pageNum;
			}).catch((error) => alert(error.data || "Error"));
		};

		$scope.getNextPage = function() {
			pitchParams.pageNum++;
			restService.getClubPitches(club.clubid, pitchParams).then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = pitchParams.pageNum;
			}).catch((error) => alert(error.data || "Error"));
		};

		$scope.getLastPage = function() {
			pitchParams.pageNum = $scope.lastPageNum;
			restService.getClubPitches(club.clubid, pitchParams).then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = pitchParams.pageNum;
			}).catch((error) => alert(error.data || "Error"));
		};

		$scope.commentText = {};

		$scope.commentSubmit = function() {
			//if ($scope.commentForm.$valid) {
				restService.commentClub(club.clubid, $scope.commentText.comment).then(function(data) {
					commentParams.pageNum = 1;
					restService.getClubComments(club.clubid, commentParams)
					    .then(function(data) {
							$scope.comments = data.comments;
							$scope.commentCount = data.commentCount;
							$scope.commentsLastPageNum = data.pageCount;
							$scope.commentsPageInitIndex = data.commentsPageInitIndex;
							$scope.commentsPageNum = commentParams.pageNum;
						});
				}).catch((error) => alert(error.data || "Error"));
			//}
		};

		$scope.goToPitch = function(id) {
			$location.url('pitches/' + id);
		};

		$scope.goToProfile = function(id) {
			$location.url('users/' + id);
		}

		$scope.getCommentsFirstPage = function() {
			commentParams.pageNum = 1;
			restService.getClubComments(club.clubid, commentParams)
			    .then(function(data) {
					$scope.comments = data.comments;
					$scope.commentCount = data.commentCount;
					$scope.commentsLastPageNum = data.pageCount;
					$scope.commentsPageInitIndex = data.commentsPageInitIndex;
					$scope.commentsPageNum = commentParams.pageNum;
				});
		};

		$scope.getCommentsPrevPage = function() {
			commentParams.pageNum--;
			restService.getClubComments(club.clubid, commentParams)
			    .then(function(data) {
					$scope.comments = data.comments;
					$scope.commentCount = data.commentCount;
					$scope.commentsLastPageNum = data.pageCount;
					$scope.commentsPageInitIndex = data.commentsPageInitIndex;
					$scope.commentsPageNum = commentParams.pageNum;
				});
		};

		$scope.getCommentsNextPage = function() {
			commentParams.pageNum++;
			restService.getClubComments(club.clubid, commentParams)
			    .then(function(data) {
					$scope.comments = data.comments;
					$scope.commentCount = data.commentCount;
					$scope.commentsLastPageNum = data.pageCount;
					$scope.commentsPageInitIndex = data.commentsPageInitIndex;
					$scope.commentsPageNum = commentParams.pageNum;
				});
		};

		$scope.getCommentsLastPage = function() {
			commentParams.pageNum = $scope.commentsLastPageNum;
			restService.getClubComments(club.clubid, commentParams)
			    .then(function(data) {
					$scope.comments = data.comments;
					$scope.commentCount = data.commentCount;
					$scope.commentsLastPageNum = data.pageCount;
					$scope.commentsPageInitIndex = data.commentsPageInitIndex;
					$scope.commentsPageNum = commentParams.pageNum;
				});
		};

	}]);
});
