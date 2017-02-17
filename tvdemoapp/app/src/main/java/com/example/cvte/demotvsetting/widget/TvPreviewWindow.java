package com.example.cvte.demotvsetting.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.cvte.tv.api.TvApiApplication;
import com.cvte.tv.api.TvServiceConnectListener;
import com.cvte.tv.api.aidl.ITVApiScreenWindowAidl;
import com.cvte.tv.api.aidl.ITvApiManager;
import com.example.cvte.demotvsetting.R;
import com.example.cvte.demotvsetting.effect.CardSizeConfig;


/**
 * Created by liyang on 2014/9/1.
 *
 * @功能 TV小窗口
 */
public class TvPreviewWindow extends FrameLayout {

    public TvSurfaceView getSurfaceView() {
        return mSurfaceView;
    }
    final String TAG = "TvPreviewWindow";
    //view item var
    RelativeLayout backgroundPreWin;
    TvSurfaceView mSurfaceView;
    Button muteButton;
    Button dismuteButton;

    private ITVApiScreenWindowAidl api;

    public TvPreviewWindow(Context context) {
        this(context, null);
    }

    public TvPreviewWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvPreviewWindow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TvApiApplication.getTvApi(new TvServiceConnectListener() {
            @Override
            public void OnConnected(ITvApiManager iTvApiManager) {
                try {
                    api = iTvApiManager.getTVApiScreenWindow();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        LayoutInflater.from(context).inflate(R.layout.layout_tv_mini_window, this, true);
        backgroundPreWin = (RelativeLayout) findViewById(R.id.preview_win);

        muteButton = (Button) findViewById(R.id.mutebutton);
        muteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mute window");
                try {
                    api.eventScreenWindowMute(true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        dismuteButton = (Button) findViewById(R.id.dismutebutton);
        dismuteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "dismute window");
                try {
                    api.eventScreenWindowMute(false);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        mSurfaceView = (TvSurfaceView) findViewById(R.id.tv_mini_window_id);
        RelativeLayout.LayoutParams miniparams = (RelativeLayout.LayoutParams) mSurfaceView.getLayoutParams();
        miniparams.height = CardSizeConfig.getPREVIEW_WINDOW_INNER_HEIGHT();
        miniparams.width = CardSizeConfig.getPREVIEW_WINDOW_INNER_WIDTH();
        mSurfaceView.setLayoutParams(miniparams);
    }

    /**
     * 缩放TV小窗口
     *
     * @param left
     * @param top
     * @param width
     * @param height
     * @param scale
     */
    public void scaleTvWindow(int left, int top, int width, int height, float scale) {
        int offsetX = (int) ((width * scale - width) / 2);
        int offsetY = (int) ((height * scale - height) / 2);
        left -= offsetX;
        top -= offsetY;
        Log.d(TAG, "left=" + left + ",top=" + top + ",width=" + width + ",height=" + height + ",scale=" + scale);
        try {
            width = (int) (width * scale);
            height = (int) (height * scale);
            if (api == null)
                return;
            api.eventScreenWindowSetPipValue(left, top, width, height);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow");
        super.onDetachedFromWindow();
        try {
            api.eventScreenWindowSetFull();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow");
        super.onAttachedToWindow();
        int[] location = new int[2];
        mSurfaceView.getLocationOnScreen(location);
        scaleTvWindow(location[0], location[1], mSurfaceView.getWidth(), mSurfaceView.getHeight(), 1.0F);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if(gainFocus){
            backgroundPreWin.requestFocus();
        }
    }
}
