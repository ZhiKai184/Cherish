package com.sp.p2002640_cherish;

public class StorageModel {
    String id, name,date, price, weight, addTimeStamp, updateTimeStamp;
    byte[] image;

    public StorageModel(String id, String name, String date,String price, String weight, String string, String cursorString, byte[] image) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.price = price;
        this.weight = weight;
        this.addTimeStamp = addTimeStamp;
        this.updateTimeStamp = updateTimeStamp;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAddTimeStamp() {
        return addTimeStamp;
    }

    public void setAddTimeStamp(String addTimeStamp) {
        this.addTimeStamp = addTimeStamp;
    }

    public String getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(String updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

}
