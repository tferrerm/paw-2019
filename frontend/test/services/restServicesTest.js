'use strict';

define(['services/restService', 'angular-mocks'], function() {

  describe('Rest Service', function () {
    var restService, url, http, q;

    var TEST_EVENTS = [{eventid: 1, name: 'Evento 1'}, {eventid: 2, name: 'Evento 2'}];
    var TEST_TOURNAMENTS = [{tournamentid: 1, name: 'Tournament 1'}, {tournamentid: 2, name: 'Tournamnet 2'}];
    var TEST_PITCHES = [{pitchid: 1, name: 'Pitch 1'}, {pitchid: 2, name: 'Pitch 2'}];

    beforeEach(module('frontend'));

    beforeEach(inject(function ($injector, _restService_, _url_, _$httpBackend_, _$q_) {
      restService = _restService_;
      url = _url_;
      http = _$httpBackend_;
      q = _$q_;
    }));

    it('should be defined', function () {
      expect(restService).toBeDefined();
    });

    describe('.getAllEvents()', function() {
      var response;

      it('should be defined', function() {
        expect(restService.getAllEvents).toBeDefined();
      });


      it('should return a valid list of events (with params)', function() {
        http.whenGET(url + '/events?pageNum=1&name=&club=&sport=&vacancies=&date=').respond(200, q.when(TEST_EVENTS[0]));
        http.whenGET('views/home.html').respond(200,q.when([]));

        restService.getAllEvents({pageNum: 1}).then(function(resp) {
          response = resp;
        });
        http.flush();
        expect(response).toEqual(TEST_EVENTS[0]);
      });
    });

    describe('.getTournaments()', function() {
      var response;

      it('should be defined', function() {
        http.whenGET('views/home.html').respond(200,q.when([]));
        expect(restService.getTournaments).toBeDefined();
      });


      it('should return a valid list of tournaments (with params)', function() {
        http.whenGET(url + '/tournaments?pageNum=1').respond(200, q.when(TEST_TOURNAMENTS[0]));
        http.whenGET('views/home.html').respond(200,q.when([]));


        restService.getTournaments({pageNum: 1}).then(function(resp) {
          response = resp;
        });
        http.flush();
        expect(response).toEqual(TEST_TOURNAMENTS[0]);
      });
    });

    describe('.getPitches()', function() {
      var response;

      it('should be defined', function() {
        expect(restService.getPitches).toBeDefined();
      });

      it('should return a valid list of pitches (with params)', function() {
        http.whenGET('views/home.html').respond(200,q.when([]));
        http.whenGET(url + '/pitches?pageNum=1&name=&sport=&location=&club=').respond(200, q.when(TEST_PITCHES[0]));

        restService.getPitches({pageNum: 1}).then(function(resp) {
        response = resp;
      });
        http.flush();
        expect(response).toEqual(TEST_PITCHES[0]);
      });
    });
  });
});
