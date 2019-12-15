/* eslint-env es6 */
'use strict';

define(['controllers/TournamentsCtrl', 'angular-mocks'], function() {
  describe('Tournaments Controller', function () {
    var controller, scope;

    var TEST_TOURNAMENT_ARRAY =
      [{
        tournamentid: 1,
        createdAt: '2019-12-12T14:55:18.752Z',
        name: 'Argentina'
      },
      {
        tournamentid: 2,
        createdAt: '2019-13-12T14:55:18.752Z',
        name: 'Tournament'
      }];
    var TEST_TOURNAMENT_COUNT = 2;
    var TEST_LAST_PAGE_NUM = 1;
    var TEST_INITIAL_PAGE_INDEX = 1;
    var TEST_PAGE_NUM = 1;

    beforeEach(module('frontend'));

    beforeEach(inject(function($injector, $rootScope, $controller) {
      scope = {
        tournaments: TEST_TOURNAMENT_ARRAY,
        tournamentCount: TEST_TOURNAMENT_COUNT,
        lastPageNum:TEST_LAST_PAGE_NUM,
        initialPageIndex: TEST_INITIAL_PAGE_INDEX,
        pageNum: TEST_PAGE_NUM
      };
      controller = $controller('TournamentsCtrl', {$scope: scope});
    }));

    describe('$scope.tournaments', function () {
      it('should be defined', function () {
        expect(scope.tournaments).toBeDefined();
      });

      it('should contain two tournaments ', function () {
        expect(scope.tournaments.length).toBe(2);
      });

      it('should not have duplicate ids ', function () {
        const ids = scope.tournaments.map(function (tournament) {
          return tournament.tournamentid;
        });
        var isDuplicate = ids.some(function(item, idx) {
          return ids.indexOf(item) !== idx;
        });

        expect(isDuplicate).toBe(false);
      });

    });

    describe('$scope.tournamentCount', function () {
      it('should be defined', function () {
        expect(scope.tournamentCount).toBeDefined();
      });

      it('should equal 2', function () {
        expect(scope.tournamentCount).toBe(2);
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
  });
});
