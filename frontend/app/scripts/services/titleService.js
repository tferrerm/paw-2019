'use strict';
define(['frontend'], function(frontend) {
  frontend.factory('titleService', ['$window', function($window) {

    function getDefaultTitle() {
      return 'SportMatcher';
    };

    return {
      setDefaultTitle: function() {
        $window.document.title = getDefaultTitle();
      },
      setTitle: function(title) {
        $window.document.title = title + ' | ' + getDefaultTitle();
      }
    };
  }]);
});
