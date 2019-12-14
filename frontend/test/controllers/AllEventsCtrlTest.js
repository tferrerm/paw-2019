define(['controllers/AllEventsCtrl', 'angular-mocks'], function() {
  describe('All Events Controller', function () {
    var controller, scope;

    var TEST_EVENT_ARRAY =
      [{
        eventid: 1,
        createdAt: "2019-12-12T14:55:18.752Z",
        name: "Argentina"
      },
      {
        eventid: 2,
        createdAt: "2019-13-12T14:55:18.752Z",
        name: "Event"
      }];
    var TEST_EVENT_COUNT = 2;
    var TEST_LAST_PAGE_NUM = 1;
    var TEST_INITIAL_PAGE_INDEX = 1;
    var TEST_PAGE_NUM = 1;

    beforeEach(module('frontend'));

    beforeEach(inject(function($injector, $rootScope, $controller) {
      scope = {
        events: TEST_EVENT_ARRAY,
        eventCount: TEST_EVENT_COUNT,
        lastPageNum:TEST_LAST_PAGE_NUM,
        initialPageIndex: TEST_INITIAL_PAGE_INDEX,
        pageNum: TEST_PAGE_NUM
      };
      controller = $controller('AllEventsCtrl', {$scope: scope});
    }));

    describe('$scope.events', function () {
      it('should be defined', function () {
        expect(scope.events).toBeDefined();
      });

      it('should contain two events ', function () {
        expect(scope.events.length).toBe(2);
      });

      it('should not have duplicate ids ', function () {
        const ids = scope.events.map(function (event) {return event.eventid})
        var isDuplicate = ids.some(function(item, idx){
          return ids.indexOf(item) !== idx
        });

        expect(isDuplicate).toBe(false);
      });

    });

    describe('$scope.eventCount', function () {
      it('should be defined', function () {
        expect(scope.eventCount).toBeDefined();
      });

      it('should equal 2', function () {
        expect(scope.eventCount).toBe(2);
      });

    });

    describe('$scope.pageNum', function () {
      it('should be defined', function () {
        expect(scope.pageNum).toBeDefined();
      });

      it('should equal 1', function () {
        expect(scope.pageNum).toBe(1);
      });

    });

    describe('$scope.lastPageNum', function () {
      it('should be defined', function () {
        expect(scope.lastPageNum).toBeDefined();
      });

      it('should equal 1', function () {
        expect(scope.lastPageNum).toBe(1);
      });
    });
  })
})
