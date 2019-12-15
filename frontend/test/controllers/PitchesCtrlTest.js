/* eslint-env es6 */
'use strict';

define(['controllers/PitchesCtrl', 'angular-mocks'], function() {
  describe('Pitches Controller', function () {
    var controller, scope;

    var TEST_PITCH_ARRAY =
      [{
        pitchid: 1,
        createdAt: '2019-12-12T14:55:18.752Z',
        location: 'Buenos Aires',
        name: 'Argentina'
      },
      {
        pitchid: 2,
        createdAt: '2019-13-12T14:55:18.752Z',
        location: 'Buenos Aires',
        name: 'Pitch'
      }];
    var TEST_PITCH_COUNT = 2;
    var TEST_LAST_PAGE_NUM = 1;
    var TEST_INITIAL_PAGE_INDEX = 1;
    var TEST_PAGE_NUM = 1;

    beforeEach(module('frontend'));

    beforeEach(inject(function($injector, $rootScope, $controller) {
      scope = {
        pitches: TEST_PITCH_ARRAY,
        pitchCount: TEST_PITCH_COUNT,
        lastPageNum:TEST_LAST_PAGE_NUM,
        initialPageIndex: TEST_INITIAL_PAGE_INDEX,
        pageNum: TEST_PAGE_NUM
      };
      controller = $controller('PitchesCtrl', {$scope: scope});
    }));

    describe('$scope.pitches', function () {
      it('should be defined', function () {
        expect(scope.pitches).toBeDefined();
      });

      it('should contain two pitches ', function () {
        expect(scope.pitches.length).toBe(2);
      });

      it('should not have duplicate ids ', function () {
        const ids = scope.pitches.map(function (pitch) {
          return pitch.pitchid;
        });
        var isDuplicate = ids.some(function(item, idx) {
          return ids.indexOf(item) !== idx;
        });

        expect(isDuplicate).toBe(false);
      });

      it('should have the field location defined', function () {
        scope.pitches.forEach(function (pitch) {
          expect(pitch.location).toBeDefined();
        });
      });
    });

    describe('$scope.pitchCount', function () {
      it('should be defined', function () {
        expect(scope.pitchCount).toBeDefined();
      });

      it('should equal 2', function () {
        expect(scope.pitchCount).toBe(2);
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
