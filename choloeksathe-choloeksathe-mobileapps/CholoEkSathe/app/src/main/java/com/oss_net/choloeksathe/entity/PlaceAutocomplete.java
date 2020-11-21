package com.oss_net.choloeksathe.entity;

/**
 * Created by mahbubhasan on 3/26/18.
 */

public class PlaceAutocomplete {
    private CharSequence placeId;
    private CharSequence description;

    public PlaceAutocomplete(CharSequence placeId, CharSequence description) {
        this.placeId = placeId;
        this.description = description;
    }

    public CharSequence getPlaceId() {
        return placeId;
    }

    public void setPlaceId(CharSequence placeId) {
        this.placeId = placeId;
    }

    public CharSequence getDescription() {
        return description;
    }

    public void setDescription(CharSequence description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PlaceAutocomplete {" +
                "placeId=" + placeId +
                ", description=" + description +
                '}';
    }
}
