'use strict';
define(['frontend', 'services/restService', 'services/modalService'], function(frontend) {

	frontend.controller('ClubCtrl', ['$scope', '$location', 'restService', 'modalService', 'club', 'Upload', function($scope, $location, restService, modalService, club, Upload) {
		var pitchParams = {pageNum: 1};
		var commentParams = {pageNum: 1};
	    $scope.club = club;

	    if ($scope.isAdmin) {
			$scope.createdPitch = {};
			restService.getSports().then(function(data) {
				$scope.sports = data.sports;
			});
			$scope.showDeleteConfirmModal = modalService.deleteConfirmModal;
		}
	    
	    restService.hasRelationshipWithClub(club.clubid)
	    	.then(function(data) {
	    		$scope.hasRelationship = data.relationship;
	    	}).catch(function(error) {
				alert(error.data || ' Error');
			});

	    updatePitches(club.clubid, pitchParams);

		updateComments(club.clubid, commentParams);

		$scope.$on('user:updated', function() {
			restService.hasRelationshipWithClub(club.clubid)
		    	.then(function(data) {
		    		$scope.hasRelationship = data.relationship;
		    	});
		    if ($scope.isAdmin) {
				$scope.createdPitch = {};
				restService.getSports().then(function(data) {
					$scope.sports = data.sports;
					$scope.createdPitch.sport = $scope.sports[0];
				});
			}
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
				$scope.pageNum = pitchParams.pageNum;
			}).catch(function(error) {
				alert(error.data || ' Error');
			});
		};

		$scope.commentText = {};

		$scope.commentSubmit = function() {
			if ($scope.commentForm.$valid) {
				restService.commentClub(club.clubid, $scope.commentText.comment).then(function(data) {
					commentParams.pageNum = 1;
					updateComments(club.clubid, commentParams);
				});
			}
		};

		$scope.goToPitch = function(id) {
			$location.url('pitches/' + id);
		};

		$scope.goToProfile = function(id) {
			$location.url('users/' + id);
		};

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
		
		$scope.createPitchSubmit = function(picture) {
			if ($scope.createPitchForm.$valid) {
				if ($scope.isAdmin) {
					// CHEQUEAR SI picture es undefined (si subieron imagen mala)
					Upload.urlToBlob(picture.$ngfBlobUrl).then(function(blob) {
						restService.createPitch(club.clubid, $scope.createdPitch, blob)
							.then(function(data) {
								pitchParams.pageNum = 1;
								updatePitches(club.clubid, pitchParams);
							});
					});
				};
			}
		};

		$scope.newTournament = function(id) {
			$location.url('admin/clubs/' + id + '/tournaments/new');
		};

		$scope.deletePitch = function(clubid, pitchid) {
			$scope.showDeleteConfirmModal().result.then(function(data) {
				restService.deletePitch(clubid, pitchid)
				.then(function(data) {
					pitchParams.pageNum = 1;
					updatePitches(club.clubid, pitchParams);
				});
			});
		};

		$scope.deleteClub = function(id) {
			$scope.showDeleteConfirmModal().result.then(function(data) {
				restService.deleteClub(id)
				.then(function(data) {
					$location.url('clubs');
				});
			});
		};

	}]);
});
