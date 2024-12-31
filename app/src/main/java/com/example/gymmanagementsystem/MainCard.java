package com.example.gymmanagementsystem;

public class MainCard {
    private int iconId;
    private String title;
    private int members;

    public MainCard() {
    }

    public MainCard(int iconId, String title, int members) {
        this.iconId = iconId;
        this.title = title;
        this.members = members;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }
}
