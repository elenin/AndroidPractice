package com.example.cvte.demotvsetting.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cvte.tv.api.TvApiApplication;
import com.cvte.tv.api.TvServiceConnectListener;
import com.cvte.tv.api.aidl.EntityInputSource;
import com.cvte.tv.api.aidl.ITVApiSystemInputSourceAidl;
import com.cvte.tv.api.aidl.ITvApiManager;
import com.cvte.tv.api.aidl.NotifyListener;
import com.example.cvte.demotvsetting.R;

import java.util.ArrayList;
import java.util.List;

public class InputsourceView extends RelativeLayout {


    private ITVApiSystemInputSourceAidl api;
    private List<EntityInputSource> srcList;
    private EntityInputSource currentSrc;
    private ListView srcListView;

    /**
     * 添加Notify监听器.
     */
    NotifyListener listener = new NotifyListener() {
        @Override
        public void onTvNotify(String s, Bundle bundle) {
            if(s.equals("notifySystemInputSourceChange")){
                //当底层通道改变时.会发出此条NOTIFY.切换能道可能是由其它APP触发的.但是只要通道有切换.所有的APP都能收到此条NOTIFY.各APP可以自行添加当通道切换时需要执行的动作.
            }
        }
    };
    public InputsourceView(Context context) {
        this(context, null);
    }

    public InputsourceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputsourceView(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layout_item_inputsource, this, true);

        srcListView = (ListView) findViewById(R.id.listSource);


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
                    api = iTvApiManager.getTVApiSystemInputSource();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                /**
                 * 判断平台是否有实现接口
                 */
                if (api != null) {
                    try {
                        srcList = api.eventSystemInputSourceGetList();
                        currentSrc = api.eventSystemInputSourceGetInputSource();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    ArrayAdapter<String> listAdapter;
                    List<String> srcStringList;
                    srcStringList = new ArrayList<String>();

                    int currentSelect = 0;
                    /**
                     * 构造出列表控件显示以及当前选中通道
                     */
                    if (srcList != null) {
                        for (int i = 0; i < srcList.size(); i++) {
                            srcStringList.add(srcList.get(i).name);

                            if (srcList.get(i).id == currentSrc.id) {
                                currentSelect = i;
                            }
                        }
                    }
                    listAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, srcStringList);
                    srcListView.setAdapter(listAdapter);
                    srcListView.setSelection(currentSelect);
                    srcListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            /**
                             * 选择通道事件调用TV-API接口设通道
                             */
                            try {
                                if (api.eventSystemInputSourceSetInputSource(srcList.get(position).id) == true) {
                                    //设置成功
                                } else {
                                    //设置失败
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
}
