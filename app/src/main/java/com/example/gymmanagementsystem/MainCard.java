package com.example.gymmanagementsystem;

public class MainCard {
    private int iconId;
    private String title;
    private long members;

    public MainCard() {
    }

    public MainCard(int iconId, String title, long members) {
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

    public long getMembers() {
        return members;
    }

    public void setMembers(long members) {
        this.members = members;
    }
}
