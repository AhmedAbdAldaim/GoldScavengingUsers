package com.example.goldscavengingusers.Model;

import com.google.gson.annotations.SerializedName;

public class UserUpdateResponse {
    @SerializedName("user")
    private UserUpdateModel userUpdateModels;

    @SerializedName("message")
    private String message;
    @SerializedName("error")
    private boolean error;
    @SerializedName("message_en")
    private String message_en;
    @SerializedName("message_ar")
    private String message_ar;

    public UserUpdateResponse(UserUpdateModel userUpdateModels, String message) {
        this.userUpdateModels = userUpdateModels;
        this.message = message;
    }

    public UserUpdateModel getUserUpdateModels() {
        return userUpdateModels;
    }

    public void setUserUpdateModels(UserUpdateModel userUpdateModels) {
        this.userUpdateModels = userUpdateModels;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
