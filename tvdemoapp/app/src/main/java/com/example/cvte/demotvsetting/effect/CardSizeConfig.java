package com.example.cvte.demotvsetting.effect;

import android.content.Context;


/**
 * Created by User on 2014/9/28.
 */
public class CardSizeConfig {
    private static Context mContext;
    public static void init(Context context){
        mContext = context;
    }

    /**
     * preview card 原大小 722*420
     */
    private static int PREVIEW_WINDOW_INNER_WIDTH = 722;//R.dimen.p722;
    private static int PREVIEW_WINDOW_INNER_HEIGHT = 420;//R.dimen.p420;
    public static int getPREVIEW_WINDOW_INNER_WIDTH(){
        return PREVIEW_WINDOW_INNER_WIDTH;
    }
    public static int getPREVIEW_WINDOW_INNER_HEIGHT(){
        return PREVIEW_WINDOW_INNER_HEIGHT;
    }

}
