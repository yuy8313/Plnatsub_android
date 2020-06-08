package com.example.plnatsub;

import com.google.gson.annotations.SerializedName;

public class AccountItem {
    String success;
    String image;

    String price;
    String date;
    String device;

    String first_name;
    String first_percent;
    String second_name;

    String name;
    String flower;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlower() {
        return flower;
    }

    public void setFlower(String flower) {
        this.flower = flower;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getSecond_percent() {
        return second_percent;
    }

    public void setSecond_percent(String second_percent) {
        this.second_percent = second_percent;
    }

    String second_percent;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getFirst_percent() {
        return first_percent;
    }

    public void setFirst_percent(String first_percent) {
        this.first_percent = first_percent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }



}