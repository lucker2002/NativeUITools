var exec = require('cordova/exec');

exports.NativeUITools = function (arg0, success, error) {
    exec(success, error, 'cordova-plugin-nativeUITools', 'NativeUITools', [arg0]);
};
