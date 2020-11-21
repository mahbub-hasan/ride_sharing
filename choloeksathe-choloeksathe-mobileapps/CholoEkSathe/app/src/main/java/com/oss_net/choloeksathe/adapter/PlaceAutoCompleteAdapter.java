package com.oss_net.choloeksathe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;
import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.entity.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by mahbubhasan on 3/26/18.
 */

public class PlaceAutoCompleteAdapter extends RecyclerView.Adapter<PlaceAutoCompleteAdapter.PlaceViewHolder> implements Filterable {
    public interface PlaceAutocompleteInterface{
        void onPlaceClick(ArrayList<PlaceAutocomplete> placeAutocompleteArrayList, int position);
    }

    Context context;
    GoogleApiClient googleApiClient;
    LatLngBounds bounds;
    AutocompleteFilter autocompleteFilter;
    private ArrayList<PlaceAutocomplete> mResultList;
    private PlaceAutocompleteInterface placeAutocompleteInterface;

    public PlaceAutoCompleteAdapter(Context context, GoogleApiClient googleApiClient, LatLngBounds bounds, AutocompleteFilter autocompleteFilter) {
        this.context = context;
        this.googleApiClient = googleApiClient;
        this.bounds = bounds;
        this.autocompleteFilter = autocompleteFilter;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.adapter_place_autocomplete, parent, false);
        PlaceViewHolder placeViewHolder = new PlaceViewHolder(convertView);
        return placeViewHolder;
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        PlaceAutocomplete placeAutocomplete = mResultList.get(position);
        holder.placeName.setText(placeAutocomplete.getDescription());
    }

    @Override
    public int getItemCount() {
        if(mResultList != null)
            return mResultList.size();
        else
            return 0;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null){
                    mResultList = getAutoCompleteResult(constraint);
                    if(mResultList != null){
                        filterResults.values = mResultList;
                        filterResults.count = mResultList.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if(filterResults != null && filterResults.count>0){
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }

    private ArrayList<PlaceAutocomplete> getAutoCompleteResult(CharSequence constraint){
        if(this.googleApiClient.isConnected()) {
            PendingResult<AutocompletePredictionBuffer> result = Places.GeoDataApi.getAutocompletePredictions(googleApiClient,constraint.toString(),bounds,autocompleteFilter);
            AutocompletePredictionBuffer autocompletePredictions = result.await(60, TimeUnit.SECONDS);
            final Status status = autocompletePredictions.getStatus();
            if(!status.isSuccess()){
                autocompletePredictions.release();
                return null;
            }

            Iterator<AutocompletePrediction> autocompletePredictionIterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList(autocompletePredictions.getCount());
            while (autocompletePredictionIterator.hasNext()){
                AutocompletePrediction autocompletePrediction = autocompletePredictionIterator.next();
                resultList.add(new PlaceAutocomplete(autocompletePrediction.getPlaceId(), autocompletePrediction.getFullText(null)));
            }

            autocompletePredictions.release();

            return resultList;
        }

        return null;
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder{

        TextView placeName;

        PlaceViewHolder(View itemView) {
            super(itemView);

            placeName = itemView.findViewById(R.id.placeName);

            placeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(placeAutocompleteInterface != null)
                        placeAutocompleteInterface.onPlaceClick(mResultList,getAdapterPosition());
                }
            });
        }
    }

    public void setOnPlaceClickListener(PlaceAutocompleteInterface placeAutocompleteInterface){
        this.placeAutocompleteInterface = placeAutocompleteInterface;
    }

}
