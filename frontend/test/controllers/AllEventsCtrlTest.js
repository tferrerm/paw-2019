define(['controllers/AllEventsCtrl', 'angular-mocks'], function() {
  describe('All Events Controller', function () {
    var $controller, $rootScope;

    var TEST_EVENT = { id: 1, name: 'Event 1'};
    var TEST_EVENT_ARRAY = [{id: 2, name: 'Event 2'},{id: 3, name: 'Event 3'}];
    var TEST_EVENT_COUNT = 2;
    var TEST_LAST_PAGE_NUM = 1;
    var TEST_INITIAL_PAGE_INDEX = 0;
    var TEST_PAGE_NUM = 1;

    beforeEach(inject(function (_$controller_, _$rootScope_) {
      $controller = _$controller_;
      $rootScope = _$rootScope_;
    }));

    var buildController = function ($scope, routeParams) {
      return $controller('AllEventsCtrl', {
        events: TEST_EVENT_ARRAY,
        eventCount: TEST_EVENT_COUNT,
        lastPageNum:TEST_LAST_PAGE_NUM,
        initialPageIndex: TEST_INITIAL_PAGE_INDEX,
        pageNum: TEST_PAGE_NUM
      });
    };

    describe('$scope.events', function () {
      it('should be defined', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.events).toBeDefined();
      });

      it('should contain two events ', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.event.length).toBe(2);
      });

      it('should not have duplicate ids ', function () {
        var $scope = {};
        buildController($scope);

        expect(
          $scope.event.map(function (e) {
            return e.id
          }).filter(function(v,i) { return $scope.event.indexOf(v) === i }).equal($scope.event.length));
      });
    });

    describe('$scope.eventCount', function () {
      it('should be defined', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.eventCount).toBeDefined();
      });

      it('should equal 2', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.scheduleHeaders.length).toBe(2);
      });

    });

    describe('$scope.pageNum', function () {
      it('should be defined', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.pageNum).toBeDefined();
      });

      it('should equal 2', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.scheduleHeaders.length).toBe(2);
      });

    });
  })
})
