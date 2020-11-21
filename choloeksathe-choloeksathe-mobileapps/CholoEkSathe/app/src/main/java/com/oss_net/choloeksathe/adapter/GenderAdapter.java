package com.oss_net.choloeksathe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oss_net.choloeksathe.R;

/**
 * Created by mahbubhasan on 12/7/17.
 */

public class GenderAdapter extends BaseAdapter{
    String[] genders;
    Context context;

    public GenderAdapter(String[] genders, Context context) {
        this.genders = genders;
        this.context = context;
    }

    @Override
    public int getCount() {
        return genders.length;
    }

    @Override
    public String getItem(int i) {
        return genders[i];
    }

    @Override
    public long getItemId(int i) {
        return genders[i].hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.adapter_gender, null);
        TextView textView = view.findViewById(R.id.tvAdapterGender);
        textView.setText(genders[i]);
        return view;
    }
}
