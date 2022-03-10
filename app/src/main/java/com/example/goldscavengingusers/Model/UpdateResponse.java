package com.example.goldscavengingusers.Model;

import com.google.gson.annotations.SerializedName;

public class UpdateResponse {

    @SerializedName("goldBarOwner")
    private UpdateModel updateModel;
    @SerializedName("error")
    private boolean error;
    @SerializedName("message_en")
    private String message_en;
    @SerializedName("message_ar")
    private String message_ar;

    public UpdateResponse(UpdateModel updateModel, boolean error, String message_en, String message_ar) {
        this.updateModel = updateModel;
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

    public UpdateModel getUpdateModel() {
        return updateModel;
    }

    public void setUpdateModel(UpdateModel updateModel) {
        this.updateModel = updateModel;
    }
}
