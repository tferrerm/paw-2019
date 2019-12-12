'use strict';
define(['routes',
	'services/dependencyResolverFor',
	'i18n/i18nLoader!',
	'angular',
	'angular-route',
	'angular-bootstrap',
	'angular-sanitize',
	'bootstrap',
	'angular-translate',
	'ng-file-upload'],
	function(config, dependencyResolverFor, i18n) {
		var frontend = angular.module('frontend', [
			'ngRoute',
			'pascalprecht.translate',
			'ngSanitize',
			'ui.bootstrap',
			'ngFileUpload'
		]);
		frontend
			.config(
				['$routeProvider',
				'$controllerProvider',
				'$compileProvider',
				'$filterProvider',
				'$provide',
				'$translateProvider',
				'$locationProvider',
				function($routeProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, $translateProvider, $locationProvider) {

					frontend.controller = $controllerProvider.register;
					frontend.directive = $compileProvider.directive;
					frontend.filter = $filterProvider.register;
					frontend.factory = $provide.factory;
					frontend.service = $provide.service;

					if (config.routes !== undefined) {
						angular.forEach(config.routes, function(route, path) {
							var resolve = dependencyResolverFor(['controllers/' + route.controller]);
							angular.forEach(route.resolve, function(value, key) {
								resolve[key] = value;
							});

							$routeProvider.when(path, {
								templateUrl: route.templateUrl, 
								resolve: resolve, 
								controller: route.controller, 
								gaPageTitle: route.gaPageTitle});
						});
					}
					if (config.defaultRoutePath !== undefined) {
						$routeProvider.otherwise({redirectTo: config.defaultRoutePath});
					}

					$translateProvider.translations('preferredLanguage', i18n);
					$translateProvider.preferredLanguage('preferredLanguage');
					$translateProvider.useSanitizeValueStrategy('escapeParameters');

					//$locationProvider.html5Mode(false);
           			$locationProvider.hashPrefix('');
				}])
			.value('url', 'http://localhost:8080/webapp');
		return frontend;
	}
);
