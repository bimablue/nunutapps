package com.blackbeltcoder.nunut.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ainozenbook on 1/11/2017.
 */

public class AreaCodeModel implements Serializable {

    @SerializedName("_id")
    public Long _id;

    @SerializedName("postalCode")
    public String postalCode;

    @SerializedName("areaName")
    public String areaName;
}
