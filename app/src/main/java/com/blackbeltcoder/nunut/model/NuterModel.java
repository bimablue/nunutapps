package com.blackbeltcoder.nunut.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ainozenbook on 10/30/2016.
 */

public class NuterModel implements Serializable {

    @SerializedName("id")
    public Long id;

    @SerializedName("fullname")
    public String fullname;

    @SerializedName("email")
    public String email;

    @SerializedName("phone")
    public String phone;

    @SerializedName("homeAddress")
    public String homeAddress;

    @SerializedName("homePostal")
    public String homePostal;

    @SerializedName("homeLatitude")
    public Double homeLatitude;

    @SerializedName("homeLongitude")
    public Double homeLongitude;

    @SerializedName("officeAddress")
    public String officeAddress;

    @SerializedName("officePostal")
    public String officePostal;

    @SerializedName("officeLatitude")
    public Double officeLatitude;

    @SerializedName("officeLongitude")
    public Double officeLongitude;

    @SerializedName("isDriver")
    public String isDriver;

    @SerializedName("joinDate")
    public String joinDate;

    @SerializedName("balance")
    public Long balance;

    @SerializedName("referralCode")
    public String referralCode;

    @SerializedName("routeId")
    public Long routeId;

}
