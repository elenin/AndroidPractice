package com.elenin.smstest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ObbInfo;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends Activity {

    private TextView sender;

    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sender = (TextView) findViewById(R.id.sender);
        content = (TextView) findViewById(R.id.content);
    }

    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[]) bundle.get("pdus");
            String format = intent.getStringExtra("format");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < messages.length; i++) {
                byte[] sms = (byte[])pdus[i];
                messages[i] = SmsMessage.createFromPdu(sms, format);
            }
            String address = messages[0].getOriginatingAddress();
            String fullMessage = "";
            for (SmsMessage message : messages) {
                fullMessage += message.getMessageBody();
            }
            sender.setText(address);
            content.setText(fullMessage);
        }
    }
}
