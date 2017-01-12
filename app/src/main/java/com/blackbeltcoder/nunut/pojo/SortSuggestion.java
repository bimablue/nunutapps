package com.blackbeltcoder.nunut.pojo;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by ainozenbook on 10/9/2016.
 */

public class SortSuggestion implements SearchSuggestion {

    private String suggestionLabel;
    private boolean isHistory = false;

    public SortSuggestion(String suggestion) {
        this.setSuggestionLabel(suggestion.toLowerCase());
    }

    public SortSuggestion(Parcel source) {
        this.setSuggestionLabel(source.readString());
        this.setHistory(source.readInt() != 0);
    }

    @Override
    public String getBody() {
        return getSuggestionLabel();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getSuggestionLabel());
        parcel.writeInt(isHistory ? 1 : 0);
    }

    public boolean isHistory() {
        return isHistory;
    }

    public void setHistory(boolean history) {
        isHistory = history;
    }

    public static final Creator<SortSuggestion> CREATOR = new Creator<SortSuggestion>() {
        @Override
        public SortSuggestion createFromParcel(Parcel in) {
            return new SortSuggestion(in);
        }

        @Override
        public SortSuggestion[] newArray(int size) {
            return new SortSuggestion[size];
        }
    };

    public String getSuggestionLabel() {
        return suggestionLabel;
    }

    public void setSuggestionLabel(String suggestionLabel) {
        this.suggestionLabel = suggestionLabel;
    }
}
