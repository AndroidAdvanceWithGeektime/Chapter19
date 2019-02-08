package com.sample.battery.alarm;

import android.util.Log;

import com.sample.battery.hook.BaseHookHandle;
import com.sample.battery.hook.HookedMethodHandler;
import com.sample.battery.hook.Utils;

import java.lang.reflect.Method;

public class IAlarmManagerHookHandle extends BaseHookHandle {

    @Override
    public void init() {
        sHookedMethodHandlers.put("set", new setAlarmManagerService());
        sHookedMethodHandlers.put("remove", new removeAlarmManagerService());
    }

    public class setAlarmManagerService extends HookedMethodHandler {

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            // 设置 Alarm
            Log.e("HOOOOOOOOK", "--set--");
            Log.e("HOOOOOOOOK", Utils.getStackTrace());

            // 前台：单个Alarm每小时不能启动超20次
            // 后台：单个Alarm每小时不能启动超10次
            return super.beforeInvoke(receiver, method, args);
        }

        @Override
        protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) throws Throwable {
            super.afterInvoke(receiver, method, args, invokeResult);
        }
    }

    public class removeAlarmManagerService extends HookedMethodHandler {

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            // 清除 Alarm
            Log.e("HOOOOOOOOK", "--remove--");
            Log.e("HOOOOOOOOK", Utils.getStackTrace());
            return super.beforeInvoke(receiver, method, args);
        }

        @Override
        protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) throws Throwable {
            super.afterInvoke(receiver, method, args, invokeResult);
        }
    }


}