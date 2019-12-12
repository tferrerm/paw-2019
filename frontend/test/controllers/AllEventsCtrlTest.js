define(['controllers/AllEventsCtrl', 'angular-mocks'], function() {
  describe('All Events Controller', function () {
    var $controller, $rootScope;

    var TEST_EVENT_ARRAY = [{
      createdAt: "2019-12-12T15:01:14.392Z",
      description: "Partido de tenis",
      endsAt: "2019-12-18T19:00:00Z",
      eventid: 1,
      inscriptionCount: 0,
      inscriptionEnd: "2019-12-17T01:04:00Z",
      maxParticipants: 2,
      name: "Partido",
      owner: {
        firstname: "Santiago",
        lastname: "Swinnen",
        role: "admin",
        userid: 1,
        username: "santiagoswinnen@hotmail.com"
      },
      pitch: {
        club: {
          clubid: 1,
          createdAt: "2019-12-12T14:55:18.752Z",
          location: "Buenos Aires",
          name: "Club",
        },
        createdAt: "2019-12-12T14:55:30.421Z",
        name: "Court 1",
        pitchid: 1,
        sport: "TENNIS",
        startsAt: "2019-12-18T14:00:00Z"
      }
    }];
    var TEST_EVENT_COUNT = 1;
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

      it('should equal 1', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.scheduleHeaders.length).toBe(1);
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
