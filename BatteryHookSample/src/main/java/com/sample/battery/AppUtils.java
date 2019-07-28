package com.sample.battery;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import java.util.List;

public class AppUtils {

    public static boolean isAppBackground(Context context) {
        final ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> list = manager.getRunningAppProcesses();

        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo item : list) {
            if (item.processName.equals(packageName)) {
                return item.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }

    private static float sDensity;
    private static float sScaledDensity;

    /**
     * 自定义 dp 适配方案
     * 需要在Activity的 onCreate 方法时调用
     *
     * from toutiaotechblog
     * @param designWdithDps 通常为 360dp
     * @param application
     * @param activity
     */
    public static void customDensity(int designWdithDps, final Application application, Activity activity) {
        DisplayMetrics appMetrics = application.getResources().getDisplayMetrics();
        if (sDensity == 0) {
            sDensity = appMetrics.density;
            sScaledDensity = appMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }

                }

                @Override
                public void onLowMemory() {

                }
            });

        }
        float scaleRatio = sScaledDensity / sDensity;
        float targetDensity = (float) appMetrics.widthPixels / designWdithDps;
        float targetScaledDensity = scaleRatio * targetDensity;
        int targetDensityDpi = (int) (160 * targetDensity);

        DisplayMetrics activityMetrics = activity.getResources().getDisplayMetrics();
        appMetrics.density = activityMetrics.density = targetDensity;
        appMetrics.scaledDensity = activityMetrics.scaledDensity = targetScaledDensity;
        appMetrics.densityDpi = activityMetrics.densityDpi = targetDensityDpi;

    }
}
