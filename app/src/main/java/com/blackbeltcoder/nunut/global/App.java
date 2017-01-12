package com.blackbeltcoder.nunut.global;

import android.app.Application;
import android.content.pm.PackageManager;

import com.blackbeltcoder.nunut.model.NuterModel;
import com.blackbeltcoder.nunut.util.ConverterUtil;

import org.json.JSONObject;

/**
 * Created by ainozenbook on 9/19/2016.
 */
public class App extends Application {
    public static SecurePreferences secPref;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    @Override
    public void onCreate() {
        super.onCreate();

        secPref = new SecurePreferences(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public int getAppVersion() {
        int localVersion = 1;
        try {
            localVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        secPref.put(IS_FIRST_TIME_LAUNCH, isFirstTime);
    }

    public boolean isFirstTimeLaunch() {
        return secPref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setNuter(NuterModel n) {
        JSONObject nuterJson = ConverterUtil.ObjecttoJSON(n);
        secPref.put("CurrentNuter", nuterJson.toString());
    }

    public static NuterModel getNuter() {
        try {
            String nmString = secPref.getString("CurrentNuter", "");
            if(!nmString.equals(""))
                return ConverterUtil.JSONtoObject(NuterModel.class, new JSONObject(nmString));
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
