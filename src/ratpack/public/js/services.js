define([
    'console', 'angular'
], function (console, angular) {
    "use strict";
    var module = angular.module("services", ["ngResource"]);

    module.factory("UserInfo", function ($resource) {
        return $resource("/data/current-user", {}, {
            get: {method: "GET"}
        });

    });

    module.factory("currentUser", function (UserInfo) {
        var m = {loading: true, username: null};
        UserInfo.get({},
            function (data) {
                if (data.username != null) {
                    m.username = data.username;
                }
                m.loading = false;
            },
            function () {
                m.loading = false;
                console.log("something went wrong reading user status");
            }
        );
        return m;
    });

    return module;
});