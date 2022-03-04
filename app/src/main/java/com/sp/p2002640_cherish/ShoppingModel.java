package com.sp.p2002640_cherish;

public class ShoppingModel {
    String id, name_shopping, quantity, type, addTimeStamp_shopping, updateTimeStamp_shopping;
    byte[] image;
    public ShoppingModel(String id, String name_shopping, String quantity,String type, String addTimeStamp_shopping, String updateTimeStamp_shopping, byte[] image) {
        this.id = id;
        this.name_shopping = name_shopping;
        this.quantity = quantity;
        this.type = type;
        this.addTimeStamp_shopping = addTimeStamp_shopping;
        this.updateTimeStamp_shopping = updateTimeStamp_shopping;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_shopping() {
        return name_shopping;
    }

    public void setName_shopping(String name_shopping) {
        this.name_shopping = name_shopping;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getAddTimeStamp_shopping() {
        return addTimeStamp_shopping;
    }

    public void setAddTimeStamp_shopping(String addTimeStamp_shopping) {
        this.addTimeStamp_shopping = addTimeStamp_shopping;
    }

    public String getUpdateTimeStamp_shopping() {
        return updateTimeStamp_shopping;
    }

    public void setUpdateTimeStamp_shopping(String updateTimeStamp_shopping) {
        this.updateTimeStamp_shopping = updateTimeStamp_shopping;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

}

