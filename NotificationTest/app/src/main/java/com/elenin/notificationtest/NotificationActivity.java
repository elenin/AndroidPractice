package com.elenin.notificationtest;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;

/**
 * Created by ele on 2016/12/21.
 */

public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        NotificationManager manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(1);
    }
}
