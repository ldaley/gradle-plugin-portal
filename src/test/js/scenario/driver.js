define(["app", "angular"], function (app, angular) {
    loadJs(["test-lib/angular.mocks.js", "test-lib/angular.scenario.js"], function () {
        angular.module('e2eApp', ['app', 'ngMockE2E']).run(function(\$httpBackend) {
            window.\$httpBackend = \$httpBackend;
        });
        loadJs([${tests.collect { "'$it'" }.join(', ')}], function () {
            angular.scenario.setUpAndRun();
        });
    });
});