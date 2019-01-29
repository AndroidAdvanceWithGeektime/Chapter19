package com.sample.battery;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.sample.battery.R;

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

            }
        });


        final Button hookWakelock = (Button) findViewById(R.id.hook_wakelock);
        hookWakelock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        final Button hookGPS = (Button) findViewById(R.id.hook_gps);
        hookGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
