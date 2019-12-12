'use strict';
define(['frontend'], function(frontend) {

	frontend.factory('errorService', [function() {
		var _error = 'not_found';
		return {
			getError: function() {
				return _error;
			},
			setError: function(error) {
				_error = error;
			},
			clear: function() {
				_error = 'not_found';
			}
		};
	}]);
	
});
