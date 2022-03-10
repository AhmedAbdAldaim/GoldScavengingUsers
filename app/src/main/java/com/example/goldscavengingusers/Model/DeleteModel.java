package com.example.goldscavengingusers.Model;

import com.google.gson.annotations.SerializedName;

public class DeleteModel {
    @SerializedName("id")
    private String id;
    @SerializedName("gold_bar_owner")
    private String gold_bar_owner;
    @SerializedName("gold_ingot_weight")
    private String gold_ingot_weight;
    @SerializedName("sample_weight")
    private String sample_weight;
    @SerializedName("gold_karat_weight")
    private String gold_karat_weight;
    @SerializedName("date_add")
    private String date_add;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("updated_at")
    String updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGold_bar_owner() {
        return gold_bar_owner;
    }

    public void setGold_bar_owner(String gold_bar_owner) {
        this.gold_bar_owner = gold_bar_owner;
    }

    public String getGold_ingot_weight() {
        return gold_ingot_weight;
    }

    public void setGold_ingot_weight(String gold_ingot_weight) {
        this.gold_ingot_weight = gold_ingot_weight;
    }

    public String getSample_weight() {
        return sample_weight;
    }

    public void setSample_weight(String sample_weight) {
        this.sample_weight = sample_weight;
    }

    public String getGold_karat_weight() {
        return gold_karat_weight;
    }

    public void setGold_karat_weight(String gold_karat_weight) {
        this.gold_karat_weight = gold_karat_weight;
    }

    public String getDate_add() {
        return date_add;
    }

    public void setDate_add(String date_add) {
        this.date_add = date_add;
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
