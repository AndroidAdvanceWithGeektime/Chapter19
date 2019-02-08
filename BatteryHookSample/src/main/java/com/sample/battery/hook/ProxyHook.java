package com.sample.battery.hook;

import android.content.Context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class ProxyHook extends Hook implements InvocationHandler {

    /**
     * 要代理的真实对象
     */
    private Object proxyObj;

    public ProxyHook(Context context) {
        super(context);
    }

    public void setProxyObj(Object proxyObj) {
        this.proxyObj = proxyObj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        HookedMethodHandler hookedMethodHandler = mBaseHookHandle.getHookedMethodHandler(method);
        if (hookedMethodHandler != null){
            return hookedMethodHandler.doHookInner(proxyObj, method, args);
        }
        return method.invoke(proxyObj, args);
    }
}