package com.example.cvte.demotvsetting.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by liyang on 2014/9/1.
 *
 * @功能: Tv小视窗视图
 */
public class TvSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    //view item vars
    SurfaceHolder mHolder;
    private int mWidth;
    private int mHeight;
    private boolean isStop = false;
    private Thread mTvThread = null;

    public TvSurfaceView(Context context) {
        this(context, null);
    }

    public TvSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mWidth = width;
        mHeight = height;
        mHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHolder = null;
    }
}
