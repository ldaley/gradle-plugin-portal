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
            exports: "angular",
            deps: ['jQuery']
        }
    },
    urlArgs: "bust=" + (new Date()).getTime()
});

define([
    'console', 'angular', 'services', 'controllers', "directives", "angular.ui/all"
], function (console, angular) {
    "use strict";
    var module = angular.module('app', ['controllers', 'services', 'directives']);
    module.config(function ($routeProvider) {
        $routeProvider.
            when('/', {templateUrl: "partials/main.html", controller: "main"}).
            when('/register', {templateUrl: "partials/full.html", controller: "register"}).
            otherwise({redirectTo: '/'});
    });
    angular.bootstrap(document, ['app']);
});