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
            exports: "angular"
        }
    },
    urlArgs: "bust=" + (new Date()).getTime()
});