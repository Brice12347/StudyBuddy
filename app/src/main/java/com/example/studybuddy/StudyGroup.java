package com.example.studybuddy;

import java.util.ArrayList;
import java.util.List;

public class StudyGroup {
    private String courseID;
    private String groupID;
    private String groupName;
    private String description;
    private List<User> members;

    public StudyGroup(String courseID, String groupID, String groupName, String description) {
        this.courseID = courseID;
        this.groupID = groupID;
        this.groupName = groupName;
        this.description = description;
        this.members = new ArrayList<>();
    }

    // Method to add a member to the group (local operation)
    public void addMember(User user) {
        if (user != null) {
            members.add(user);
        }
    }

    public String getCourseID() {
        return courseID;
    }

    public String getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getDescription() {
        return description;
    }

    public List<User> getMembers() {
        return members;
    }
}
