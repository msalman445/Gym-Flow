package com.example.gymmanagementsystem;

public class Attendance {
    String name;
    boolean isPresent;

    public Attendance() {
    }

    public Attendance(boolean isPresent, String name) {
        this.isPresent = isPresent;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
