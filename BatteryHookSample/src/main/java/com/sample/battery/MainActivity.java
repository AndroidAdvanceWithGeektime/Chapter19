package com.sample.battery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.AlarmManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sample.battery.R;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.logging.Logger;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button hookAlarm = (Button) findViewById(R.id.hook_alarm);
        hookAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                new ProxyHook(alarmManager, "mService", new ProxyHook.InvokeBeforeListener() {
                    @Override
                    public void beforeInvoke(Method method, Object[] args) {
                        // 设置 Alarm
                        if (method.getName().equals("set")) {
                            // 不同版本参数类型的适配，获取应用堆栈等等
                            Log.d("Hoook", "set Alarm", new Throwable());
                            // 清除 Alarm
                        } else if (method.getName().equals("remove")) {
                            // 清除的逻辑
                            Log.d("Hoook", "清除 Alarm", new Throwable());
                        }
                    }
                });
                if (alarmManager != null) {
                    PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 1, new Intent(), PendingIntent.FLAG_ONE_SHOT);
                    alarmManager.cancel(pendingIntent);
                    AlarmManagerCompat.setAlarmClock(alarmManager, System.currentTimeMillis() + 10 * 1000,
                            pendingIntent, pendingIntent);
                }
            }
        });


        final Button hookWakelock = (Button) findViewById(R.id.hook_wakelock);
        hookWakelock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
                if (null != powerManager) {
                    new ProxyHook(powerManager, "mService", new ProxyHook.InvokeBeforeListener() {
                        @Override
                        public void beforeInvoke(Method method, Object[] args) {
                            // 申请 Wakelock
                            if (method.getName().equals("acquireWakeLock")) {
                                if (AppUtils.isAppBackground(SampleApplication.getCtx())) {
                                    // 应用后台逻辑，获取应用堆栈等等
                                    Log.d("Hoook", "acquireWakeLock background", new Throwable());
                                } else {
                                    // 应用前台逻辑，获取应用堆栈等等
                                    Log.d("Hoook", "acquireWakeLock", new Throwable());
                                }
                                // 释放 Wakelock
                            } else if (method.getName().equals("releaseWakeLock")) {
                                // 释放的逻辑
                                Log.d("Hoook", "releaseWakeLock", new Throwable());
                            }
                        }
                    });
                    PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, MainActivity.class.getName());
                    wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
                    wakeLock.release();
                }

            }
        });

        final Button hookGPS = (Button) findViewById(R.id.hook_gps);
        hookGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (null != locationManager) {
                    new ProxyHook(locationManager, "mService", new ProxyHook.InvokeBeforeListener() {
                        @Override
                        public void beforeInvoke(Method method, Object[] args) {
                            // 请求一次定位
                            if (method.getName().equals("requestLocationUpdates")) {
                                // 不同版本参数类型的适配，获取应用堆栈等等
                                Log.d("Hoook", "requestLocationUpdates", new Throwable());
                                // 清除 定位请求
                            } else if (method.getName().equals("removeUpdates")) {
                                // 清除的逻辑
                                Log.d("Hoook", "Location removeUpdates", new Throwable());
                            }
                        }
                    });
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Criteria criteria = new Criteria();
                        final HandlerThread handlerThread = new HandlerThread("locationThread");
                        handlerThread.start();
                        LocationListener listener = new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                Log.d("Hoook", location.toString());
                                handlerThread.quit();
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(String provider) {

                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                                handlerThread.quit();
                                locationManager.removeUpdates(this);
                            }
                        };
                        locationManager.requestSingleUpdate(criteria, listener, handlerThread.getLooper());
                    }

                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("Hoook", "授权成功！");
    }
}
