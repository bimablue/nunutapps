package com.blackbeltcoder.nunut.service;

import com.blackbeltcoder.nunut.model.NuterModel;
import com.blackbeltcoder.nunut.model.RouteModel;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ainozenbook on 9/15/2016.
 */
public class NuterResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("nuter")
    private NuterModel nuter;

    @SerializedName("route")
    private RouteModel route;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public NuterModel getNuter() {
        return nuter;
    }

    public void setAgent(NuterModel nuter) {
        this.nuter = nuter;
    }

    public RouteModel getRoute() {
        return route;
    }

    public void setRoute(RouteModel route) {
        this.route = route;
    }
}
