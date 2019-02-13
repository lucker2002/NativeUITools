package org.apache.cordova.nativeUITools;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;
import android.content.res.Resources;
import android.content.Context;
import java.lang.reflect.Method;
import android.util.DisplayMetrics;

/**
 * This class echoes a string called from JavaScript.
 */
public class NativeUITools extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getNotchHeight")) {

            Activity activity = this.cordova.getActivity();
            String manufacturer = Build.MANUFACTURER;
            int notchHeight = 0;
            if (manufacturer.equalsIgnoreCase("XiaoMi")) {
                notchHeight = getXiaoMi(activity);
            }
            if (manufacturer.equalsIgnoreCase("HuaWei")) {
                notchHeight = getHuaWei(activity);
            }
            if (notchHeight == 0) {
                Resources resources = activity.getResources();
                int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
                notchHeight = resources.getDimensionPixelSize(resourceId);
            }

            callbackContext.success(notchHeight + "");

            return true;
        }

        if (action.equals("getEquipmentWidth")) {

            Activity activity = this.cordova.getActivity();
            DisplayMetrics displaysMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
            String width = "" + displaysMetrics.widthPixels;
            callbackContext.success(width);
            return true;
        }
        if (action.equals("setStatusBarColorType")) {

            if (Build.VERSION.SDK_INT >= 21) {
                this.cordova.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String type = "";
                        try {
                            type = args.getString(0);

                        } catch (Exception e) {
                            callbackContext.error("error");
                        }
                        setStatusBarColorType(type, callbackContext);

                    }
                });

            } else {
                return false;
            }

            return true;
        }
        if (action.equals("setImmerse")) {
            this.cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setImmerse(callbackContext);
                    String type = "";
                    try {
                        type = args.getString(0);

                    } catch (Exception e) {
                        callbackContext.error("error");
                    }
                    setStatusBarColorType(type, callbackContext);
                }
            });

            return true;
        }
        if (action.equals("navigationBarShow")) {
            this.cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    navigationBarShow();

                }
            });

            return true;
        }
        if (action.equals("getNavigationBarHeight")) {

            String height = getNavigationBarHeight() + "";

            callbackContext.success(height + "");
            return true;
        }
        if (action.equals("setFullScreen")) {
            if (Build.VERSION.SDK_INT >= 21) {
                this.cordova.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setFullScreen(callbackContext);

                    }
                });

            } else {
                return false;
            }
            return true;
        }
        return false;
    }

    private void setStatusBarColorType(final String type, CallbackContext callbackContext) {
        if (Build.VERSION.SDK_INT >= 21) {
            final Window window = cordova.getActivity().getWindow();
            int opt = window.getDecorView().getSystemUiVisibility();

            if (type.equals("darkColor")) {

                opt = opt | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else if (type.equals("lightColor")) {
                opt = opt & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            callbackContext.success(type);
            window.getDecorView().setSystemUiVisibility(opt);

        }

    }

    private void setImmerse(CallbackContext callbackContext) {
        if (Build.VERSION.SDK_INT >= 21) {
            final Window window = cordova.getActivity().getWindow();

            int opt = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            if (Build.VERSION.SDK_INT >= 19) {
                opt |= 0x00001000;
            } else {
                opt |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }

            window.getDecorView().setSystemUiVisibility(opt);

            // window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // 设置状态栏为透明 ,必须为沉浸时才有效
            window.setStatusBarColor(Color.TRANSPARENT);
            callbackContext.success(opt);
        }

    }

    private int getNavigationBarHeight() {
        final Activity activity = this.cordova.getActivity();
        final Window window = cordova.getActivity().getWindow();
        int height = 0;
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        boolean isHidden = (window.getDecorView().getSystemUiVisibility()
                & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (!isHidden) {
            height = resources.getDimensionPixelSize(resourceId);
        }

        return height;
    }

    private void navigationBarShow() {
        final Window window = cordova.getActivity().getWindow();
        int opt = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(opt);
    }

    private void setFullScreen(CallbackContext callbackContext) {
        final Window window = cordova.getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        int opt = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= 19) {
            opt |= 0x00001000;
        } else {
            opt |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        window.getDecorView().setSystemUiVisibility(opt);
    }

    private static int getHuaWei(Context context) {

        int[] ret = new int[] { 0, 0 };
        try {
            ClassLoader cl = context.getClassLoader();

            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");

            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (Exception e) {

        }

        return ret[1];
    }

    private static int getXiaoMi(Context context) {

        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
        int result = 0;

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);

        }
        return result;
    }
}
