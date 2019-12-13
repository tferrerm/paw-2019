define(['controllers/ClubsCtrl', 'angular-mocks'], function() {
  describe('Clubs Controller', function () {
    var controller, scope;

    var TEST_CLUB_ARRAY =
      [{
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
      scope = {
        clubs: TEST_CLUB_ARRAY,
        clubCount: TEST_CLUB_COUNT,
        lastPageNum:TEST_LAST_PAGE_NUM,
        initialPageIndex: TEST_INITIAL_PAGE_INDEX,
        pageNum: TEST_PAGE_NUM
      };
      controller = $controller('ClubsCtrl', {$scope: scope});
    }));

    describe('$scope.clubs', function () {
      it('should be defined', function () {
        expect(scope.clubs).toBeDefined();
      });

      it('should contain two clubs ', function () {
        expect(scope.clubs.length).toBe(2);
      });

      it('should not have duplicate ids ', function () {
        const ids = scope.clubs.map(function (club) {return club.clubid})
        var isDuplicate = ids.some(function(item, idx){
          return ids.indexOf(item) !== idx
        });

        expect(isDuplicate).toBe(false);
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
        expect(scope.clubCount).toBe(2);
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
