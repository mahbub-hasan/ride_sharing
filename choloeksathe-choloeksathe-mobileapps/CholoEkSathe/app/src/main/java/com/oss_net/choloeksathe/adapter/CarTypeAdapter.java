package com.oss_net.choloeksathe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarTypeList;

import java.util.List;

/**
 * Created by mahbubhasan on 3/19/18.
 */

public class CarTypeAdapter  extends BaseAdapter{
    private List<CarTypeList> carTypeListList;
    private Context context;

    public CarTypeAdapter(List<CarTypeList> carTypeListList, Context context) {
        this.carTypeListList = carTypeListList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return carTypeListList.size();
    }

    @Override
    public CarTypeList getItem(int i) {
        return carTypeListList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return carTypeListList.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.adapter_journey_type, null);
        TextView textView = view.findViewById(R.id.tvJourneyType);
        textView.setText(carTypeListList.get(i).carTypeName);
        return view;
    }
}
