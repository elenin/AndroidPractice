package com.example.cvte.demotvsetting.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.cvte.demotvsetting.R;


/**
 * Created by liyang on 2014/8/28.
 */
public class ChannelView extends RelativeLayout {
    Spinner mSleeptimeSpinner;
    public ChannelView(Context context) {
        this(context, null);
    }

    public ChannelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChannelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layout_item_channel, this, true);
        mSleeptimeSpinner = (Spinner) findViewById(R.id.spinnersleeptime);
        String[] mItems = new String[]{"30", "60", "90", "180"};
        ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, mItems);
        mSleeptimeSpinner.setAdapter(_Adapter);
        mSleeptimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String str = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }
}
