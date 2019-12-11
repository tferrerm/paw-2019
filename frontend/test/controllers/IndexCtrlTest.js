define(['controllers/IndexCtrl', 'angular-mocks'], function() {

  describe('Index Controller test', function() {

    var controller, scope;

    beforeEach(module('frontend'));

    beforeEach(inject(function($injector, $rootScope, $controller) {
      scope = $rootScope.$new();
      controller = $controller('IndexCtrl', {$scope: scope});
    }));

    describe('$scope.isLoggedIn', function() {
      it('should be well set', function() {
        expect(scope.isLoggedIn).toBe(false);
      });

    });
  });
});
