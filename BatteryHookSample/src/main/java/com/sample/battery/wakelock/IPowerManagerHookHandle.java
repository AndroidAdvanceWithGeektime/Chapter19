package com.sample.battery.wakelock;

import android.util.Log;

import com.sample.battery.hook.BaseHookHandle;
import com.sample.battery.hook.HookedMethodHandler;
import com.sample.battery.hook.Utils;

import java.lang.reflect.Method;

public class IPowerManagerHookHandle extends BaseHookHandle {

    @Override
    public void init() {
        sHookedMethodHandlers.put("acquireWakeLock", new acquirePowerManagerService());
        sHookedMethodHandlers.put("releaseWakeLock", new releasePowerManagerService());
    }

    public class acquirePowerManagerService extends HookedMethodHandler {

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            // 申请 Wakelock
            Log.e("HOOOOOOOOK", "--acquireWakeLock--");
            Log.e("HOOOOOOOOK", Utils.getStackTrace());
            // 前台：单个wakelock每小时不超过30分钟或者20次
            // 后台：单个wakelock每小时不超过10分钟或者12次
            return super.beforeInvoke(receiver, method, args);
        }

        @Override
        protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) throws Throwable {
            super.afterInvoke(receiver, method, args, invokeResult);
        }
    }

    public class releasePowerManagerService extends HookedMethodHandler {

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            // 释放 Wakelock
            Log.e("HOOOOOOOOK", "--releaseWakeLock--");
            Log.e("HOOOOOOOOK", Utils.getStackTrace());
            return super.beforeInvoke(receiver, method, args);
        }

        @Override
        protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) throws Throwable {
            super.afterInvoke(receiver, method, args, invokeResult);
        }
    }
}