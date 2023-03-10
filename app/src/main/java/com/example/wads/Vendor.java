package com.example.wads;

public class Vendor extends User{
    private boolean isActive;
    private boolean suspended;

    public Vendor(){}

    public Vendor(String fullName, String email, long phone, String password, boolean isActive, boolean suspended) {
        super(fullName, email, phone, password);
        this.isActive = isActive;
        this.suspended = suspended;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }
}
