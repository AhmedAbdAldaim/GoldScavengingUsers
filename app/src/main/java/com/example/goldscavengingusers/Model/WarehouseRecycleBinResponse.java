package com.example.goldscavengingusers.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WarehouseRecycleBinResponse {

    @SerializedName("goldBarOwners")
    private List<WarehouseRecycleBinModel> warehouseRecycleBinModels;
    @SerializedName("error")
    private boolean error;
    @SerializedName("message_en")
    private String message_en;
    @SerializedName("message_ar")
    private String message_ar;

    public WarehouseRecycleBinResponse(List<WarehouseRecycleBinModel> warehouseRecycleBinModels, boolean error, String message_en, String message_ar) {
        this.warehouseRecycleBinModels = warehouseRecycleBinModels;
        this.error = error;
        this.message_en = message_en;
        this.message_ar = message_ar;
    }

    public List<WarehouseRecycleBinModel> getWarehouseRecycleBinModels() {
        return warehouseRecycleBinModels;
    }

    public void setWarehouseDetailsModel(List<WarehouseRecycleBinModel> warehouseRecycleBinModels) {
        this.warehouseRecycleBinModels = warehouseRecycleBinModels;
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
