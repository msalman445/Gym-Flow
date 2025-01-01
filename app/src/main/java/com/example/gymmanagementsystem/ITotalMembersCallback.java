package com.example.gymmanagementsystem;

public interface ITotalMembersCallback {
    void onTotalMembersReceived(long totalMembers);
    void onError(Exception e);
}

