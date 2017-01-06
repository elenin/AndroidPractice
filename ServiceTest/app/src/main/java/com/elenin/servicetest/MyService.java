package com.elenin.servicetest;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by ele on 2016/12/22.
 */

public class MyService extends Service {

    private DownloadBinder mBinder = new DownloadBinder();

    class DownloadBinder extends Binder {

        public void startDownload() {
            Log.d("MyService", "startDownload executed");
        }

        public int getProgress() {
            Log.d("MyService", "getProgress executed");
            return 0;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //Notification notification = new Notification(R.mipmap.ic_launcher,
         //       "Notification comes", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
         //       notificationIntent, 0);
        //notification.setLatestEventInfo(this, "This is title", "This is content", pendingIntent);
        //startForeground(1, notification);
        Log.d("MyService", "onCreate executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyService", "onStartCommand executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyService", "onDestroy executed");
    }
}
