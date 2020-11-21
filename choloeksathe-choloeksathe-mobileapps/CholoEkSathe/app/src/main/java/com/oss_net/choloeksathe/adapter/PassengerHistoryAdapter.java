package com.oss_net.choloeksathe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.remote_model.PassengerHistory;
import com.oss_net.choloeksathe.utils.CircularImageView;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mahbubhasan on 12/26/17.
 */

public class PassengerHistoryAdapter extends RecyclerView.Adapter<PassengerHistoryAdapter.PassengerHistoryViewHolder>{

    List<PassengerHistory> passengerHistories;
    Context context;

    public PassengerHistoryAdapter(List<PassengerHistory> passengerHistories, Context context) {
        this.passengerHistories = passengerHistories;
        this.context = context;
    }

    @Override
    public PassengerHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PassengerHistoryViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.adapter_passenger_history,
                parent, false));
    }

    @Override
    public void onBindViewHolder(PassengerHistoryViewHolder holder, int position) {
        try {
            PassengerHistory passengerHistory = passengerHistories.get(position);
            holder.interestNumber.setText(String.format(Locale.ENGLISH,"Interest# %d",passengerHistory.activityId));
            holder.price.setText(String.format(Locale.ENGLISH, "BDT %.2f",passengerHistory.price));
            holder.journeyDate.setText(DateUtils.getRelativeTimeSpanString(passengerHistory.startTime, System.currentTimeMillis(), 0));
            holder.sourcePoint.setText(passengerHistory.startLocationName);
            holder.destinationPoint.setText(passengerHistory.stopLocationName);
            holder.driverName.setText(passengerHistory.driverFullName);
            holder.driverRating.setText(""+passengerHistory.totalRating+"/5");
            if(passengerHistory.driverPicture!= null){
                if(getItemCount() != 0)
                    Glide.with(context).load(CommonTask.getGlideUrl(passengerHistory.driverPicture, context)).into(holder.driverImage);
            }else {
                Glide.with(context).load(CommonURL.getInstance().emptyPictureUrl).into(holder.driverImage);
            }

        }catch (Exception ex){
            CommonTask.showErrorLog(ex.getMessage());
        }
    }

    @Override
    public int getItemCount() {

        return passengerHistories.size();
    }

    class PassengerHistoryViewHolder extends RecyclerView.ViewHolder{

        TextView interestNumber, price, journeyDate, sourcePoint, destinationPoint, driverName,driverRating;
        private CircleImageView driverImage;

        PassengerHistoryViewHolder(View itemView) {
            super(itemView);

            interestNumber = itemView.findViewById(R.id.interestNumber);
            price = itemView.findViewById(R.id.price);
            journeyDate = itemView.findViewById(R.id.journeyDate);
            sourcePoint = itemView.findViewById(R.id.sourcePoint);
            destinationPoint = itemView.findViewById(R.id.destinationPoint);
            driverName = itemView.findViewById(R.id.driverName);
            driverImage = itemView.findViewById(R.id.driverPicture);
            driverRating = itemView.findViewById(R.id.rating);
        }
    }
}
