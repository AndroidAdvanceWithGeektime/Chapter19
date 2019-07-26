package com.sample.battery;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理Hook的属性，并在方法调用前回调 beforeInvoke
 */
public class ProxyHook implements InvocationHandler {
    private Object mHookTarget;
    private InvokeBeforeListener mListener;

    public ProxyHook(Object hookRef, String field, InvokeBeforeListener listener) {
        if (null == hookRef) {
            Log.e("Hoook", "hookRef is not exist");
            return;
        }
        try {
            Field fieldRef = hookRef.getClass().getDeclaredField(field);
            fieldRef.setAccessible(true);

            mHookTarget = fieldRef.get(hookRef);

            Object newObj = Proxy.newProxyInstance(this.getClass().getClassLoader(), mHookTarget.getClass().getInterfaces(), this);
            fieldRef.set(hookRef, newObj);
            this.mListener = listener;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setListener(InvokeBeforeListener listener) {
        mListener = listener;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (null != mListener) {
            mListener.beforeInvoke(method, args);
        }
        return method.invoke(mHookTarget, args);
    }

    public interface InvokeBeforeListener {
        void beforeInvoke(Method method, Object[] args);
    }
}
