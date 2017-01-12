package com.blackbeltcoder.nunut.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.blackbeltcoder.nunut.app.MainDefaultFragment;
import com.blackbeltcoder.nunut.app.MainRegisterFragment;
import com.blackbeltcoder.nunut.app.MainRegisterRouteCheckFragment;
import com.blackbeltcoder.nunut.app.MainRegisterSuccessFragment;
import com.blackbeltcoder.nunut.app.MainRouteFragment;
import com.blackbeltcoder.nunut.app.MainSettingFragment;
import com.blackbeltcoder.nunut.app.MainShareFragment;

import java.util.ArrayList;

/**
 * Created by ainozenbook on 10/30/2016.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private Object objFragment;
    private ArrayList<Object> objFragments = new ArrayList<Object>();

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);

        objFragments.clear();
        objFragments.add(MainRegisterFragment.newInstance("", ""));
        objFragments.add(MainRouteFragment.newInstance("c", "d"));
        objFragments.add(MainShareFragment.newInstance("e", "f"));
        objFragments.add(MainSettingFragment.newInstance("e", "f"));
        objFragments.add(MainDefaultFragment.newInstance("e", "f"));
        objFragments.add(MainRegisterRouteCheckFragment.newInstance("", ""));
        objFragments.add(MainRegisterSuccessFragment.newInstance("", ""));
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) objFragments.get(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            objFragment = object;
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getCount() {
        return objFragments.size();
    }

    public Object getCurrentFragment() {
        return objFragment;
    }
}
