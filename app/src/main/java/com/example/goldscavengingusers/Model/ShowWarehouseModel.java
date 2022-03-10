package com.example.goldscavengingusers.Model;

import com.google.gson.annotations.SerializedName;

public class ShowWarehouseModel {
    @SerializedName("gold_bar_id")
    private String gold_bar_id;
    @SerializedName("date_add_item")
    private String date_add_item;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("updated_at")
    String updated_at;

    public ShowWarehouseModel(String date_add_item) {
        this.date_add_item = date_add_item;
    }

    public String getGold_bar_id() {
        return gold_bar_id;
    }

    public void setGold_bar_id(String gold_bar_id) {
        this.gold_bar_id = gold_bar_id;
    }

    public String getDate_add_item() {
        return date_add_item;
    }

    public void setDate_add_item(String date_add_item) {
        this.date_add_item = date_add_item;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
