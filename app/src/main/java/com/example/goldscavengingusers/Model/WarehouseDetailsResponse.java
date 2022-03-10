package com.example.goldscavengingusers.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WarehouseDetailsResponse {

    @SerializedName("dataByDate")
    private List<WarehouseDetailsModel> warehouseDetailsModel;
    @SerializedName("error")
    private boolean error;
    @SerializedName("gold_block")
    private String gold_block;
    @SerializedName("nets")
    private String nets;
    @SerializedName("Expected")
    private String Expected;
    @SerializedName("prices")
    private String prices;
    @SerializedName("message_en")
    private String message_en;
    @SerializedName("message_ar")
    private String message_ar;

    public WarehouseDetailsResponse( List<WarehouseDetailsModel> warehouseDetailsModel, boolean error, String message_en, String message_ar) {
        this.warehouseDetailsModel = warehouseDetailsModel;
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

    public List<WarehouseDetailsModel> getWarehouseDetailsModel() {
        return warehouseDetailsModel;
    }

    public void setWarehouseDetailsModel(List<WarehouseDetailsModel> warehouseDetailsModel) {
        this.warehouseDetailsModel = warehouseDetailsModel;
    }

    public void setMessage_ar(String message_ar) {
        this.message_ar = message_ar;
    }

    public String getGold_block() {
        return gold_block;
    }

    public void setGold_block(String gold_block) {
        this.gold_block = gold_block;
    }

    public String getNets() {
        return nets;
    }

    public void setNets(String nets) {
        this.nets = nets;
    }

    public String getExpected() {
        return Expected;
    }

    public void setExpected(String expected) {
        Expected = expected;
    }

    public String getPrice() {
        return prices;
    }

    public void setPrice(String price) {
        prices= price;
    }
}
