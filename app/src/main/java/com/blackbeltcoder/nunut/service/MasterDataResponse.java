package com.blackbeltcoder.nunut.service;

import com.blackbeltcoder.nunut.model.RouteModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ainozenbook on 9/15/2016.
 */
public class MasterDataResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("routes")
    private List<RouteModel> routes;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RouteModel> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteModel> routes) {
        this.routes = routes;
    }
}
