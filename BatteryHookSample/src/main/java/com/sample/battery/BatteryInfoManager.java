package com.example.dymproxy;


import java.util.ArrayList;
import java.util.List;

public class BatteryInfoManager {

    private static volatile BatteryInfoManager sBatteryInfoManager;

    private List<BatteryInfo> mBatteryInfos;

    private BatteryInfoManager() {
        mBatteryInfos = new ArrayList<>();
    }

    public static BatteryInfoManager getInstance() {
        if (sBatteryInfoManager == null) {
            synchronized (BatteryInfoManager.class) {
                if (sBatteryInfoManager == null) {
                    sBatteryInfoManager = new BatteryInfoManager();
                }
            }
        }
        return sBatteryInfoManager;

    }
    public void addBatteryInfo(BatteryInfo batteryInfo){
        mBatteryInfos.add(batteryInfo);
    }
}
