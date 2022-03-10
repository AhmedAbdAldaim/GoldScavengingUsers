package com.example.goldscavengingusers.Model;

import com.google.gson.annotations.SerializedName;

public class RecoveryToWarehousesResponse {
    @SerializedName("error")
    private boolean error;
    @SerializedName("message_en")
    private String message_en;
    @SerializedName("message_ar")
    private String message_ar;
    @SerializedName("returned")
    private AddToWarehouseModel addToWarehouseModel;

    public RecoveryToWarehousesResponse(boolean error, String message_en, String message_ar) {
        this.error = error;
        this.message_en = message_en;
        this.message_ar = message_ar;
    }

    public AddToWarehouseModel getAddToWarehouseModel() {
        return addToWarehouseModel;
    }

    public void setAddToWarehouseModel(AddToWarehouseModel addToWarehouseModel) {
        this.addToWarehouseModel = addToWarehouseModel;
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
}
