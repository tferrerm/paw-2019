define(['controllers/ClubsCtrl', 'angular-mocks'], function() {
  describe('Clubs Controller', function () {
    var $controller, $rootScope;

    var TEST_CLUB_ARRAY = [{
      clubid: 1,
      createdAt: "2019-12-12T14:55:18.752Z",
      location: "Buenos Aires",
      name: "Argentina"
    },
      {
        clubid: 2,
        createdAt: "2019-13-12T14:55:18.752Z",
        location: "Buenos Aires",
        name: "Club"
      }];
    var TEST_CLUB_COUNT = 2;
    var TEST_LAST_PAGE_NUM = 1;
    var TEST_INITIAL_PAGE_INDEX = 1;
    var TEST_PAGE_NUM = 1;

    beforeEach(inject(function (_$controller_, _$rootScope_) {
      $controller = _$controller_;
      $rootScope = _$rootScope_;
    }));

    var buildController = function ($scope, routeParams) {
      return $controller('ClubsCtrl', {
        clubs: TEST_CLUB_ARRAY,
        clubCount: TEST_CLUB_COUNT,
        lastPageNum:TEST_LAST_PAGE_NUM,
        initialPageIndex: TEST_INITIAL_PAGE_INDEX,
        pageNum: TEST_PAGE_NUM
      });
    };

    describe('$scope.events', function () {
      it('should be defined', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.clubs).toBeDefined();
      });

      it('should contain one event ', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.clubs.length).equal(1);
      });

      it('should not have duplicate ids ', function () {
        var $scope = {};
        buildController($scope);

        expect(
          $scope.clubs.map(function (e) {
            return e.clubid
          }).filter(function(v,i) { return $scope.clubs.indexOf(v) === i }).equal($scope.clubs.length));
      });

      it('should have the field location defined', function () {
        var $scope = {};
        buildController($scope);

        $scope.clubs.forEach(function (club) {
          expect(club.location).toBeDefined();
        })
      });
    });

    describe('$scope.clubCount', function () {
      it('should be defined', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.clubCount).toBeDefined();
      });

      it('should equal 2', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.eventCount).equal(2);
      });

    });

    describe('$scope.pageNum', function () {
      it('should be defined', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.pageNum).toBeDefined();
      });

      it('should equal 1', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.pageNum).equal(1);
      });

    });

    describe('$scope.lastPageNum', function () {
      it('should be defined', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.lastPageNum).toBeDefined();
      });

      it('should equal 1', function () {
        var $scope = {};
        buildController($scope);

        expect($scope.lastPageNum).equal(1);
      });
    });
  })
})
