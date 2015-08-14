package com.example.user.beaconapplication;

import android.app.Application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by user on 2015/8/14.
 */
public class BeaconApplication extends Application implements BeaconConsumer {
    private static final String TAG = BeaconApplication.class.getSimpleName();
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot = false;
    private Context nowCurrentForegroundActivity=null;
    private BeaconManager beaconManager;



    @Override
    public void onTerminate() {
        beaconManager.unbind(this);
        super.onTerminate();
    }

    public void onCreate() {
        super.onCreate();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
        beaconManager.setBackgroundBetweenScanPeriod(10000);
        Log.d(TAG, "setting up background monitoring for  power saving");
        // wake up the app when a beacon is seen
        backgroundPowerSaver = new BackgroundPowerSaver(this);

    }



    public void setMonitoringActivity(Context context) {
        this.nowCurrentForegroundActivity = context;
    }

    @Override
    public void onBeaconServiceConnect() {
       beaconManager.setRangeNotifier(new RangeNotifier() {
           @Override
           public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
              if(beacons.size()>0) //有抓到beacon
               {
                   Iterator<Beacon> iterator=beacons.iterator(); //走訪beacon collection\
                   Beacon nearstBeacon=beacons.iterator().next();// 預設第一個為 為距離最近的beacon
                   Double nearstDistance=nearstBeacon.getDistance(); //預設最近距離 為第一個beacon的距離
                    while(iterator.hasNext()) //走訪beacon 的collection
                    {
                        Beacon next=iterator.next();
                        int result=compareDistance(nearstDistance,next.getDistance());
                        switch (result)
                        {
                            case 0: //距離一樣
                                    break;
                            case 1://next較近
                                nearstBeacon=next;
                                nearstDistance=next.getDistance();
                                    break;
                            case 2://原來較近
                                    break;

                        }
                    }
                   
                   CheckBeaconId(nearstBeacon.getId2().toInt());


               }

           }
       });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }


    }

// 自訂判斷距離的function
private int compareDistance(double currentNearst,double next) {
    int result = 0;
    BigDecimal d_c = new BigDecimal(currentNearst);
    BigDecimal d_n = new BigDecimal(next);
    if (d_c.compareTo(d_n) == 0) //下個beacon與目前的beacon距離相等
    {
        result = 0;
    } else if (d_c.compareTo(d_n) > 0)  //下個beacon與目前最近的beacon  相比  下個beacon的距離較近
    {
        result = 1;
    } else if (d_c.compareTo(d_n) < 0)  //目前beacon的距離仍為最短
    {
        result = 2;
    }

    return result;
}
//依據beaconid 傳入對應的參數
 private void CheckBeaconId(int bid)
 {
     Intent intent = new Intent();
     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     intent.setClass(this, patient.class);
     Bundle bundle = new Bundle();
     switch (bid)
     {
         case 1:
             bundle.putString("bid", "1");//傳遞Double
             intent.putExtras(bundle);
             startActivity(intent);
             break;
         case 3:
             bundle.putString("bid", "3");//傳遞Double
             intent.putExtras(bundle);
             startActivity(intent);
             break;
     }

 }

}
