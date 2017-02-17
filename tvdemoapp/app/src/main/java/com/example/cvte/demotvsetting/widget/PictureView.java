package com.example.cvte.demotvsetting.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
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
import com.cvte.tv.api.aidl.EnumPictureControlItem;
import com.cvte.tv.api.aidl.ITVApiPictureControlAidl;
import com.cvte.tv.api.aidl.ITvApiManager;
import com.cvte.tv.api.aidl.NotifyListener;
import com.example.cvte.demotvsetting.R;

import java.util.List;


/**
 * Created by liyang on 2014/8/28.
 */
public class PictureView extends RelativeLayout implements SeekBar.OnSeekBarChangeListener {

    Spinner mPModeSpinner;
    SeekBar seekBarContrast;
    SeekBar seekBarBrightness;
    SeekBar seekBarTint;
    SeekBar seekBarColor;
    SeekBar seekBarSharpness;

    private ITVApiPictureControlAidl api;

    /**
     * TV-API NOTIFY LISTENER
     * 用于接收底层发上来的NOTIFY消息。随着当前类的生命周期而消毁。若需手动消毁可调用listener.finalize();或listener=null;
     */
    private NotifyListener listener = new NotifyListener() {
        @Override
        public void onTvNotify(String s, Bundle bundle) {

            if (s.equals("notifyPictureControlChange")) {

                //刷新MODE SPINNER值。底层有可以调动数值后就变成USER MODE了。
                try {

                    List<EnumModeOptionId> pModeList = api.eventGetPictureModeOptions();
                    EnumModeOptionId currentMode = api.eventGetCurrentPictureMode();

                    for (int i = 0; i < pModeList.size(); i++) {
                        if (currentMode == pModeList.get(i)) {
                            //刷新PMODE的选项。
                            mPModeSpinner.setSelection(i);
                            Log.d("Demo", "OnNotify CurrentMode=" + currentMode + "Index=" + i);
                            break;
                        }
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                //收到PICTURE MODE更新的NOTIFY。需要刷新各子条目的状态。
                try {
                    SeekBar tmpSeekBar = seekBarBrightness;
                    for (int i = 0; i < EnumPictureControlItem.values().length; i++) {

                        switch (EnumPictureControlItem.values()[i]) {
                            case PICTURE_BRIGHTNESS:
                                tmpSeekBar = seekBarBrightness;
                                break;
                            case PICTURE_CONTRAST:
                                tmpSeekBar = seekBarContrast;
                                break;
                            case PICTURE_COLOR:
                                tmpSeekBar = seekBarColor;
                                break;
                            case PICTURE_SHARPNESS:
                                tmpSeekBar = seekBarSharpness;
                                break;
                            case PICTURE_TINT:
                                tmpSeekBar = seekBarTint;
                                break;
                        }
                        //刷新UI显示的各条目的数值
                        tmpSeekBar.setProgress(api.eventPictureControlGetValue(EnumPictureControlItem.values()[i]));

                        //刷新各条目的状态。
                        switch (api.eventPictureControlGetItemStatus(EnumPictureControlItem.values()[i])) {
                            case ITEM_STATUS_HIDE:
                                tmpSeekBar.setVisibility(GONE);
                                break;
                            case ITEM_STATUS_DISABLE:
                                tmpSeekBar.setVisibility(VISIBLE);//标准控件没有灰掉的属性。只用VISABLE替代。
                                break;
                            case ITEM_STATUS_ENABLE:
                                tmpSeekBar.setVisibility(VISIBLE);
                                break;
                        }

                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public PictureView(Context context) {
        this(context, null);
    }

    public PictureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PictureView(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layout_item_picture, this, true);
        mPModeSpinner = (Spinner) findViewById(R.id.spinnerPmode);
        seekBarContrast = (SeekBar) findViewById(R.id.seekBarContrast);
        seekBarBrightness = (SeekBar) findViewById(R.id.seekBarBrightness);
        seekBarTint = (SeekBar) findViewById(R.id.seekBarTint);
        seekBarColor = (SeekBar) findViewById(R.id.seekBarColor);
        seekBarSharpness = (SeekBar) findViewById(R.id.seekBarSharpness);
//
//

        /**
         * 获取TVAPI接口
         */
        TvApiApplication.getTvApi(new TvServiceConnectListener() {
                    @Override
                    public void OnConnected(ITvApiManager iTvApiManager) {

                        /**
                         * 获取并记录API接口对像。
                         */
                        try {
                            api = iTvApiManager.getTVApiPictureControl();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        if (api == null) {
                            /**
                             * API == NULL 表示为底层接口ITVApiPictureControl没有实现。所以要隐藏掉这个接口实现的相关条目
                             */
                            mPModeSpinner.setVisibility(GONE);
                            seekBarContrast.setVisibility(GONE);
                            seekBarBrightness.setVisibility(GONE);
                            seekBarTint.setVisibility(GONE);
                            seekBarColor.setVisibility(GONE);
                            seekBarSharpness.setVisibility(GONE);
                        } else {
                            /**
                             * PICTURE MODE.
                             */
                            List<EnumModeOptionId> pModeList = null;
                            EnumModeOptionId currentMode = EnumModeOptionId.OPTION_PICTURE_MODE_STANDARD;
                            try {
                                pModeList = api.eventGetPictureModeOptions();
                                currentMode = api.eventGetCurrentPictureMode();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            if (pModeList == null) {
                                return;
                            }
                            String[] mItems = new String[pModeList.size()];
                            int currentSelectedIndex = 0;
                            for (int i = 0; i < pModeList.size(); i++) {
                                mItems[i] = pModeList.get(i).name();//DEMO直接用枚举名字为MODE NAME.实际请映射为字符翻译表.
                                if (currentMode == pModeList.get(i)) {
                                    currentSelectedIndex = i;
                                }
                            }
                            Log.d("Demo", "OnConnect CurrentMode=" + currentMode + "Index=" + currentSelectedIndex);
                            ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, mItems);
                            mPModeSpinner.setAdapter(_Adapter);
                            mPModeSpinner.setSelection(currentSelectedIndex);
                            final List<EnumModeOptionId> finalPModeList = pModeList;
                            mPModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    try {
                                        Log.d("Demo", "OnSelectPMode CurrentMode=" + finalPModeList.get(position) + "Index=" + position);

                                        if (api.eventSetCurrentPictureMode(finalPModeList.get(position)) == true) {
                                            //设置成功
                                            //设置PMODE接口后。底层会调用 notifyPictureControlChange 通知上层。上层接收到消息后通过调用 eventPictureControlGetItemStatus 函数获取PMODE子条目状态并按状态刷新UI。
                                        } else {
                                            //设置失败
                                        }
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // TODO Auto-generated method stub
                                }
                            });


                            /**
                             * Contrast调整
                             */
                            seekBarContrast.setOnSeekBarChangeListener(PictureView.this);

                            /**
                             * Brightness调整
                             */
                            seekBarBrightness.setOnSeekBarChangeListener(PictureView.this);

                            /**
                             * Tint调整
                             */
                            seekBarTint.setOnSeekBarChangeListener(PictureView.this);

                            /**
                             * Color调整
                             */
                            seekBarColor.setOnSeekBarChangeListener(PictureView.this);

                            /**
                             * Brightness调整
                             */
                            seekBarSharpness.setOnSeekBarChangeListener(PictureView.this);
                        }
                    }
                }

        );
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d("Demo", "OnProgressChange fromUser=" + fromUser);
        if (fromUser == true) {
            try {
                if (seekBar.equals(seekBarContrast)) {
                    if (api.eventPictureControlSetValue(EnumPictureControlItem.PICTURE_CONTRAST, progress)) {
                        //调用底层设置成功。
                    } else {
                        //调用底层设置失败。
                    }
                } else if (seekBar.equals(seekBarBrightness)) {
                    if (api.eventPictureControlSetValue(EnumPictureControlItem.PICTURE_BRIGHTNESS, progress)) {
                        //调用底层设置成功。
                    } else {
                        //调用底层设置失败。
                    }
                } else if (seekBar.equals(seekBarTint)) {
                    if (api.eventPictureControlSetValue(EnumPictureControlItem.PICTURE_TINT, progress)) {
                        //调用底层设置成功。
                    } else {
                        //调用底层设置失败。
                    }
                } else if (seekBar.equals(seekBarColor)) {
                    if (api.eventPictureControlSetValue(EnumPictureControlItem.PICTURE_COLOR, progress)) {
                        //调用底层设置成功。
                    } else {
                        //调用底层设置失败。
                    }
                } else if (seekBar.equals(seekBarSharpness)) {
                    if (api.eventPictureControlSetValue(EnumPictureControlItem.PICTURE_SHARPNESS, progress)) {
                        //调用底层设置成功。
                    } else {
                        //调用底层设置失败。
                    }
                }
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
}
