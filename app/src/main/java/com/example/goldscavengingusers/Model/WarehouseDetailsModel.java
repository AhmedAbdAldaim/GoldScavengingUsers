package com.example.goldscavengingusers.Model;

import com.google.gson.annotations.SerializedName;

public class WarehouseDetailsModel {
    @SerializedName("id_warehouse")
    private String id_warehouse;
    @SerializedName("gold_bar_id")
    private String gold_bar_id;
    @SerializedName("date_add")
    private String date_add;
    @SerializedName("gold_bar_owner")
    private String gold_bar_owner;
    @SerializedName("gold_ingot_weight")
    private String gold_ingot_weight;
    @SerializedName("sample_weight")
    private String sample_weight;
    @SerializedName("gold_karat_weight")
    private String gold_karat_weight;
    @SerializedName("net")
    private String net;
    @SerializedName("price_gram")
    private String price_gram;
    @SerializedName("price")
    private String price;
    @SerializedName("date_add_item")
    private String date_add_item;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("updated_at")
    String updated_at;


    public String getGold_bar_id() {
        return gold_bar_id;
    }

    public void setGold_bar_id(String gold_bar_id) {
        this.gold_bar_id = gold_bar_id;
    }

    public String getDate_add() {
        return date_add;
    }

    public void setDate_add(String date_add) {
        this.date_add = date_add;
    }
    public String getId_warehouse() {
        return id_warehouse;
    }

    public void setId_warehouse(String id_warehouse) {
        this.id_warehouse = id_warehouse;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getPrice_gram() {
        return price_gram;
    }

    public void setPrice_gram(String price_gram) {
        this.price_gram = price_gram;
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
