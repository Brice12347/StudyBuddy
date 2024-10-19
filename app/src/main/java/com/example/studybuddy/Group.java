package com.example.studybuddy;

public class Group {
    public Group() {
        groupId = "";
        groupName = "";
    }

    public Group(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void viewGroup(){

    }

    private String groupId;
    private String groupName;



}
