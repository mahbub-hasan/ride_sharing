package com.oss_net.choloeksathe.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.remote_model.CarShareListInfo;
import com.oss_net.choloeksathe.utils.CircularImageView;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mahbubhasan on 12/11/17.
 */

public class CarShareListInfoAdapter extends RecyclerView.Adapter<CarShareListInfoAdapter.CarShareListInfoViewHolder>{

    List<CarShareListInfo> carShareListInfos;
    Context context;
    onClickEvents onClickEvents;

    public CarShareListInfoAdapter(List<CarShareListInfo> carShareListInfos) {
        this.carShareListInfos = carShareListInfos;
    }

    public CarShareListInfoAdapter(List<CarShareListInfo> carShareListInfos, Context context) {
        this.carShareListInfos = carShareListInfos;
        this.context = context;
    }

    @Override
    public CarShareListInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CarShareListInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_car_share_list_info, parent, false));
    }

    @Override
    public void onBindViewHolder(CarShareListInfoViewHolder holder, int position) {
        CarShareListInfo carShareListInfo = carShareListInfos.get(position);
        holder.activityId.setText(String.format("Request # %s",carShareListInfo.activityId));
        holder.price.setText(String.format("BDT : %s",carShareListInfo.price));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy HH:mm");
        holder.startTime.setText(simpleDateFormat.format(new Date(carShareListInfo.startTime)));
        holder.startLocation.setText(carShareListInfo.startLocationName);
        holder.stopLocation.setText(carShareListInfo.stopLocationName);
        holder.passengerName.setText(carShareListInfo.firstName);
        holder.passengerRating.setText(""+carShareListInfo.totalRating+"/5");
        holder.requestSit.setText(String.format(Locale.getDefault(), "Request for %d sit",carShareListInfo.passengerSitRequest));
        if(carShareListInfo.activityStatusId==1) {
            holder.status_text.setText("Interested");
            holder.viewAcceptAndReject.setVisibility(View.VISIBLE);
            holder.viewStartAndEnd.setVisibility(View.GONE);
        }else if(carShareListInfo.activityStatusId==2){
            holder.status_text.setText("Accepted");
            holder.viewAcceptAndReject.setVisibility(View.GONE);
            holder.viewStartAndEnd.setVisibility(View.VISIBLE);
            holder.buttonJourneyEnd.setVisibility(View.GONE);
        }else if(carShareListInfo.activityStatusId==4){
            holder.status_text.setText("Started");
            holder.viewAcceptAndReject.setVisibility(View.GONE);
            holder.viewStartAndEnd.setVisibility(View.VISIBLE);
            holder.buttonJourneyStart.setVisibility(View.GONE);
        }

        if(carShareListInfo.imageLocation == null || carShareListInfo.imageLocation.isEmpty())
            Glide.with(context).load(CommonURL.getInstance().emptyPictureUrl).into(holder.passengerImage);
        else
            Glide.with(context).load(CommonTask.getGlideUrl(carShareListInfo.imageLocation, context)).into(holder.passengerImage);
    }

    @Override
    public int getItemCount() {
        return carShareListInfos.size();
    }

    class CarShareListInfoViewHolder extends RecyclerView.ViewHolder{
        TextView activityId, startTime, price, startLocation, stopLocation,
                passengerName, status_text, requestSit,passengerRating;
        CircleImageView passengerImage;
        HorizontalScrollView viewAcceptAndReject, viewStartAndEnd;
        Button buttonRequestAccepted, buttonRequestRejected, buttonCall1, buttonJourneyStart, buttonJourneyEnd, buttonCall2;

        CarShareListInfoViewHolder(View itemView) {
            super(itemView);
            activityId = itemView.findViewById(R.id.tvActivityId);
            startTime = itemView.findViewById(R.id.startTime);
            price = itemView.findViewById(R.id.price);
            startLocation = itemView.findViewById(R.id.startLocation);
            stopLocation = itemView.findViewById(R.id.stopLocation);
            passengerName = itemView.findViewById(R.id.passengerName);
            passengerImage = itemView.findViewById(R.id.passengerImage);
            status_text = itemView.findViewById(R.id.status_text);
            requestSit = itemView.findViewById(R.id.sitWant);
            viewAcceptAndReject = itemView.findViewById(R.id.viewAcceptAndReject);
            viewStartAndEnd = itemView.findViewById(R.id.viewStartAndEnd);
            passengerRating = itemView.findViewById(R.id.rating);

            buttonRequestAccepted = itemView.findViewById(R.id.buttonRequestAccepted);
            buttonRequestRejected = itemView.findViewById(R.id.buttonRequestRejected);
            buttonCall1 = itemView.findViewById(R.id.buttonCall1);
            buttonJourneyStart = itemView.findViewById(R.id.buttonJourneyStart);
            buttonJourneyEnd = itemView.findViewById(R.id.buttonJourneyEnd);
            buttonCall2 = itemView.findViewById(R.id.buttonCall2);

            buttonRequestAccepted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onClickEvents != null)
                        onClickEvents.onItemClickListener(getAdapterPosition(), "REQUEST_ACCEPT");
                }
            });

            buttonRequestRejected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onClickEvents != null)
                        onClickEvents.onItemClickListener(getAdapterPosition(), "REQUEST_REJECT");
                }
            });

            buttonJourneyStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onClickEvents != null)
                        onClickEvents.onItemClickListener(getAdapterPosition(), "JOURNEY_START");
                }
            });

            buttonJourneyEnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onClickEvents != null)
                        onClickEvents.onItemClickListener(getAdapterPosition(), "JOURNEY_STOP");
                }
            });

            buttonCall1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onClickEvents != null)
                        onClickEvents.onItemClickListener(getAdapterPosition(), "CALL");
                }
            });

            buttonCall2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onClickEvents != null)
                        onClickEvents.onItemClickListener(getAdapterPosition(), "CALL");
                }
            });
        }
    }

    public interface onClickEvents{
        void onItemClickListener(int position, String mode);
    }

    public void setOnItemsClickEvents(onClickEvents onItemsClickEvents){
        onClickEvents = onItemsClickEvents;
    }
}
