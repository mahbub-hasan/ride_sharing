package com.oss_net.choloeksathe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.entity.databases.UserCategory;

import io.realm.RealmResults;

/**
 * Created by mahbubhasan on 12/7/17.
 */

public class UserCategoryAdapter extends BaseAdapter {

    private RealmResults<UserCategory> userCategories;
    private Context context;

    public UserCategoryAdapter(Context context, RealmResults<UserCategory> userCategories){
        this.userCategories = userCategories;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userCategories.size();
    }

    @Override
    public UserCategory getItem(int i) {
        return userCategories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return userCategories.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.adapter_user_category, null);
        TextView textView = view.findViewById(R.id.tvAdapterUserCategory);
        textView.setText(userCategories.get(i).getCategory());
        return view;
    }
}
