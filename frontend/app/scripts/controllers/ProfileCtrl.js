'use strict';
define(['frontend', 'services/restService', 'services/authService'], function(frontend) {

	frontend.controller('ProfileCtrl', ['$http', 'url', '$scope', '$location', 'restService', 'authService', 'user', function($http, url, $scope, $location, restService, authService, user) {
		var commentParams = {pageNum: 1};
		$scope.user = user;
		$scope.isLoggedIn = authService.isLoggedIn();
	    if ($scope.isLoggedIn) {
	    	$scope.loggedUser = authService.getLoggedUser();
	    	$scope.isLoggedUser = $scope.loggedUser.userid === user.userid;
	    } else {
	    	$scope.isLoggedUser = false;
	    }

    	restService.getUserProfilePicture(user.userid).then(function(data) {
    		$scope.picture = 'data:image/png;base64,' + _arrayBufferToBase64(data);
    	}).catch(function(error) {
    		$scope.picture = '../../images/profile_default.png';
    	});

		function _arrayBufferToBase64(buffer) {
		    var binary = '';
		    var bytes = new Uint8Array(buffer);
		    var len = bytes.byteLength;
		    for (var i = 0; i < len; i++) {
		      binary += String.fromCharCode(bytes[i]);
		    }
		    return window.btoa(binary);
		}

	    restService.hasRelationshipWithUser(user.userid)
	    	.then(function(data) {
	    		$scope.hasRelationship = data.relationship;
	    	}).catch(function(error) {
alert(error.data || ' Error');
});

	    $scope.goToProfile = function(id) {
			$location.url('users/' + id);
		};

		$scope.$on('user:updated', function() {
			$scope.isLoggedIn = authService.isLoggedIn();
			if ($scope.isLoggedIn) {
		    	$scope.loggedUser = authService.getLoggedUser();
		    	$scope.isLoggedUser = $scope.loggedUser.userid === user.userid;
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
			}).catch(function(error) {
alert(error.data || ' Error');
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
				}).catch(function(error) {
alert(error.data || ' Error');
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
				}).catch(function(error) {
alert(error.data || ' Error');
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
				}).catch(function(error) {
alert(error.data || ' Error');
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
				}).catch(function(error) {
alert(error.data || ' Error');
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
						}).catch(function(error) {
alert(error.data || ' Error');
});
				});
			//}
		};

	}]);
});
