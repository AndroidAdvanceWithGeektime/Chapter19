package com.sample.battery.gps;

import android.util.Log;

import com.sample.battery.hook.BaseHookHandle;
import com.sample.battery.hook.HookedMethodHandler;
import com.sample.battery.hook.Utils;

import java.lang.reflect.Method;

public class ILocationManagerHookHandle extends BaseHookHandle {

    @Override
    public void init() {
        sHookedMethodHandlers.put("requestLocationUpdates", new requestLocationManagerService());
        sHookedMethodHandlers.put("removeUpdates", new removeLocationManagerService());
    }

    public class requestLocationManagerService extends HookedMethodHandler {

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            // 获取定位
            Log.e("HOOOOOOOOK", "--requestLocationUpdates--");
            Log.e("HOOOOOOOOK", Utils.getStackTrace());

            // 前台：每小时使用不能超过30分钟
            // 后台：每小时使用不能超过15分钟
            return super.beforeInvoke(receiver, method, args);
        }

        @Override
        protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) throws Throwable {
            super.afterInvoke(receiver, method, args, invokeResult);
        }
    }

    public class removeLocationManagerService extends HookedMethodHandler {

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            // 清除监听
            Log.e("HOOOOOOOOK", "--removeUpdates--");
            Log.e("HOOOOOOOOK", Utils.getStackTrace());
            return super.beforeInvoke(receiver, method, args);
        }

        @Override
        protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) throws Throwable {
            super.afterInvoke(receiver, method, args, invokeResult);
        }
    }


}