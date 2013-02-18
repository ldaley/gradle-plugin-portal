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
    },
    urlArgs: "bust=" + (new Date()).getTime()
});

define([
    'console', 'angular', 'services', 'controllers', "angular.ui/all"
], function (console, angular, services, controllers, ui) {
    "use strict";
    var module = angular.module('app', ['controllers', 'services']);
    module.config(['$routeProvider', '$httpProvider', function ($routeProvider) {
        $routeProvider.
            when('/', {templateUrl: "partials/main.html", controller: "main"}).
            otherwise({redirectTo: '/'});
    }]);

    angular.bootstrap(document, ['app']);
});