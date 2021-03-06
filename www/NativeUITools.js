var exec = require('cordova/exec');

let NativeUITools = {

    //获取刘海高度
    getNotchHeight:function (arg0, success, error) {   
        exec(success, error, 'NativeUITools', 'getNotchHeight', []);
    },

    getEquipmentWidth:function (arg0, success, error) {
        exec(success, error, 'NativeUITools', 'getEquipmentWidth', []);
    },

    //设置沉浸
    setImmerse: function (arg0, success, error) {
        exec(success, error, 'NativeUITools', 'setImmerse', [arg0]);
    },

    //获取虚拟按键高度
    getNavigationBarHeight:function (arg0, success, error) {
        exec(success, error, 'NativeUITools', 'getNavigationBarHeight', []);
    },

    //设置全屏
    setFullScreen:function (arg0, success, error) {
        exec(success, error, 'NativeUITools', 'setFullScreen', []);
    },

    setStatusBarColorType:function(arg0, success, error){
        exec(success, error, 'NativeUITools', 'setStatusBarColorType', [arg0]);
    },
    navigationBarShow:function (arg0, success, error) {
        exec(success, error, 'NativeUITools', 'navigationBarShow', [arg0]);
        
    }
}
module.exports = NativeUITools;
