"use strict";

define([
    'console', 'angular', 'services', 'controllers', "angular.ui/all"
], function (console, angular, services, controllers, ui) {
    "use strict";
    var module = angular.module('app', ['controllers', 'services']);
    module.config(function ($routeProvider) {
        $routeProvider.
            when('/', {templateUrl: "partials/main.html", controller: "main"}).
            otherwise({redirectTo: '/'});
    });

    return angular;
});