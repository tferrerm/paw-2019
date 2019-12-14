define(['services/authService', 'angular-mocks'], function() {

  describe('Auth Service', function() {
    var authService, url, http, q;
    var TEST_USER = {id: '1', name: 'User'};
    var TEST_USERNAME = 'user@user.com';
    var TEST_PASSWORD = '123456';
    var TEST_CREDENTIALS = 'j_username=' + encodeURIComponent(TEST_USERNAME) + '&j_password=' + encodeURIComponent(TEST_PASSWORD);
    var TEST_TOKEN = 'tok3NT0KENte5t1ngT0k3n';

    beforeEach(module('frontend'));

    beforeEach(inject(function(_authService_, _url_, _$httpBackend_, _$q_) {
      authService = _authService_;
      url = _url_;
      http = _$httpBackend_;
      q = _$q_;

      http.whenPOST(url + '/users/login', TEST_CREDENTIALS).respond(200, q.when(''), {headers: {'X-AUTH-TOKEN': TEST_TOKEN}});
      http.whenGET(url + '/user', undefined, {'X-AUTH-TOKEN': TEST_TOKEN}).respond(200, q.when(TEST_USER));
    }));

    it('should be defined', function() {
      expect(authService).toBeDefined();
    });

    describe('.login()', function() {
      it('should be defined', function() {
        expect(authService.login).toBeDefined();
      });

      it('should log in if user and pass are correct', function() {
        var loggedIn = false;
        authService.login(TEST_USERNAME, TEST_PASSWORD).then(function() {
        loggedIn = authService.isLoggedIn();
      });
        http.flush();
        expect(loggedIn).toBe(true);
      });

      it('should NOT log in given an incorrect user or password', function() {
        var loggedIn = false;
        var NEW_CREDS = 'j_username=' + encodeURIComponent(TEST_USERNAME) + '&j_password=' + encodeURIComponent('foo');

        http.expectPOST(url + '/users/login', NEW_CREDS).respond(401, q.reject({details: 'Authentication Failed'}));
        authService.login(TEST_USERNAME, 'foo').catch(function() {
        loggedIn = authService.isLoggedIn();
      });
        http.flush();
        expect(loggedIn).toBe(false);
      });
    });

    describe('.getLoggedUser()', function() {
      it('should be defined', function() {
        expect(authService.getLoggedUser).toBeDefined();
      });

      it('should return a user if logged in', function() {
        var testUser;

        authService.login(TEST_USERNAME, TEST_PASSWORD).then(function() {
        testUser = authService.getLoggedUser();
        });
        http.flush();
        expect(testUser).toEqual(TEST_USER);
      });

      it('should set the user to something not valid after logout', function() {
        var testUser;

        authService.login(TEST_USERNAME, TEST_PASSWORD)
          .then(function() {
            authService.logout();
            testUser = authService.getLoggedUser();
          }).catch(function() {});

        http.flush();
        expect(testUser).not.toBeTruthy();
      });
    });

    describe('.logout()', function() {
      it('should be defined', function() {
        expect(authService.logout).toBeDefined();
      });

      it('should logout for the cases when usr was previously logged in', function() {
        var loggedIn;

        authService.login(TEST_USERNAME, TEST_PASSWORD)
          .then(function() {
            loggedIn = authService.isLoggedIn();
            authService.logout();
          });

        http.flush();
        expect(loggedIn && !authService.isLoggedIn()).toBe(true);
      });
    });
  });
});
