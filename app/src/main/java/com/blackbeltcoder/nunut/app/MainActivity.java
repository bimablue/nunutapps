package com.blackbeltcoder.nunut.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.blackbeltcoder.nunut.R;
import com.blackbeltcoder.nunut.adapter.MainViewPagerAdapter;
import com.blackbeltcoder.nunut.component.CustomInfoDialog;
import com.blackbeltcoder.nunut.global.App;
import com.blackbeltcoder.nunut.global.ConstantVariable;
import com.blackbeltcoder.nunut.model.RouteModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class MainActivity extends AppCompatActivity implements
        MainRegisterFragment.OnFragmentInteractionListener,
        MainRouteFragment.OnFragmentInteractionListener,
        MainShareFragment.OnFragmentInteractionListener,
        MainSettingFragment.OnFragmentInteractionListener,
        MainDefaultFragment.OnFragmentInteractionListener,
        MainRegisterRouteCheckFragment.OnFragmentInteractionListener,
        MainRegisterSuccessFragment.OnFragmentInteractionListener {

    private App app;
    private Vibrator vb;
    private RouteModel rmGlobal;

    private MainRegisterFragment registerFragment;
    private MainRouteFragment routeFragment;
    private MainShareFragment shareFragment;
    private MainSettingFragment settingFragment;
    private MainDefaultFragment defaultFragment;
    private MainRegisterRouteCheckFragment registerRouteCheckFragment;
    private MainRegisterSuccessFragment registerSuccessFragment;
    private MainViewPagerAdapter mainViewAdapter;

    private CustomInfoDialog dialogInfo, dialogWarning;

    private AHBottomNavigation bottomNavigation;
    private AHBottomNavigationViewPager navigationViewPager;

    private int currentPosition = -1;
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private String fieldName = "";
    private boolean alreadyLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        app = (App) getApplicationContext();
        vb = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        currentPosition = getIntent().getIntExtra("ACT_MODE", 0);

        alreadyLogin = false;
        if(App.getNuter() != null){
            if(!App.getNuter().serverKey.equals("")){
                alreadyLogin = true;
                currentPosition = 6;
            }
        }

        dialogInfo = new CustomInfoDialog(MainActivity.this,
                ConstantVariable.TITLE_INFO,
                getResources().getString(R.string.msg_install_first),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogInfo.dismiss();
                    }
                });

        dialogWarning = new CustomInfoDialog(MainActivity.this,
                ConstantVariable.TITLE_INFO,
                getResources().getString(R.string.msg_register_first),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogWarning.dismiss();
                    }
                });

        bindActivity();
    }

    @Override
    public void onBackPressed() {
        /*vb.vibrate(40);
        finish();

        super.onBackPressed();*/
    }

    private void bindActivity(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNavigation);
        navigationViewPager = (AHBottomNavigationViewPager) findViewById(R.id.navigationViewPager);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.nav_register, R.drawable.ic_register_nav, R.color.colorPrimaryDark);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.nav_route, R.drawable.ic_route_nav, R.color.colorPrimaryDark);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.nav_share, R.drawable.ic_share_nav, R.color.colorPrimaryDark);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.nav_setting, R.drawable.ic_setting_nav, R.color.colorPrimaryDark);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setAccentColor(Color.parseColor("#356286"));
        bottomNavigation.setInactiveColor(Color.parseColor("#356286"));
        bottomNavigation.setForceTint(true);
        bottomNavigation.setForceTitlesDisplay(true);
        bottomNavigation.setColored(true);
        bottomNavigation.setTitleTextSize(28, 25);
        bottomNavigation.setTitleTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        bottomNavigation.setCurrentItem(currentPosition);
        bottomNavigation.setNotification("", 1);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                if (wasSelected) {
                    if(position == 0){
                        if(alreadyLogin){
                            registerSuccessFragment = (MainRegisterSuccessFragment) mainViewAdapter.getCurrentFragment();
                        } else {
                            if (currentPosition == 0)
                                registerFragment = (MainRegisterFragment) mainViewAdapter.getCurrentFragment();
                            //registerFragment.refreshUI();
                        }
                    } else if(position == 1){
                        routeFragment = (MainRouteFragment) mainViewAdapter.getCurrentFragment();
                        routeFragment.refresh();
                    } else if(position == 2){
                        shareFragment = (MainShareFragment) mainViewAdapter.getCurrentFragment();
                    } else if(position == 3){
                        settingFragment = (MainSettingFragment) mainViewAdapter.getCurrentFragment();
                    }

                    return true;
                }

                if (position == 0) {
                    if(alreadyLogin) {
                        position = 6;
                        registerSuccessFragment = (MainRegisterSuccessFragment) mainViewAdapter.getItem(position);
                        registerSuccessFragment.willBeHidden();
                    } else {
                        if (currentPosition == 0) {
                            registerFragment = (MainRegisterFragment) mainViewAdapter.getItem(position);
                            registerFragment.willBeHidden();
                        } else if (currentPosition == 5) {
                            registerRouteCheckFragment = (MainRegisterRouteCheckFragment) mainViewAdapter.getItem(position);
                            registerRouteCheckFragment.willBeHidden();
                        } else if (currentPosition == 6) {
                            registerSuccessFragment = (MainRegisterSuccessFragment) mainViewAdapter.getItem(position);
                            registerSuccessFragment.willBeHidden();
                        }
                    }
                } else if (position == 1) {
                    routeFragment = (MainRouteFragment) mainViewAdapter.getItem(position);
                    routeFragment.willBeHidden();
                } else if (position == 2) {
                    if(App.getNuter() != null) {
                        shareFragment = (MainShareFragment) mainViewAdapter.getItem(position);
                        shareFragment.willBeHidden();
                    } else {
                        position = 4;
                        dialogWarning.show();

                        defaultFragment = (MainDefaultFragment) mainViewAdapter.getItem(position);
                        defaultFragment.willBeHidden();
                    }
                } else if (position == 3) {
                    if(App.getNuter() != null) {
                        settingFragment = (MainSettingFragment) mainViewAdapter.getItem(position);
                        settingFragment.willBeHidden();
                    } else {
                        position = 4;
                        dialogWarning.show();

                        defaultFragment = (MainDefaultFragment) mainViewAdapter.getItem(position);
                        defaultFragment.willBeHidden();
                    }
                }

                currentPosition = position;
                navigationViewPager.setCurrentItem(position, false);
                showSelectedFragment(position);

                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        });

        navigationViewPager.setOffscreenPageLimit(6);
        mainViewAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        navigationViewPager.setAdapter(mainViewAdapter);

        showSelectedFragment(currentPosition);
        navigationViewPager.setCurrentItem(currentPosition, false);
    }

    private void showSelectedFragment(int position){
        if(position == 0){
            registerFragment = (MainRegisterFragment) mainViewAdapter.getItem(position);
            registerFragment.willBeDisplayed();
        } else if(position == 1){
            routeFragment = (MainRouteFragment) mainViewAdapter.getItem(position);
            routeFragment.willBeDisplayed();
        } else if(position == 2){
            shareFragment = (MainShareFragment) mainViewAdapter.getItem(position);
            shareFragment.willBeDisplayed();
        } else if(position == 3){
            settingFragment = (MainSettingFragment) mainViewAdapter.getItem(position);
            settingFragment.willBeDisplayed();
        } else if(position == 4){
            defaultFragment = (MainDefaultFragment) mainViewAdapter.getItem(position);
            defaultFragment.willBeDisplayed();
        } else if(position == 5){
            registerRouteCheckFragment = (MainRegisterRouteCheckFragment) mainViewAdapter.getItem(position);
            registerRouteCheckFragment.willBeDisplayed(rmGlobal);
        } else if(position == 6){
            registerSuccessFragment = (MainRegisterSuccessFragment) mainViewAdapter.getItem(position);
            registerSuccessFragment.willBeDisplayed();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(int pageMode) {
        int position = 0;
        if (pageMode == 1) {
            showSelectedFragment(0);
            position = 0;
        } else if (pageMode == 2) {
            showSelectedFragment(5);
            position = 5;
        } else if (pageMode == 3) {
            alreadyLogin = true;
            showSelectedFragment(6);
            position = 6;
        }

        currentPosition = position;
        navigationViewPager.setCurrentItem(position, false);
    }

    @Override
    public void onFragmentInteraction(RouteModel rm) {
        rmGlobal = rm;
        showSelectedFragment(5);
        currentPosition = 5;
        navigationViewPager.setCurrentItem(5, false);
    }

    @Override
    public void getAddressFromMaps(String fieldName) {
        try {
            this.fieldName = fieldName;
            Intent intent = new PlaceAutocomplete.IntentBuilder
                    (PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(MainActivity.this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException |
                GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                //Log.v("GooglePlaces", " Place: " + place.getName());
                registerFragment = (MainRegisterFragment) mainViewAdapter.getItem(currentPosition);
                registerFragment.refreshField(fieldName, place.getAddress().toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("GooglePlaces", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
