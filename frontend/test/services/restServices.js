define(['services/restService', 'angular-mocks'], function() {

  describe('Rest Service', function () {
    var restService, url, http, $q;

    var TEST_EVENTS = [{eventid: 1, name: "Evento 1"}, {eventid: 2, name: "Evento 2"}];

    beforeEach(module('frontend'));

    beforeEach(inject(function ($injector, $restService, $url, $httpBackend, $q) {
      restService = $restService;
      url = $url;
      http = $httpBackend;
      q = $q;
    }));

    it('should be defined', function () {
      expect(restService).toBeDefined();
    });

    describe('.getAllEvents()', function() {
      var response;

      it('should be defined', function() {
        expect(restService.getAllEvents).toBeDefined();
      });

      it('should return a valid list of events (no params)', function() {
        http.whenGET(url + '/events').respond(200, $q.when(TEST_EVENTS));

        restService.getAllEvents().then(function(res) {response = res;});

        http.flush();
        expect(response).toEqual(TEST_EVENTS);
      });

      it('should return a valid list of events (with params)', function() {
        http.whenGET(url + '/events?pageNum=1').respond(200, $q.when(TEST_EVENTS[0]));

        restService.getAllEvents({pageNum: 1}).then(function(resp) {response = resp});
        http.flush();
        expect(response).toEqual(TEST_EVENTS[0]);
      });
    });
  })


})
