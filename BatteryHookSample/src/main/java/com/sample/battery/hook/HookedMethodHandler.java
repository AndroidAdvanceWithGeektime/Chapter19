package com.sample.battery.hook;

import java.lang.reflect.Method;

public class HookedMethodHandler {

    public synchronized Object doHookInner(Object receiver, Method method, Object[] args) throws Throwable {
        boolean suc = beforeInvoke(receiver, method, args);
        Object invokeResult = null;
        if (!suc) {
            invokeResult = method.invoke(receiver, args);
        }
        afterInvoke(receiver, method, args, invokeResult);
        return invokeResult;
    }

    protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
        return false;
    }

    protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) throws Throwable {
    }
}