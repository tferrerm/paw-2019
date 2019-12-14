define(['services/storageService', 'angular-mocks'], function() {
  describe('Storage Service', function () {
    var storageService;
    var TEST_USER = {name: 'Test user', id: 2};

    beforeEach(module('frontend'));

    beforeEach(inject(function ($storageService) {
      storageService = $storageService;
    }));

    it('should be defined', function() {
      expect(storageService).toBeDefined();
    });

    describe('.getUser()', function() {
      it('should be defined', function() {
expect(storageService.getUser).toBeDefined();
});
      it('should be initially null', function() {
expect(storageService.getUser()).toBeNull();
});
      it('should save the user correctly', function() {
        storageService.setUser(TEST_USER);
        expect(storageService.getUser()).toEqual(TEST_USER);
      });
    });
  });
});
