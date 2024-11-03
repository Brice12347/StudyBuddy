package com.example.studybuddy;

public class GroupChat {
    private String courseID;
    private String groupID;

    public GroupChat(String courseID, String groupID) {
        this.courseID = courseID;
        this.groupID = groupID;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getGroupID() {
        return groupID;
    }
}
