package com.oss_net.choloeksathe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.entity.databases.remote_model.CompanyList;

import java.util.List;

/**
 * Created by mahbubhasan on 1/10/18.
 */

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.CompanyListViewHolder>{

    private List<CompanyList> companyLists;
    private Context context;
    private onItemClick onItemClick;

    public CompanyListAdapter(List<CompanyList> companyLists, Context context) {
        this.companyLists = companyLists;
        this.context = context;
    }

    @Override
    public CompanyListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CompanyListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_company_list, parent, false));
    }

    @Override
    public void onBindViewHolder(CompanyListViewHolder holder, int position) {
        CompanyList companyList = companyLists.get(position);
        holder.companyName.setText(companyList.companyName);
    }

    @Override
    public int getItemCount() {
        return companyLists.size();
    }

    class CompanyListViewHolder extends RecyclerView.ViewHolder{

        CheckedTextView companyName;

        CompanyListViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.companyName);

            companyName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!companyName.isChecked()){
                        companyName.setChecked(true);
                        if(onItemClick != null)
                            onItemClick.setOnItemClick(getAdapterPosition());
                    }else{
                        companyName.setChecked(false);
                    }
                }
            });
        }
    }

    public interface onItemClick{
        void setOnItemClick(int position);
    }

    public void setOnItemClickListener(onItemClick onItemClickListener){
        this.onItemClick = onItemClickListener;
    }
}
