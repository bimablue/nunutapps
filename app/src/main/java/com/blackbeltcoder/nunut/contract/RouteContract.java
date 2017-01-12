package com.blackbeltcoder.nunut.contract;

import android.net.Uri;

import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.ContentUri;

/**
 * Created by ainozenbook on 10/30/2016.
 */

public interface RouteContract extends ProviGenBaseContract {

    @Column(Column.Type.INTEGER)
    public static final String COLUMN_SERVER_ID = "id";

    @Column(Column.Type.TEXT)
    public static final String COLUMN_POSTAL_ORIGIN = "postalOrigin";

    @Column(Column.Type.TEXT)
    public static final String COLUMN_NAME_ORIGIN = "nameOrigin";

    @Column(Column.Type.TEXT)
    public static final String COLUMN_POSTAL_DESTINATION = "postalDestination";

    @Column(Column.Type.TEXT)
    public static final String COLUMN_NAME_DESTINATION = "nameDestination";

    @Column(Column.Type.REAL)
    public static final String COLUMN_ROUTE_VOTE = "vote";

    @Column(Column.Type.TEXT)
    public static final String COLUMN_NAME_SUBMIT_DATE = "submitDate";

    @Column(Column.Type.TEXT)
    public static final String COLUMN_NAME_SUBMIT_BY = "submitBy";

    @Column(Column.Type.TEXT)
    public static final String COLUMN_NAME_IS_OPEN = "isOpen";

    @Column(Column.Type.TEXT)
    public static final String COLUMN_NAME_ORIGIN_AREA = "originArea";

    @Column(Column.Type.TEXT)
    public static final String COLUMN_NAME_DESTINATION_AREA = "destinationArea";

    @ContentUri
    public static final Uri CONTENT_URI = Uri.parse("content://com.blackbeltcoder.nunut/route");
}
