package com.oss_net.choloeksathe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.entity.databases.RequestType;
import com.oss_net.choloeksathe.entity.databases.remote_model.CompanyList;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by mahbubhasan on 1/10/18.
 */

public class CompanySpinnerAdapter extends BaseAdapter {

    private List<CompanyList> companyLists;
    private Context context;

    public CompanySpinnerAdapter(List<CompanyList> companyLists, Context context) {
        this.companyLists = companyLists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return companyLists.size();
    }

    @Override
    public CompanyList getItem(int i) {
        return companyLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return companyLists.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.adapter_request_type, null);
        TextView textView = view.findViewById(R.id.tvRequestType);
        textView.setText(companyLists.get(i).companyName);
        return view;
    }
}

