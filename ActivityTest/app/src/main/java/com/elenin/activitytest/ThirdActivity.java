package com.elenin.activitytest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ele on 2016/10/20.
 */

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.third_layout);
    }
}
