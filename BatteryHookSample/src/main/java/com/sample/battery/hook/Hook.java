package com.sample.battery.hook;

import android.content.Context;

public abstract class Hook {

    protected BaseHookHandle mBaseHookHandle;

    protected Context mHostContext;

    public Hook(Context context) {
        mHostContext = context;
        mBaseHookHandle = createBaseHookHandle();
    }

    public abstract BaseHookHandle createBaseHookHandle();

    public abstract void onInstall();
}