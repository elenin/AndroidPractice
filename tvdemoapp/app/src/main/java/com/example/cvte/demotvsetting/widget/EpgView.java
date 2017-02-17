package com.example.cvte.demotvsetting.widget;

import android.content.Context;
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
import com.cvte.tv.api.aidl.EntityChannel;
import com.cvte.tv.api.aidl.EntityEpgEvent;
import com.cvte.tv.api.aidl.ITVApiDtvEpgAidl;
import com.cvte.tv.api.aidl.ITVApiSystemDateTimeAidl;
import com.cvte.tv.api.aidl.ITVApiTvChannelsAidl;
import com.cvte.tv.api.aidl.ITvApiManager;
import com.example.cvte.demotvsetting.R;

import java.util.ArrayList;
import java.util.List;

public class EpgView extends RelativeLayout {

    private ITVApiTvChannelsAidl mChannelApi;
    private ITVApiSystemDateTimeAidl mDateTimeApi;
    private ITVApiDtvEpgAidl mEpgApi;
    private List<EntityChannel> mChannelList;
    private ArrayAdapter<String> mEpgAdapter;
    private List<String> mEventNameList;

    public EpgView(Context context) {
        this(context, null);
    }

    public EpgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EpgView(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layout_item_epg, this, true);
        final ListView channelList = (ListView)findViewById(R.id.list_channels);
        final ListView eventList = (ListView)findViewById(R.id.list_events);

        mEventNameList = new ArrayList<String>();
        mEpgAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mEventNameList);
        eventList.setAdapter(mEpgAdapter);

        TvApiApplication.getTvApi(new TvServiceConnectListener() {
            @Override
            public void OnConnected(ITvApiManager iTvApiManager) {
                try {
                    mChannelApi = iTvApiManager.getTVApiTvChannels();
                    mDateTimeApi = iTvApiManager.getTVApiSystemDateTime();
                    mEpgApi = iTvApiManager.getTVApiDtvEpg();
                    if (mChannelApi != null) {
                        mChannelList = mChannelApi.eventTVChannelsGetChannelList(
                                mChannelApi.eventTVChannelsGetChannel().tvCategory);
                        if (mChannelList != null) {
                            String[] channelName = new String[mChannelList.size()];
                            for (int i = 0; i < mChannelList.size(); i++) {
                                channelName[i] = mChannelList.get(i).serviceName;
                            }
                            ArrayAdapter<String> channelAdapter = new ArrayAdapter<String>(
                                    context, android.R.layout.simple_list_item_1, channelName);
                            channelList.setAdapter(channelAdapter);
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        channelList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mEventNameList.clear();
                if (mEpgApi != null && mDateTimeApi != null && mChannelList != null && i < mChannelList.size()) {
                    try {
                        long time = mDateTimeApi.eventSystemDateTimeGetDateTime();
                        List<EntityEpgEvent> events = mEpgApi.eventDtvEpgGetEventList(
                                mChannelList.get(i).channelId, time, 24 * 60 * 60);
                        if (events != null) {
                            for (EntityEpgEvent event : events) {
                                mEventNameList.add(event.name);
                            }
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                mEpgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
