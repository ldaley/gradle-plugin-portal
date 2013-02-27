define([
    'console', 'angular'
], function (console, angular) {
    "use strict";
    var module = angular.module("directives", []);

    module.directive("readOnlyOn", function() {
        return function(scope, element, attrs) {
            var readOnly = false;

            function updateReadOnly() {
                element.find("input,select").attr("readonly", readOnly ? "readonly" : null);
                element.find("input[type=checkbox],button").attr("disabled", readOnly ? "disabled" : null);
            }

            scope.$watch(attrs.readOnlyOn, function(value) {
                readOnly = value;
                updateReadOnly();
            });
        }
    });

    return module;
});