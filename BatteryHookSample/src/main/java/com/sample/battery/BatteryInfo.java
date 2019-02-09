package com.example.dymproxy;


import java.text.SimpleDateFormat;
import java.util.Date;

public class BatteryInfo {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
    private boolean isCharge;
    private String stack;
    private float batteryLevel;
    private String time;


    public BatteryInfo(boolean isCharge, String stack, float batteryLevel) {
        this.isCharge = isCharge;
        this.stack = stack;
        this.batteryLevel = batteryLevel;
        this.time = dateFormat.format(new Date());
    }


    @Override
    public String toString() {
        return "{" +
                "batteryLevel" + ":" + batteryLevel + "," +
                "isCharge" + ":" + isCharge + "," +
                "time" + ":" + time + "," +
                "stacktrace" + ":" + stack +
                "}"
                ;
    }
}
