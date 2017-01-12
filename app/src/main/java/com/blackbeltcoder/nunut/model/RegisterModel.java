package com.blackbeltcoder.nunut.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ainozenbook on 10/30/2016.
 */

public class RegisterModel implements Serializable {

    @SerializedName("nuter")
    public NuterModel nuter;

    @SerializedName("route")
    public RouteModel route;
}
