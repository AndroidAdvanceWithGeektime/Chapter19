package com.sample.battery.wakelock;

import android.content.Context;
import android.util.Log;

import com.sample.battery.hook.BaseHookHandle;
import com.sample.battery.hook.ProxyHook;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

public class IPowerManagerHook extends ProxyHook {

    public IPowerManagerHook(Context context) {
        super(context);
    }

    @Override
    public BaseHookHandle createBaseHookHandle() {
        return new IPowerManagerHookHandle();
    }

    @Override
    public void onInstall() {
        Object oldObj = mHostContext.getSystemService(Context.POWER_SERVICE);
        Class<?> clazz = oldObj.getClass();

        try {
            Field field = clazz.getDeclaredField("mService");
            field.setAccessible(true);

            final Object mService = field.get(oldObj);
            setProxyObj(mService);

            Object newObj = Proxy.newProxyInstance(this.getClass().getClassLoader(), mService.getClass().getInterfaces(), this);
            field.set(oldObj, newObj);

        } catch (NoSuchFieldException e) {
            Log.e("HOOOOOOOOK", "IPowerManager is null");
        } catch (IllegalAccessException e){
            e.fillInStackTrace();
        }

    }
}