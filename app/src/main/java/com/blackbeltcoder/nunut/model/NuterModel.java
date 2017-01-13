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

    @SerializedName("homeArea")
    public String homeArea;

    @SerializedName("homeLatitude")
    public Double homeLatitude;

    @SerializedName("homeLongitude")
    public Double homeLongitude;

    @SerializedName("officeAddress")
    public String officeAddress;

    @SerializedName("officePostal")
    public String officePostal;

    @SerializedName("officeArea")
    public String officeArea;

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

    @SerializedName("serverKey")
    public String serverKey;

    @SerializedName("routeServerKey")
    public String routeServerKey;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("fullname", fullname);
        result.put("email", email);
        result.put("phone", phone);
        result.put("homeAddress", homeAddress);
        result.put("homePostal", homePostal);
        result.put("homeArea", homeArea);
        result.put("officeAddress", officeAddress);
        result.put("officePostal", officePostal);
        result.put("officeArea", officeArea);
        result.put("isDriver", isDriver);
        result.put("joinDate", joinDate);
        result.put("balance", balance);
        result.put("referralCode", referralCode);
        result.put("routeServerKey", routeServerKey);
        return result;
    }

}
