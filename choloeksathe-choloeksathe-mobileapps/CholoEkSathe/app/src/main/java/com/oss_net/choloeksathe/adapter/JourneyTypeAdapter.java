package com.oss_net.choloeksathe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.entity.databases.JourneyType;

import io.realm.RealmResults;

/**
 * Created by mahbubhasan on 12/8/17.
 */

public class JourneyTypeAdapter extends BaseAdapter {

    private RealmResults<JourneyType> journeyTypes;
    private Context context;

    public JourneyTypeAdapter(Context context, RealmResults<JourneyType> journeyTypes){
        this.journeyTypes = journeyTypes;
        this.context = context;
    }


    @Override
    public int getCount() {
        return journeyTypes.size();
    }

    @Override
    public JourneyType getItem(int i) {
        return journeyTypes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return journeyTypes.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.adapter_journey_type, null);
        TextView textView = view.findViewById(R.id.tvJourneyType);
        textView.setText(journeyTypes.get(i).getJourneyType());
        return view;
    }
}

