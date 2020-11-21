package com.oss_net.choloeksathe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.entity.databases.remote_model.DriverHistory;
import com.oss_net.choloeksathe.utils.CommonTask;

import java.util.List;
import java.util.Locale;

/**
 * Created by root on 2/12/18.
 */

public class DriverHistoryAdapter extends RecyclerView.Adapter<DriverHistoryAdapter.DriverHistoryViewHolder> {

    List<DriverHistory> driverHistories;
    Context context;

    public DriverHistoryAdapter(List<DriverHistory> driverHistories, Context context) {
        this.driverHistories = driverHistories;
        this.context = context;
    }

    @Override
    public DriverHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new DriverHistoryAdapter.DriverHistoryViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.adapter_driver_history,
                parent, false));

    }

    @Override
    public void onBindViewHolder(DriverHistoryViewHolder holder, int position) {

        try {
            DriverHistory driverHistory = driverHistories.get(position);
            holder.requestID.setText(String.format(Locale.ENGLISH, "RequestID : %d",
                    driverHistory.requestId));

            holder.driver_journeyDate.setText(String.format(Locale.ENGLISH, "Date %s", driverHistory.requestDate));
            holder.driver_sourcePoint.setText(driverHistory.shortStartPlace);
            holder.driver_destinationPoint.setText(driverHistory.shortEndPlace);


        } catch (Exception ex) {
            CommonTask.showErrorLog(ex.getMessage());
        }

    }


    @Override
    public int getItemCount() {
        return driverHistories.size();
    }


    public class DriverHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView requestID, amount, driver_journeyDate,
                driver_sourcePoint, driver_destinationPoint;

        public DriverHistoryViewHolder(View itemView) {
            super(itemView);
            requestID = itemView.findViewById(R.id.requestId);
            amount = itemView.findViewById(R.id.amount);
            driver_journeyDate = itemView.findViewById(R.id.driver_journeyDate);
            driver_sourcePoint = itemView.findViewById(R.id.driver_sourcePoint);
            driver_destinationPoint = itemView.findViewById(R.id.driver_destinationPoint);

        }
    }
}
