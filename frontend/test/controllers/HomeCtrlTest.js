define(['controllers/HomeCtrl', 'angular-mocks'], function() {
  describe('Home Controller', function () {
    var $controller, $rootScope;

    beforeEach(inject(function (_$controller_, _$rootScope_) {
      $controller = _$controller_;
      $rootScope = _$rootScope_;
    }));

    var buildController = function ($scope, routeParams) {
      return $controller('HomeCtrl', {
        noParticipations: false,
        scheduleHeaders: []
      });
    };

    describe('$scope.noParticipations', function () {
      it('should be defined', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.noParticipations).toBeDefined();
      });

      it('should be false', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.noParticipations).toBeFalse();
      });
    });

    describe('$scope.scheduleHeaders', function () {
      it('should be defined', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.scheduleHeaders).toBeDefined();
      });

      it('should be empty', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.scheduleHeaders.length).toBe(0);
      });

    });
  })
})
