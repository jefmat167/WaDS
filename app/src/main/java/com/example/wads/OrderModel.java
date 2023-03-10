package com.example.wads;

public class OrderModel {
    private String quantity;
    private String address;
    private String area;
    private String fromName;
    private String fromNumber;
    boolean attended;

    public OrderModel(){}

    public OrderModel(String quantity, String address, String area, String fromName, String fromNumber, boolean attended) {
        this.quantity = quantity;
        this.address = address;
        this.area = area;
        this.fromName = fromName;
        this.fromNumber = fromNumber;
        this.attended = attended;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String lodgeName) {
        this.area = lodgeName;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromNumber() {
        return fromNumber;
    }

    public void setFromNumber(String fromNumber) {
        this.fromNumber = fromNumber;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }
}