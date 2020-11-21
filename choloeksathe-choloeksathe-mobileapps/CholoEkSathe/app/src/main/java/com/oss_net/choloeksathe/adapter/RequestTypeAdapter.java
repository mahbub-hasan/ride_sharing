package com.oss_net.choloeksathe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.entity.databases.RequestType;

import io.realm.RealmResults;

/**
 * Created by mahbubhasan on 12/8/17.
 */

public class RequestTypeAdapter extends BaseAdapter {

    private RealmResults<RequestType> requestTypes;
    private Context context;

    public RequestTypeAdapter(Context context, RealmResults<RequestType> requestTypes){
        this.requestTypes = requestTypes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return requestTypes.size();
    }

    @Override
    public RequestType getItem(int i) {
        return requestTypes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return requestTypes.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.adapter_request_type, null);
        TextView textView = view.findViewById(R.id.tvRequestType);
        textView.setText(requestTypes.get(i).getRequestType());
        return view;
    }
}
