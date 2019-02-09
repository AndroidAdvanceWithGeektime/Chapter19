package com.sample.battery;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MainActivity extends Activity {
    public static Context sContext;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sContext = getApplicationContext();
        final Button hookAlarm = (Button) findViewById(R.id.hook_alarm);
        hookAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hookAlarmManager(sContext);
            }
        });


        final Button hookWakelock = (Button) findViewById(R.id.hook_wakelock);
        hookWakelock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hookPowerManager(sContext);

            }
        });

        final Button hookGPS = (Button) findViewById(R.id.hook_gps);
        hookGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hookLoactionManager(sContext);
            }
        });

    }


    private void hookPowerManager(Context context) {

        try {
            PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
            final Object OrginmService = ReflectUtil.getField(powerManager.getClass(), powerManager, "mService");
            Class iPM = Class.forName("android.os.IPowerManager");
            Object newPM = Proxy.newProxyInstance(getClassLoader(), new Class[]{iPM}, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    if (method.getName().equalsIgnoreCase("acquireWakeLock") ||
                            method.getName().equalsIgnoreCase("releaseWakeLock")) {
                        getBatteryInfo();
                    }

                    return method.invoke(OrginmService, objects);

                }
            });

            ReflectUtil.setField(powerManager.getClass(), powerManager, "mService", newPM);

        } catch (Exception e) {
            Log.e("hookPowerManager", e.getMessage());
        }

    }


    private void hookAlarmManager(Context context) {

        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            final Object orginmService = ReflectUtil.getField(alarmManager.getClass(), alarmManager, "mService");
            Class iAM = Class.forName("android.app.IAlarmManager");
            Object newAM = Proxy.newProxyInstance(getClassLoader(), new Class[]{iAM}, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    getBatteryInfo();
                    return method.invoke(orginmService, objects);

                }
            });

            ReflectUtil.setField(alarmManager.getClass(), alarmManager, "mService", newAM);
            
        } catch (Exception e) {

            Log.e("hookAlarmManager", e.getMessage());

        }

    }


    private void hookLoactionManager(Context context) {
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            final Object orginmService = ReflectUtil.getField(locationManager.getClass(), locationManager, "mService");
            Class iLM = Class.forName("android.location.ILocationManager");
            Object newLM = Proxy.newProxyInstance(getClassLoader(), new Class[]{iLM}, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    getBatteryInfo();
                    return method.invoke(orginmService, objects);

                }
            });

            ReflectUtil.setField(locationManager.getClass(), locationManager, "mService", newLM);

        } catch (Exception e) {

            Log.e("hookLoactionManager", e.getMessage());

        }

    }


    private void getBatteryInfo() {

        String stacktrace = Log.getStackTraceString(new Throwable());

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float) scale;

        BatteryInfo batteryInfo = new BatteryInfo(isCharging, stacktrace, batteryPct);

        BatteryInfoManager.getInstance().addBatteryInfo(batteryInfo);

    }
}
