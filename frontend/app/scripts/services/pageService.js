'use strict';
define(['frontend'], function(frontend) {

    frontend.factory('Page', function() {
        var title = 'Sportmatcher';
        return {
          title: function() {
              return title;
            },
          setTitle: function(newTitle) {
              title = newTitle;
            }
        };
      });
      
});
