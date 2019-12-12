define(['controllers/ClubsCtrl', 'angular-mocks'], function() {
  describe('Clubs Controller', function () {
    var controller, scope;

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

    beforeEach(module('frontend'));

    beforeEach(inject(function($injector, $rootScope, $controller) {
      // scope = $rootScope.$new();
      scope = {
        clubs: TEST_CLUB_ARRAY,
        clubCount: TEST_CLUB_COUNT,
        lastPageNum:TEST_LAST_PAGE_NUM,
        initialPageIndex: TEST_INITIAL_PAGE_INDEX,
        pageNum: TEST_PAGE_NUM
      }
      controller = $controller('ClubsCtrl', {$scope: scope});
    }));

    describe('$scope.events', function () {
      it('should be defined', function () {
        expect(scope.clubs).toBeDefined();
      });

      it('should contain one event ', function () {
        expect(scope.clubs.length).equal(1);
      });

      it('should not have duplicate ids ', function () {
        expect(
          scope.clubs.map(function (e) {
            return e.clubid
          }).filter(function(v,i) { return scope.clubs.indexOf(v) === i }).equal(scope.clubs.length));
      });

      it('should have the field location defined', function () {
        scope.clubs.forEach(function (club) {
          expect(club.location).toBeDefined();
        })
      });
    });

    describe('$scope.clubCount', function () {
      it('should be defined', function () {
        expect(scope.clubCount).toBeDefined();
      });

      it('should equal 2', function () {
        expect(scope.eventCount).equal(2);
      });

    });

    describe('$scope.pageNum', function () {
      it('should be defined', function () {
        expect(scope.pageNum).toBeDefined();
      });

      it('should equal 1', function () {
        expect(scope.pageNum).equal(1);
      });

    });

    describe('$scope.lastPageNum', function () {
      it('should be defined', function () {
        expect(scope.lastPageNum).toBeDefined();
      });

      it('should equal 1', function () {
        expect(scope.lastPageNum).equal(1);
      });
    });
  })
})
