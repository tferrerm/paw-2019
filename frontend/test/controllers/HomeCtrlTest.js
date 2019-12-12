define(['controllers/HomeCtrl', 'angular-mocks'], function() {
  describe('Home Controller', function () {
    var controller, scope;

    beforeEach(module('frontend'));

    beforeEach(inject(function($injector, $rootScope, $controller) {
      scope = $rootScope.$new();
      controller = $controller('HomeCtrl', {$scope: scope});
    }));

    describe('$scope.noParticipations', function () {
      it('should be defined', function () {
        expect(scope.noParticipations).toBeDefined();
      });

      it('should be false', function () {
        expect(scope.noParticipations).toBeFalse();
      });
    });

    describe('$scope.scheduleHeaders', function () {
      it('should be defined', function () {
        expect(scope.scheduleHeaders).toBeDefined();
      });

      it('should be empty', function () {
        expect(scope.scheduleHeaders.length).toBe(0);
      });

    });
  })
})
