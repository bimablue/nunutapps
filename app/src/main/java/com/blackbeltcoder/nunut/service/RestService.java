package com.blackbeltcoder.nunut.service;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import com.blackbeltcoder.nunut.contract.RouteContract;
import com.blackbeltcoder.nunut.global.ConstantVariable;
import com.blackbeltcoder.nunut.model.NuterModel;
import com.blackbeltcoder.nunut.model.RegisterModel;
import com.blackbeltcoder.nunut.model.RouteModel;
import com.blackbeltcoder.nunut.util.ConverterUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ainozenbook on 11/6/2016.
 */

public class RestService extends IntentService {
    private final String LOG_TAG = RestService.class.getSimpleName();
    private Intent intentResult;

    public RestService() {
        super("RestService");
    }

    private void sendMessage(Intent intent) {
        //Log.d("sender", "Broadcasting message");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String broadcastKey = intent.getStringExtra(ConstantVariable.BROADCAST_KEY);
        //Log.d(LOG_TAG, "broadcastKey : " + broadcastKey);
        if(broadcastKey.equals(ConstantVariable.BROADCAST_KEY_REGISTER)){
            registerProcess(intent);
        } else if(broadcastKey.equals(ConstantVariable.BROADCAST_KEY_LOGIN)){
            loginProcess(intent);
        } else if(broadcastKey.equals(ConstantVariable.BROADCAST_KEY_CHECK)){
            checkRoute(intent);
        } else if(broadcastKey.equals(ConstantVariable.BROADCAST_KEY_ROUTES)){
            masterDataProcess(intent);
        }
    }

    private void loginProcess(Intent intent){
        NuterModel nm = (NuterModel) intent.getSerializableExtra(ConstantVariable.BROADCAST_OBJ);

        RestInterface apiService =
                RestClient.getClient().create(RestInterface.class);
        Call<NuterResponse> call = apiService.doLogin(nm.email, nm.phone);
        //Log.d(LOG_TAG, "start call api -loginProcess()-");
        //App.secPref.put("loginStatus", "P");

        call.enqueue(new Callback<NuterResponse>() {

            @Override
            public void onResponse(Call<NuterResponse> call, Response<NuterResponse> response) {
                //Log.d(LOG_TAG, "response : " + response.body());
                if(response.body().getStatus().equals("Y")){
                    intentResult = new Intent(ConstantVariable.BROADCAST_KEY_LOGIN_RESULT);
                    intentResult.putExtra(ConstantVariable.BROADCAST_STATUS, response.body().getStatus());
                    intentResult.putExtra(ConstantVariable.BROADCAST_KEY_LOGIN_OBJ, response.body().getNuter());
                } else {
                    intentResult = new Intent(ConstantVariable.BROADCAST_KEY_LOGIN_RESULT);
                    intentResult.putExtra(ConstantVariable.BROADCAST_STATUS, "N");
                }

                sendMessage(intentResult);
                //App.secPref.put("loginStatus", "C");
            }

            @Override
            public void onFailure(Call<NuterResponse> call, Throwable t) {
                // Log error here since request failed
                //Log.d(LOG_TAG, "errors : " + t.toString());
                intentResult = new Intent(ConstantVariable.BROADCAST_KEY_LOGIN_RESULT);
                intentResult.putExtra(ConstantVariable.BROADCAST_STATUS, "N");

                sendMessage(intentResult);
                //App.secPref.put("loginStatus", "C");
            }
        });
    }

    private void registerProcess(Intent intent){
        NuterModel nm = (NuterModel) intent.getSerializableExtra(ConstantVariable.BROADCAST_KEY_REGISTER_OBJ);
        RouteModel rm = (RouteModel) intent.getSerializableExtra(ConstantVariable.BROADCAST_KEY_ROUTES_OBJ);

        RegisterModel register = new RegisterModel();
        register.nuter = nm;
        register.route = rm;

        RestInterface apiService =
                RestClient.getClient().create(RestInterface.class);
        Call<NuterResponse> call = apiService.doRegister(register);
        //Log.d(LOG_TAG, "start call api -registerProcess()-");
        //App.secPref.put("registerStatus", "P");

        call.enqueue(new Callback<NuterResponse>() {

            @Override
            public void onResponse(Call<NuterResponse> call, Response<NuterResponse> response) {
                //Log.d(LOG_TAG, "response : " + response.body());
                if(response.body().getStatus().equals("Y")){
                    intentResult = new Intent(ConstantVariable.BROADCAST_KEY_REGISTER_RESULT);
                    intentResult.putExtra(ConstantVariable.BROADCAST_STATUS, response.body().getStatus());
                    intentResult.putExtra(ConstantVariable.BROADCAST_KEY_REGISTER_OBJ, response.body().getNuter());
                    intentResult.putExtra(ConstantVariable.BROADCAST_KEY_ROUTES_OBJ, response.body().getRoute());
                } else {
                    intentResult = new Intent(ConstantVariable.BROADCAST_KEY_REGISTER_RESULT);
                    intentResult.putExtra(ConstantVariable.BROADCAST_STATUS, "N");
                }

                sendMessage(intentResult);
                //App.secPref.put("registerStatus", "C");
            }

            @Override
            public void onFailure(Call<NuterResponse> call, Throwable t) {
                // Log error here since request failed
                //Log.d(LOG_TAG, "errors : " + t.toString());
                intentResult = new Intent(ConstantVariable.BROADCAST_KEY_REGISTER_RESULT);
                intentResult.putExtra(ConstantVariable.BROADCAST_STATUS, "N");

                sendMessage(intentResult);
                //App.secPref.put("registerStatus", "C");
            }
        });
    }

    private void checkRoute(Intent intent){
        RouteModel rm = (RouteModel) intent.getSerializableExtra(ConstantVariable.BROADCAST_OBJ);

        RestInterface apiService =
                RestClient.getClient().create(RestInterface.class);
        Call<NuterResponse> call = apiService.checkRoute(rm.postalOrigin, rm.postalDestination);
        //Log.d(LOG_TAG, "start call api -checkRoute()-");
        //App.secPref.put("loginStatus", "P");

        call.enqueue(new Callback<NuterResponse>() {

            @Override
            public void onResponse(Call<NuterResponse> call, Response<NuterResponse> response) {
                //Log.d(LOG_TAG, "response : " + response.body());
                if(response.body().getStatus().equals("Y")){
                    intentResult = new Intent(ConstantVariable.BROADCAST_KEY_CHECK_RESULT);
                    intentResult.putExtra(ConstantVariable.BROADCAST_STATUS, response.body().getStatus());
                    intentResult.putExtra(ConstantVariable.BROADCAST_KEY_CHECK_OBJ, response.body().getRoute());
                } else {
                    intentResult = new Intent(ConstantVariable.BROADCAST_KEY_CHECK_RESULT);
                    intentResult.putExtra(ConstantVariable.BROADCAST_STATUS, "N");
                }

                sendMessage(intentResult);
                //App.secPref.put("loginStatus", "C");
            }

            @Override
            public void onFailure(Call<NuterResponse> call, Throwable t) {
                // Log error here since request failed
                //Log.d(LOG_TAG, "errors : " + t.toString());
                intentResult = new Intent(ConstantVariable.BROADCAST_KEY_LOGIN_RESULT);
                intentResult.putExtra(ConstantVariable.BROADCAST_STATUS, "N");

                sendMessage(intentResult);
                //App.secPref.put("loginStatus", "C");
            }
        });
    }

    private void masterDataProcess(Intent intent){
        NuterModel nm = (NuterModel) intent.getSerializableExtra(ConstantVariable.BROADCAST_OBJ);

        RestInterface apiService =
                RestClient.getClient().create(RestInterface.class);
        Call<MasterDataResponse> call = apiService.getAllRoute(nm);
        //Log.d(LOG_TAG, "start call api -masterDataProcess()-");

        call.enqueue(new Callback<MasterDataResponse>() {

            @Override
            public void onResponse(Call<MasterDataResponse> call, Response<MasterDataResponse> response) {
                //Log.d(LOG_TAG, "response : " + response.body());
                if(response.body().getStatus().equals("Y")){
                    /* REFRESH ROUTES */
                    List<RouteModel> routes = response.body().getRoutes();
                    getContentResolver().delete(RouteContract.CONTENT_URI, null, null);
                    for (RouteModel route : routes) {
                        Uri insertedUri = getContentResolver().insert(
                                RouteContract.CONTENT_URI,
                                ConverterUtil.ObjecttoContentValue(route)
                        );
                        ContentUris.parseId(insertedUri);
                    }

                    intentResult = new Intent(ConstantVariable.BROADCAST_KEY_ROUTES_RESULT);
                    intentResult.putExtra(ConstantVariable.BROADCAST_STATUS, response.body().getStatus());
                } else {
                    intentResult = new Intent(ConstantVariable.BROADCAST_KEY_ROUTES_RESULT);
                    intentResult.putExtra(ConstantVariable.BROADCAST_STATUS, "N");
                }

                sendMessage(intentResult);
                //App.secPref.put("masterStatus", "C");
            }

            @Override
            public void onFailure(Call<MasterDataResponse> call, Throwable t) {
                // Log error here since request failed
                //Log.d(LOG_TAG, "errors : " + t.toString());
                intentResult = new Intent(ConstantVariable.BROADCAST_KEY_ROUTES_RESULT);
                intentResult.putExtra(ConstantVariable.BROADCAST_STATUS, "N");

                sendMessage(intentResult);
                //App.secPref.put("masterStatus", "C");
            }
        });
    }
}
