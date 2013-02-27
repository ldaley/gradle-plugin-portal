define(["app"], function (app) {
    loadJs(["test-lib/angular.mocks.js"], function () {
        loadJs([${tests.collect { "'$it'" }.join(', ')}], function () {
            var jasmineEnv = jasmine.getEnv();
            jasmineEnv.addReporter(new jasmine.ConsoleReporter());
            jasmineEnv.addReporter(new jasmine.BootstrapReporter());
            jasmineEnv.addReporter(new jasmine.JUnitXmlReporter("${junitDirPath}/"));
            jasmineEnv.execute();
        });
    });
});