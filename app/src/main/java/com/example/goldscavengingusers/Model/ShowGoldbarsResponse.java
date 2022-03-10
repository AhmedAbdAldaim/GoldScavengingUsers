package com.example.goldscavengingusers.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ShowGoldbarsResponse {

    @SerializedName("goldBarOwners")
    public List<ShowGoldbarsModel> showGoldbarsModels;
    @SerializedName("error")
    private boolean error;
    @SerializedName("message_en")
    private String message_en;
    @SerializedName("message_ar")
    private String message_ar;


    public ShowGoldbarsResponse(List<ShowGoldbarsModel> showGoldbarsModels, boolean error, String message_en, String message_ar) {
        this.showGoldbarsModels = showGoldbarsModels;
        this.error = error;
        this.message_en = message_en;
        this.message_ar = message_ar;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage_en() {
        return message_en;
    }

    public void setMessage_en(String message_en) {
        this.message_en = message_en;
    }

    public String getMessage_ar() {
        return message_ar;
    }

    public void setMessage_ar(String message_ar) {
        this.message_ar = message_ar;
    }

    public List<ShowGoldbarsModel> getShowGoldbarsModels() {
        return showGoldbarsModels;
    }

    public void setShowGoldbarsModels(List<ShowGoldbarsModel> showGoldbarsModels) {
        this.showGoldbarsModels = showGoldbarsModels;
    }
}

