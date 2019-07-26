package com.sample.battery;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class AppUtils {

    public static boolean isAppBackground(Context context)
    {
        final ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> list = manager.getRunningAppProcesses();

        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo item : list)
        {
            if (item.processName.equals(packageName))
            {
                return item.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }
}
