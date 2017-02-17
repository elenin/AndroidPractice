package com.example.cvte.demotvsetting.widget;

import android.content.Context;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.cvte.tv.api.TvApiApplication;
import com.cvte.tv.api.TvServiceConnectListener;
import com.cvte.tv.api.aidl.EnumModeOptionId;
import com.cvte.tv.api.aidl.ITVApiSoundEqAidl;
import com.cvte.tv.api.aidl.ITVApiSoundSpeakerOutAidl;
import com.cvte.tv.api.aidl.ITvApiManager;
import com.example.cvte.demotvsetting.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liyang on 2014/8/28.
 */
public class SoundView extends RelativeLayout {

    private ITVApiSoundSpeakerOutAidl mSpeakerApi;
    private ITVApiSoundEqAidl mSoundEqApi;
    private List<EnumModeOptionId> mSoundModeList;
    private int currentModeIndex;

    public SoundView(Context context) {
        this(context, null);
    }

    public SoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layout_item_sound, this, true);
        final SeekBar mVoiceProcessbar = (SeekBar) findViewById(R.id.processnoise);
        Spinner mSoundModeSpinner = (Spinner) findViewById(R.id.spinnersoundmode);

        final List<String> soundModeList = new ArrayList<String>();
        currentModeIndex = 0;

        TvApiApplication.getTvApi(new TvServiceConnectListener() {
            @Override
            public void OnConnected(ITvApiManager iTvApiManager) {
                try {
                    mSpeakerApi = iTvApiManager.getTVApiSoundSpeakerOut();
                    mSoundEqApi = iTvApiManager.getTVApiSoundEq();
                    if (mSpeakerApi != null) {
                        mVoiceProcessbar.setProgress(mSpeakerApi.eventSoundSpeakerOutGetVolume());
                    }
                    if (mSoundEqApi != null) {
                        mSoundModeList = mSoundEqApi.eventGetSoundModeOptions();
                        EnumModeOptionId current = mSoundEqApi.eventGetCurrentSoundMode();
                        currentModeIndex = mSoundModeList.indexOf(current);
                        for (EnumModeOptionId mode : mSoundModeList) {
                            soundModeList.add(mode.name());
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        //sound mode
        ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, soundModeList);
        mSoundModeSpinner.setAdapter(_Adapter);
        mSoundModeSpinner.setSelection(currentModeIndex);
        mSoundModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (mSoundEqApi != null && mSoundModeList != null && position < mSoundModeList.size()) {
                    try {
                        mSoundEqApi.eventSetCurrentSoundMode(mSoundModeList.get(position));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        //voice
        mVoiceProcessbar.setMax(100);
        mVoiceProcessbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mSpeakerApi != null) {
                    try {
                        mSpeakerApi.eventSoundSpeakerOutSetVolume(progress);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
