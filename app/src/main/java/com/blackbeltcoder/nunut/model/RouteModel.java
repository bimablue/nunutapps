package com.blackbeltcoder.nunut.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ainozenbook on 10/30/2016.
 */

@IgnoreExtraProperties
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

    @SerializedName("serverKey")
    public String serverKey;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("postalOrigin", postalOrigin);
        result.put("nameOrigin", nameOrigin);
        result.put("postalDestination", postalDestination);
        result.put("nameDestination", nameDestination);
        result.put("vote", vote);
        result.put("submitDate", submitDate);
        result.put("submitBy", submitBy);
        result.put("isOpen", isOpen);
        return result;
    }
}
