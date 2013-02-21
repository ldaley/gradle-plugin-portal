define(["angular", "console"], function (angular, console) {
    var loadJs = function (url, callback) {
        callback = callback || function () {};

        var script = document.createElement("script");
        script.type = "text/javascript";
        script.src = url;

        if (script.readyState) {  //IE
            script.onreadystatechange = function () {
                if (script.readyState == "loaded" ||
                    script.readyState == "complete") {
                    script.onreadystatechange = null;
                    callback();
                }
            };
        } else {  //Others
            script.onload = function () {
                callback();
            };
        }

        document.getElementsByTagName("head")[0].appendChild(script);

        if (typeof Envjs != 'undefined') {
            Envjs.loadLocalScript(script);
            callback();
        }
    };

    var loadJsSet = function (scripts, callback) {
        var num = scripts.length;
        var countdown = num;
        for (i = 0; i < num; ++i) {
            loadJs(scripts[i], function () {
                console.log(scripts[i] + " done");
                countdown -= 1;
                if (countdown == 0) {
                    callback();
                }
            });
        }
    };

    window.angular = angular;

    loadJsSet(["test-lib/angular.mocks.js"], function () {
        loadJsSet([${tests.collect { "'$it'" }.join(', ')}], function () {
            var jasmineEnv = jasmine.getEnv();
            jasmineEnv.addReporter(new jasmine.ConsoleReporter());
            jasmineEnv.addReporter(new jasmine.BootstrapReporter());
            jasmineEnv.addReporter(new jasmine.JUnitXmlReporter("${junitDirPath}/"));
            jasmineEnv.execute();
        });
    });

});