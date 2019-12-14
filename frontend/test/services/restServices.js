define(['services/restService', 'angular-mocks'], function() {

  describe('Rest Service', function () {
    var restService, url, $httpBackend, $q;
    var DUMMY_USER = {id: '1', name: 'tomaco'};
    var DUMMY_PRODUCTS = [{id: '1', name: 'slime rancher', category: 'game'},
      {id: '2', name: 'apple watch', category: 'gadget'}];
    var RESPONSE_ERROR = {detail: 'Not found.'};

    beforeEach(module('productSeek'));

    beforeEach(inject(function (_restService_, _url_, _$httpBackend_, _$q_) {
      restService = _restService_;
      url = _url_;
      $httpBackend = _$httpBackend_;
      $q = _$q_;
    }));

    it('should be defined', function () {
      expect(restService).toBeDefined();
    });
  })
})
