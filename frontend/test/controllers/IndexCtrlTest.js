define(['controllers/IndexCtrl', 'angular-mocks'], function() {

  describe('Index Controller test', function() {

    var controller, scope;

    beforeEach(module('frontend'));

    beforeEach(inject(function($injector, $rootScope, $controller) {
      scope = $rootScope.$new();
      controller = $controller('IndexCtrl', { $scope: scope });
    }));

    describe('$scope.welcomeText', function() {
      it('should be well set', function() {
        expect(scope.welcomeText).toBe('Welcome to your frontend page');
      });

    });
  });
});
