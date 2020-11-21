package com.oss_net.choloeksathe.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarShareList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mahbubhasan on 12/11/17.
 */

public class CarShareListAdapter extends RecyclerView.Adapter<CarShareListAdapter.CarShareListViewHolder>{

    private List<CarShareList> carShareLists;
    onItemClick onItemClick;

    public CarShareListAdapter(List<CarShareList> carShareLists) {
        this.carShareLists = carShareLists;
    }

    @Override
    public CarShareListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CarShareListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_car_share_list, parent, false));
    }

    @Override
    public void onBindViewHolder(CarShareListViewHolder holder, int position) {
        CarShareList carShareList = carShareLists.get(position);
        holder.requestId.setText(String.format("Request # %s", carShareList.requestId));
        SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy HH:mm");
        holder.requestDate.setText(format.format(new Date(carShareList.startTime)));
        //holder.requestDate.setText(DateUtils.getRelativeTimeSpanString(carShareList.startTime, new Date().getTime(), 0));
        holder.startLocation.setText(carShareList.sourceLocation);
        holder.stopLocation.setText(carShareList.destinationLocation);
        if(carShareList.requestStatus.equals("Cancelled")){
            holder.activeStatus.setText("Canceled");
            holder.cancelRequest.setVisibility(View.GONE);
            holder.journeyStart.setVisibility(View.GONE);
            holder.journeyStop.setVisibility(View.GONE);
        }else if(carShareList.requestStatus.equals("Submitted")){
            holder.activeStatus.setText("New");
            holder.cancelRequest.setVisibility(View.VISIBLE);
            holder.journeyStart.setVisibility(View.VISIBLE);
            holder.journeyStop.setVisibility(View.GONE);
        }else if(carShareList.requestStatus.equals("Ongoing")){
            holder.activeStatus.setText("Journey Start");
            holder.cancelRequest.setVisibility(View.GONE);
            holder.journeyStart.setVisibility(View.GONE);
            holder.journeyStop.setVisibility(View.VISIBLE);
        }else if(carShareList.requestStatus.equals("Completed")){
            holder.activeStatus.setText("Completed");
            holder.cancelRequest.setVisibility(View.GONE);
            holder.journeyStart.setVisibility(View.GONE);
            holder.journeyStop.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return carShareLists.size();
    }

    class CarShareListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView requestId, requestDate, startLocation, stopLocation, activeStatus;
        Button cancelRequest, journeyStart, journeyStop;

        CarShareListViewHolder(View itemView) {
            super(itemView);

            requestId = itemView.findViewById(R.id.requestId);
            requestDate = itemView.findViewById(R.id.requestDate);
            startLocation = itemView.findViewById(R.id.startLocation);
            stopLocation = itemView.findViewById(R.id.stopLocation);
            activeStatus = itemView.findViewById(R.id.activeStatus);
            cancelRequest = itemView.findViewById(R.id.driverCancelRequest);
            journeyStart = itemView.findViewById(R.id.driverJourneyStart);
            journeyStop = itemView.findViewById(R.id.driverJourneyStop);

            itemView.setOnClickListener(this);
            cancelRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClick != null)
                        onItemClick.setOnCancelRequest(getAdapterPosition());
                }
            });

            journeyStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClick != null)
                        onItemClick.setOnJourneyStartListener(getAdapterPosition());
                }
            });

            journeyStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClick != null)
                        onItemClick.setOnJourneyStopListener(getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View view) {
            if(onItemClick != null)
                onItemClick.setOnItemClick(getAdapterPosition());
        }
    }

    public interface onItemClick{
        void setOnItemClick(int position);
        void setOnCancelRequest(int position);
        void setOnJourneyStartListener(int position);
        void setOnJourneyStopListener(int position);
    }

    public void setOnItemClickListener(onItemClick onItemClick){
        this.onItemClick = onItemClick;
    }
}
