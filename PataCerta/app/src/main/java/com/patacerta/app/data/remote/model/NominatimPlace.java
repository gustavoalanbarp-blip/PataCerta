package com.patacerta.app.data.remote.model;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo parcial da resposta de GET /search (Nominatim / OpenStreetMap).
 * Usado no localizador de clínicas veterinárias (RF07).
 */
public class NominatimPlace {

    @SerializedName("place_id")
    private long placeId;

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("lat")
    private String lat;

    @SerializedName("lon")
    private String lon;

    @SerializedName("type")
    private String type;

    public long getPlaceId() { return placeId; }
    public String getDisplayName() { return displayName; }
    public String getLat() { return lat; }
    public String getLon() { return lon; }
    public String getType() { return type; }
}
