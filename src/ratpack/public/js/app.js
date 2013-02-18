"use strict";

require.config({
  paths: {
    console: 'libs/console-min',
    jQuery: 'libs/jquery-min',
    underscore: 'libs/underscore-min',
    angular: 'libs/angular-min'
  },
  shim: {
    console: {
      exports: 'debug'
    },
    jQuery: {
      exports: '$'
    },
    underscore: {
      exports: '_'
    },
    angular: {
      exports: "angular"
    }
  }
});

define([
  'console', 'angular', 'services', 'controllers'
], function (console, angular, services, controllers) {
  "use strict";

  angular.module('app', ['controllers', 'services']).
    config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/', {templateUrl: "partials/main.html", controller: "main"}).
        otherwise({redirectTo: '/'});
  }]);

  angular.bootstrap(document, ['app']);

});