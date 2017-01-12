package com.blackbeltcoder.nunut.provider;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.blackbeltcoder.nunut.contract.RouteContract;
import com.tjeannin.provigen.ProviGenOpenHelper;
import com.tjeannin.provigen.ProviGenProvider;

/**
 * Created by ainozenbook on 9/15/2016.
 */
public class DataProvider extends ProviGenProvider {

    private static Class[] dataContract = new Class[]{RouteContract.class};

    @Override
    public SQLiteOpenHelper openHelper(Context context) {
        return new ProviGenOpenHelper(getContext(), "nunut", null, 1, dataContract);
    }

    @Override
    public Class[] contractClasses() {
        return dataContract;
    }
}
