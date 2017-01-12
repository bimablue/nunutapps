package com.blackbeltcoder.nunut.model;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ainozenbook on 1/11/2017.
 */

public class ListItemModel {
    public String listItemText;
    public String listItemCreationDate;

    public String getListItemText() {
        return listItemText;
    }

    public void setListItemText(String listItemText) {
        this.listItemText = listItemText;
    }

    public void setListItemCreationDate(String listItemCreationDate) {
        this.listItemCreationDate = listItemCreationDate;
    }

    @Override
    public String toString() {
        return this.listItemText +"\n" + this.listItemCreationDate;
    }

    public String getListItemCreationDate() {
        return listItemCreationDate;
    }

    public ListItemModel() {
        // Default constructor required for calls to DataSnapshot.getValue(ListItem.class)
    }

    public ListItemModel(String listItemText) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        this.listItemCreationDate = sdf.format(new Date());
        this.listItemText = listItemText;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("listItemText", listItemText);
        result.put("listItemCreationDate", listItemCreationDate);
        return result;
    }
}
