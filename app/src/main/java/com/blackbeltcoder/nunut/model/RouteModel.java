package com.blackbeltcoder.nunut.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ainozenbook on 10/30/2016.
 */

public class RouteModel implements Serializable {

    @SerializedName("_id")
    public Long _id;

    @SerializedName("id")
    public Long id;

    @SerializedName("postalOrigin")
    public String postalOrigin;

    @SerializedName("nameOrigin")
    public String nameOrigin;

    @SerializedName("postalDestination")
    public String postalDestination;

    @SerializedName("nameDestination")
    public String nameDestination;

    @SerializedName("vote")
    public Long vote;

    @SerializedName("submitDate")
    public String submitDate;

    @SerializedName("submitBy")
    public String submitBy;

    @SerializedName("isOpen")
    public String isOpen;

    @SerializedName("originArea")
    public String originArea;

    @SerializedName("destinationArea")
    public String destinationArea;
}
