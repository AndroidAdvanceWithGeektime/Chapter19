package com.sample.battery.hook;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseHookHandle {

    protected Map<String, HookedMethodHandler> sHookedMethodHandlers = new HashMap<String, HookedMethodHandler>(5);

    public abstract void init();

    public BaseHookHandle() {
        init();
    }

    public HookedMethodHandler getHookedMethodHandler(Method method) {
        if (method != null) {
            return sHookedMethodHandlers.get(method.getName());
        } else {
            return null;
        }
    }
}