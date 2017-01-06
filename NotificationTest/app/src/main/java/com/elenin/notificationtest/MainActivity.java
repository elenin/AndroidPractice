package com.elenin.notificationtest;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button sendNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendNotice = (Button) findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationManager manager = (NotificationManager)
                                getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder  builder = new Notification(this);
                Intent intent = new Intent(this, NotificationActivity.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setTicker("This is ticker text");
                builder.setWhen(System.currentTimeMillis());
                builder.setContentTitle("This is content title");
                builder.setContentText("This is content");
                builder.setContentIntent(pi);
                Notification notification = builder.getNotification();
                manager.notify(1, notification);

                 /*        Notification notification = new Notification(R.drawable.ic_launcher,
                                "This is ticker text", System.currentTimeMillis());
                        Intent intent = new Intent(this, NotificationActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(this, 0, intent,
                                     PendingIntent.FLAG_CANCEL_CURRENT);
                        notification.setLatesEventInfo(this, "This is content title",
                                    "This is content text", pi);
                        manager.notify(1, notification);
                      /*  Notification notification = new Notification.Builder(Context).
                                setSmallIcon(R.mipmap.ic_launcher).
                                setTicker("This is ticker text").
                                setWhen(System.currentTimeMillis()).
                                setContentTitle("This is content title").
                                setContentText("This is content").
                                setContentIntent(pi).
                                build(); */

                }
            }
        );
    }


}
