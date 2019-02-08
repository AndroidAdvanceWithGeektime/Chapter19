package com.sample.battery;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sample.battery.alarm.IAlarmManagerHook;
import com.sample.battery.alarm.MyAlarmReceiver;
import com.sample.battery.gps.ILocationManagerHook;
import com.sample.battery.wakelock.IPowerManagerHook;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends Activity {
    public static Context sContext;

    private PowerManager.WakeLock wakeLock;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sContext = getApplicationContext();
        final Button hookAlarm = (Button) findViewById(R.id.hook_alarm);
        hookAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IAlarmManagerHook(sContext).onInstall();
            }
        });


        final Button hookWakelock = (Button) findViewById(R.id.hook_wakelock);
        hookWakelock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IPowerManagerHook(sContext).onInstall();
            }
        });

        final Button hookGPS = (Button) findViewById(R.id.hook_gps);
        hookGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ILocationManagerHook(sContext).onInstall();
            }
        });

        final Button wakelockAcquire = (Button) findViewById(R.id.wakelock_acquire);
        wakelockAcquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PowerManager powerManager = (PowerManager) sContext.getSystemService(POWER_SERVICE);
                wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getName());//持有唤醒锁
                wakeLock.setReferenceCounted(false);
                wakeLock.acquire(60 * 1000); //亮屏60s

            }
        });

        final Button wakelockRelease = (Button) findViewById(R.id.wakelock_release);
        wakelockRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wakeLock != null){
                    wakeLock.release();//释放锁，灭屏
                }
            }
        });

        final Button alarmSet = (Button) findViewById(R.id.alarm_set);
        alarmSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmService = (AlarmManager) sContext.getSystemService(ALARM_SERVICE);
                Intent alarmIntent = new Intent(sContext, MyAlarmReceiver.class).setAction("intent_alarm");
                PendingIntent broadcast = PendingIntent.getBroadcast(sContext, 0, alarmIntent, 0);//通过广播接收
                alarmService.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);//INTERVAL毫秒后触
            }
        });

        final Button alarmCancel = (Button) findViewById(R.id.alarm_cancel);
        alarmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmService = (AlarmManager) sContext.getSystemService(ALARM_SERVICE);
                Intent alarmIntent = new Intent(sContext, MyAlarmReceiver.class).setAction("intent_alarm");
                PendingIntent broadcast = PendingIntent.getBroadcast(sContext, 0, alarmIntent, 0);
                alarmService.cancel(broadcast);
            }
        });

        final Button gpsRequest = (Button) findViewById(R.id.gps_request);
        gpsRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{ACCESS_FINE_LOCATION}, 100);
                } else {
                    locationManager = (LocationManager) sContext.getSystemService(LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, locationListener);
                }

            }
        });

        final Button gpsRemove = (Button) findViewById(R.id.gps_remove);
        gpsRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationManager != null && locationListener != null) {
                    // 关闭程序时将监听器移除
                    locationManager.removeUpdates(locationListener);
                }
            }
        });

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e("HOOOOOOOOK", location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
