define([
    'console', 'angular'
], function (console, angular) {
    "use strict";
    var module = angular.module("controllers", []);

    module.controller("main", function ($rootScope, $scope) {
        $rootScope.title = "title";
        $scope.val = "changed";
    });

    module.controller("leftnav", function ($scope) {
        $scope.categories = [
            {name: "Code Quality"},
            {name: "Something Else"}
        ]
    });

    module.controller("topnav", function ($scope) {
        $scope.projectName = "Gradle Plugins";
        $scope.nav = [
            {name: "Admin"}
        ];
    });

    module.controller("plugins", function ($scope) {
        $scope.plugins = [
            {name: "Plugin One", description: "Blah blah blah"},
            {name: "Plugin Two", description: "Blah blah blah"}
        ];
    });

    return module;
});