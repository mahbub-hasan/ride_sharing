package com.oss_net.choloeksathe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.entity.databases.RequestStatusInfo;

import io.realm.RealmResults;

/**
 * Created by mahbubhasan on 12/8/17.
 */

public class RequestStatusAdapter extends BaseAdapter {

    private RealmResults<RequestStatusInfo> requestStatusInfos;
    private Context context;

    public RequestStatusAdapter(Context context, RealmResults<RequestStatusInfo> requestStatusInfos){
        this.requestStatusInfos = requestStatusInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return requestStatusInfos.size();
    }

    @Override
    public RequestStatusInfo getItem(int i) {
        return requestStatusInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return requestStatusInfos.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.adapter_request_status, null);
        TextView textView = view.findViewById(R.id.tvRequestStatus);
        textView.setText(requestStatusInfos.get(i).getRequestStatus());
        return view;
    }
}
