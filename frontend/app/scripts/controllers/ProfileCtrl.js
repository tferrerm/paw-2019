'use strict';
define(['frontend', 'services/restService', 'services/authService', 'services/titleService'], function(frontend) {

	frontend.controller('ProfileCtrl', ['$http', 'url', '$scope', '$location', 'restService', 'authService', 'titleService', 'user', function($http, url, $scope, $location, restService, authService, titleService, user) {
		var commentParams = {pageNum: 1};
		$scope.user = user;
		titleService.setTitle(user.firstname + ' ' + user.lastname);
	    if ($scope.isLoggedIn) {
	    	$scope.isLoggedUser = $scope.loggedUser.userid === user.userid;
	    	if (!$scope.isLoggedUser) {
		    	$scope.picture = url + '/users/' + user.userid + '/picture';
	    	} else {
	    		$scope.picture = $scope.profilePicture;
	    	}
	    } else {
	    	$scope.isLoggedUser = false;
	    	$scope.picture = 'images/profile_default.png';
	    }

		restService.hasRelationshipWithUser(user.userid)
			.then(function(data) {
				$scope.hasRelationship = data.relationship;
			}).catch(function(error) {
					$scope.hasRelationship = false;
			});

		updateComments(user, commentParams);

		$scope.getFirstPage = function() {
			commentParams.pageNum = 1;
			updateComments(user, commentParams);
		};

		$scope.getPrevPage = function() {
			commentParams.pageNum--;
			updateComments(user, commentParams);
		};

		$scope.getNextPage = function() {
			commentParams.pageNum++;
			updateComments(user, commentParams);
		};

		$scope.getLastPage = function() {
			commentParams.pageNum = $scope.commentsLastPageNum;
			updateComments(user, commentParams);
		};

		$scope.commentText = {};

		$scope.commentSubmit = function() {
			if ($scope.commentForm.$valid) {
				restService.commentUser(user.userid, $scope.commentText.comment).then(function(data) {
					commentParams.pageNum = 1;
					updateComments(user, commentParams);
					$scope.commentForm.$setPristine();
					$scope.commentForm.$setUntouched();
					$scope.commentText = {};
				});
			}
		};

		function updateComments(user, commentParams) {
			restService.getUserComments(user.userid, commentParams)
			    .then(function(data) {
					$scope.comments = data.comments;
					$scope.commentCount = data.commentCount;
					$scope.commentsLastPageNum = data.pageCount;
					$scope.commentsPageInitIndex = data.commentsPageInitIndex;
					$scope.commentsPageNum = commentParams.pageNum;
				});
		}

	}]);
});
