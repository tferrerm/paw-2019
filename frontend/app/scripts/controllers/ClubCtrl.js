'use strict';
define(['frontend', 'services/restService'], function(frontend) {

	frontend.controller('ClubCtrl', ['$scope', '$location', 'restService', 'club', function($scope, $location, restService, club) {
		var pitchParams = {pageNum: 1};
		var commentParams = {pageNum: 1};
	    $scope.club = club;

	    if($scope.isAdmin) {
			$scope.createdPitch = {};
			restService.getSports().then(function(data) {
				$scope.sports = data.sports;
			});
		}
	    
	    restService.hasRelationshipWithClub(club.clubid)
	    	.then(function(data) {
	    		$scope.hasRelationship = data.relationship;
	    	});

	    updatePitches(club.clubid, pitchParams);

		updateComments(club.clubid, commentParams);

		$scope.$on('user:updated', function() {
			restService.hasRelationshipWithClub(club.clubid)
		    	.then(function(data) {
		    		$scope.hasRelationship = data.relationship;
		    	});
		});

		$scope.getFirstPage = function() {
			pitchParams.pageNum = 1;
			updatePitches(club.clubid, pitchParams);
		};

		$scope.getPrevPage = function() {
			pitchParams.pageNum--;
			updatePitches(club.clubid, pitchParams);
		};

		$scope.getNextPage = function() {
			pitchParams.pageNum++;
			updatePitches(club.clubid, pitchParams);
		};

		$scope.getLastPage = function() {
			pitchParams.pageNum = $scope.lastPageNum;
			updatePitches(club.clubid, pitchParams);
		};

		function updatePitches(clubid, params) {
			restService.getClubPitches(clubid, params).then(function(data) {
				$scope.pitches = data.pitches;
				$scope.pitchCount = data.pitchCount;
				$scope.lastPageNum = data.pageCount;
				$scope.initialPageIndex = data.initialPageIndex;
				$scope.pageNum = params.pageNum;
			});
		};

		$scope.commentText = {};

		$scope.commentSubmit = function() {
			//if ($scope.commentForm.$valid) {
				restService.commentClub(club.clubid, $scope.commentText.comment).then(function(data) {
					commentParams.pageNum = 1;
					updateComments(club.clubid, commentParams);
				});
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
			updateComments(club.clubid, commentParams);
		};

		$scope.getCommentsPrevPage = function() {
			commentParams.pageNum--;
			updateComments(club.clubid, commentParams);
		};

		$scope.getCommentsNextPage = function() {
			commentParams.pageNum++;
			updateComments(club.clubid, commentParams);
		};

		$scope.getCommentsLastPage = function() {
			commentParams.pageNum = $scope.commentsLastPageNum;
			updateComments(club.clubid, commentParams);
		};

		function updateComments(clubid, params) {
			restService.getClubComments(clubid, params).then(function(data) {
				$scope.comments = data.comments;
				$scope.commentCount = data.commentCount;
				$scope.commentsLastPageNum = data.pageCount;
				$scope.commentsPageInitIndex = data.commentsPageInitIndex;
				$scope.commentsPageNum = params.pageNum;
			});
		};

		$scope.createPitchSubmit = function() {
			//checkPasswordsMatch();
			//if ($scope.createEventForm.$valid) {
				//$scope.duplicateEmailError = false;
				
				if($scope.isAdmin) {
					restService.createPitch(club.clubid, $scope.createdPitch)
						.then(function(data) {
							//var createdEvent = data.event;
							$location.url('pitches/' + data.pitchid);
						});

				}
			//}
		};

		$scope.newTournament = function(id) {
			$location.url('admin/clubs/' + id + '/tournaments/new');
		};

		$scope.deleteClub = function(id) {
			// DELETE CLUB
		};

	}]);
});
