package com.example.gymmanagementsystem;

public class Member {
    private String name;
    private String phoneNumber;
    private String address;
    private String planName;
    private double planAmount;
    private double paidAmount;
    private String startDate;
    private String endDate;
    private String gender;
    private long timeStamp;

//    Constructors


    public Member() {
    }

    public Member(String name, String phoneNumber, String address, String planName, double planAmount, double paidAmount, String startDate, String endDate) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.planName = planName;
        this.planAmount = planAmount;
        this.paidAmount = paidAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Member(long timeStamp , String gender, String endDate, String startDate, double paidAmount, double planAmount, String planName, String address, String phoneNumber, String name) {
        this.gender = gender;
        this.endDate = endDate;
        this.startDate = startDate;
        this.paidAmount = paidAmount;
        this.planAmount = planAmount;
        this.planName = planName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

//    Getter Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public double getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(double planAmount) {
        this.planAmount = planAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
