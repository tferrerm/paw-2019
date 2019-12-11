'use strict';
define(['frontend', 'services/restService', 'services/authService'], function(frontend) {

	frontend.controller('ProfileCtrl', ['$scope', '$location', 'restService', 'authService', 'user', function($scope, $location, restService, authService, user) {
		var commentParams = {pageNum: 1};
		$scope.user = user;
		$scope.isLoggedIn = authService.isLoggedIn();
	    if($scope.isLoggedIn) {
	    	$scope.loggedUser = authService.getLoggedUser();
	    	$scope.isLoggedUser = $scope.loggedUser.userid == user.userid;
	    } else {
	    	$scope.isLoggedUser = false;
	    }

	    $scope.getUserPicture = function(userid) {
	    	return restService.getUserProfilePicture(userid);
	    }

	    

	    restService.hasRelationshipWithUser(user.userid)
	    	.then(function(data) {
	    		$scope.hasRelationship = data.relationship;
	    	});

	    $scope.goToProfile = function(id) {
			$location.url('users/' + id);
		}

		$scope.$on('user:updated', function() {
			$scope.isLoggedIn = authService.isLoggedIn();
			if($scope.isLoggedIn) {
		    	$scope.loggedUser = authService.getLoggedUser();
		    	$scope.isLoggedUser = $scope.loggedUser.userid == user.userid;
		    } else {
		    	$scope.isLoggedUser = false;
		    }
		});

		restService.getUserComments(user.userid, commentParams)
		    .then(function(data) {
				$scope.comments = data.comments;
				$scope.commentCount = data.commentCount;
				$scope.commentsLastPageNum = data.pageCount;
				$scope.commentsPageInitIndex = data.commentsPageInitIndex;
				$scope.commentsPageNum = commentParams.pageNum;
			});

		$scope.getFirstPage = function() {
			commentParams.pageNum = 1;
			restService.getUserComments(user.userid, commentParams)
			    .then(function(data) {
					$scope.comments = data.comments;
					$scope.commentCount = data.commentCount;
					$scope.commentsLastPageNum = data.pageCount;
					$scope.commentsPageInitIndex = data.commentsPageInitIndex;
					$scope.commentsPageNum = commentParams.pageNum;
				});
		};

		$scope.getPrevPage = function() {
			commentParams.pageNum--;
			restService.getUserComments(user.userid, commentParams)
			    .then(function(data) {
					$scope.comments = data.comments;
					$scope.commentCount = data.commentCount;
					$scope.commentsLastPageNum = data.pageCount;
					$scope.commentsPageInitIndex = data.commentsPageInitIndex;
					$scope.commentsPageNum = commentParams.pageNum;
				});
		};

		$scope.getNextPage = function() {
			commentParams.pageNum++;
			restService.getUserComments(user.userid, commentParams)
			    .then(function(data) {
					$scope.comments = data.comments;
					$scope.commentCount = data.commentCount;
					$scope.commentsLastPageNum = data.pageCount;
					$scope.commentsPageInitIndex = data.commentsPageInitIndex;
					$scope.commentsPageNum = commentParams.pageNum;
				});
		};

		$scope.getLastPage = function() {
			commentParams.pageNum = $scope.commentsLastPageNum;
			restService.getUserComments(user.userid, commentParams)
			    .then(function(data) {
					$scope.comments = data.comments;
					$scope.commentCount = data.commentCount;
					$scope.commentsLastPageNum = data.pageCount;
					$scope.commentsPageInitIndex = data.commentsPageInitIndex;
					$scope.commentsPageNum = commentParams.pageNum;
				});
		};

		$scope.commentText = {};

		$scope.commentSubmit = function() {
			//if ($scope.commentForm.$valid) {
				restService.commentUser(user.userid, $scope.commentText.comment).then(function(data) {
					commentParams.pageNum = 1;
					restService.getUserComments(user.userid, commentParams)
						.then(function(data) {
							$scope.comments = data.comments;
							$scope.commentCount = data.commentCount;
							$scope.commentsLastPageNum = data.pageCount;
							$scope.commentsPageInitIndex = data.commentsPageInitIndex;
							$scope.commentsPageNum = commentParams.pageNum;
						});
				});
			//}
		};

	}]);
});
