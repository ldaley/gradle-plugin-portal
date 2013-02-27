(function () {
    var single = function (url, callback) {
        callback = callback || function () {
        };

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

    window.loadJs = function (scripts, callback) {
        var num = scripts.length;
        var countdown = num;
        for (i = 0; i < num; ++i) {
            single(scripts[i], function () {
                countdown -= 1;
                if (countdown == 0) {
                    callback();
                }
            });
        }
    }
})();