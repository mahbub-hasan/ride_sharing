package com.oss_net.choloeksathe.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.base.App;
import com.oss_net.choloeksathe.entity.databases.remote_model.PassengerAvailableCar;
import com.oss_net.choloeksathe.fragments.passenger.AvailableVehicelsFragment;
import com.oss_net.choloeksathe.utils.CircularImageView;
import com.oss_net.choloeksathe.utils.CommonTask;
import com.oss_net.choloeksathe.utils.CommonURL;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Noor Nabiul Alam Siddiqui on 12/11/2017.
 * siddiqui.sazal@gmail.com
 */

public class PassengerCarListAdapter extends RecyclerView.Adapter<PassengerCarListAdapter.CarListViewHolder> {

    onItemClick itemClick;
    List<PassengerAvailableCar> passengerAvailableCars;
    Context context;

    public PassengerCarListAdapter(List<PassengerAvailableCar> passengerAvailableCars, Context context) {
        this.passengerAvailableCars = passengerAvailableCars;
        this.context = context;
    }


    @Override
    public CarListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CarListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_passenger_car_list, parent, false));
    }

    @Override
    public void onBindViewHolder(CarListViewHolder holder, int position) {
        PassengerAvailableCar passengerAvailableCar = passengerAvailableCars.get(position);

        holder.requestIdNumber.setText(String.format(Locale.getDefault(), "Request # %s",passengerAvailableCar.requestId));
        holder.price.setText(String.format(Locale.getDefault(), "BDT %s", passengerAvailableCar.price));
        holder.availableSit.setText(String.format(Locale.getDefault(),"Available sits: %d", passengerAvailableCar.noofFreeSeat));
        SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy HH:mm");
        holder.startTime.setText(format.format(new Date(passengerAvailableCar.startTime)));
        //holder.startTime.setText(String.format(Locale.getDefault(), "%s", DateUtils.getRelativeTimeSpanString(passengerAvailableCar.startTime)));
        holder.sourceLocationName.setText(passengerAvailableCar.startPlace);
        holder.destinationLocationName.setText(passengerAvailableCar.endPlace);
        holder.driverFullName.setText(passengerAvailableCar.driverName);
        holder.carNumber.setText(passengerAvailableCar.carNumber);
        holder.driverRating.setText(""+passengerAvailableCar.totalRating+"/5");
        if(passengerAvailableCar.driverPic!= null){
            if(getItemCount() != 0)
            Glide.with(context).load(CommonTask.getGlideUrl(passengerAvailableCar.driverPic, context)).into(holder.driverImage);
        }else {
            Glide.with(context).load(CommonURL.getInstance().emptyPictureUrl).into(holder.driverImage);
        }
        //holder.driverImage.setImageBitmap();
        /*holder.cbFoodAllow.setChecked(passengerAvailableCar.outSideFoodAllowed);
        holder.cbLuggageAllow.setChecked(passengerAvailableCar.luggageSpaceAvailable);
        holder.cbMusicAllow.setChecked(passengerAvailableCar.musicAllowed);
        holder.cbPetAllow.setChecked(passengerAvailableCar.petsAllowed);
        holder.cbSmockingAllow.setChecked(passengerAvailableCar.smokingAllowed);*/
    }

    @Override
    public int getItemCount() {
        return passengerAvailableCars.size();
    }

    class CarListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView requestIdNumber, price, availableSit, startTime,
                sourceLocationName, destinationLocationName, driverFullName, carNumber,driverRating;
        private CircleImageView driverImage;
       // CheckBox cbLuggageAllow, cbSmockingAllow, cbFoodAllow, cbMusicAllow, cbPetAllow;
       // Button buttonShowInterest;

        CarListViewHolder(View itemView) {
            super(itemView);
            requestIdNumber = itemView.findViewById(R.id.requestIdNumber);
            price = itemView.findViewById(R.id.price);
            availableSit = itemView.findViewById(R.id.availableSit);
            startTime = itemView.findViewById(R.id.startTime);
            sourceLocationName = itemView.findViewById(R.id.sourceLocationName);
            destinationLocationName = itemView.findViewById(R.id.destinationLocationName);
            driverFullName = itemView.findViewById(R.id.driverFullName);
            carNumber = itemView.findViewById(R.id.carNumber);
            driverImage = itemView.findViewById(R.id.driverImage);
            driverRating = itemView.findViewById(R.id.rating);
            /*cbLuggageAllow = itemView.findViewById(R.id.cbLuggageAllow);
            cbSmockingAllow = itemView.findViewById(R.id.cbSmockingAllow);
            cbFoodAllow = itemView.findViewById(R.id.cbFoodAllow);
            cbMusicAllow = itemView.findViewById(R.id.cbMusicAllow);
            cbPetAllow = itemView.findViewById(R.id.cbPetAllow);
            buttonShowInterest = itemView.findViewById(R.id.buttonShowInterest);*/

            itemView.setOnClickListener(this);
            /*buttonShowInterest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClick != null)
                        itemClick.setOnInterestButtonClick(getAdapterPosition());
                }
            });*/
        }

        @Override
        public void onClick(View view) {
            if(itemClick != null)
                itemClick.setOnItemClick(getAdapterPosition());
        }
    }

    public interface onItemClick{
        void setOnItemClick(int position);
    }

    public void setOnItemClickListener(onItemClick onItemClick){
        this.itemClick = onItemClick;
    }
}
