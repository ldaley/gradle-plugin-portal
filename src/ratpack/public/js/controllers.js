define([
    'console', 'angular', 'services'
], function (console, angular, services) {
    "use strict";
    var module = angular.module("controllers", ["services", 'ui.bootstrap.dialog']);

    module.controller("main", function ($rootScope) {
        $rootScope.title = "Gradle Plugins";
    });

    module.controller("login", function ($scope, dialog, $http, currentUser) {
        $scope.foo = "bar";
        $scope.request = {username: null, password: null, remember: true};
        $scope.requesting = false;
        $scope.failure = null;
        $scope.cancel = function () {
            dialog.close();
            return false;
        };

        $scope.login = function () {
            $scope.requesting = true;
            $http.post("/login", $scope.request).
                success(function (data, status, headers, config) {
                    if (data.success) {
                        currentUser.username = $scope.request.username;
                        dialog.close();
                    } else {
                        $scope.failure = data.failure;
                    }
                    $scope.requesting = false;
                }).
                error(function (data, status, headers, config) {
                    $scope.requesting = false;
                    dialog.close();
                });
        };
    });

    module.controller("leftnav", function ($scope) {
        $scope.categories = [
            {name: "Code Quality"},
            {name: "Something Else"}
        ]
    });

    module.controller("topnav", function ($scope, currentUser, $dialog) {
        $scope.projectName = "Gradle Plugins";
        $scope.user = currentUser;
        $scope.nav = [
            {name: "Admin"}
        ];
        $scope.doLogin = function () {
            var d = $dialog.dialog({modalFade: true});
            d.open("partials/login.html", "login");
        };

        $scope.logout = function() {
            window.location = "/logout";
        }
    });

    module.controller("plugins", function ($scope) {
        $scope.plugins = [
            {name: "Plugin One", description: "Blah blah blah"},
            {name: "Plugin Two", description: "Blah blah blah"}
        ];
    });

    return module;
});