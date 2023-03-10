package com.example.wads;

public class Admin extends User{
    private boolean isAdmin;

    public Admin() {}

    public Admin(String fullName, String email, long phone, String password, boolean isAdmin) {
        super(fullName, email, phone, password);
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
