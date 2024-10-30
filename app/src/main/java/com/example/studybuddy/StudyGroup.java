package com.example.studybuddy;

import java.util.ArrayList;
import java.util.List;

public class StudyGroup {
    public String groupID;
    private String groupName;
    private String description;
    private List<User> members;  // Use User to represent group members
    private List<StudySession> studySessions;
    private GroupChat groupChat;

    //Needed for Firebase
    public StudyGroup() {}

    public StudyGroup(String groupID, String groupName, String description) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.members = new ArrayList<>();
        this.description = description;
        this.studySessions = new ArrayList<>();
        this.groupChat = new GroupChat(groupID);
    }

    // Add a new member to the group
    public void addMember(User user) {
        if (user != null) {
            members.add(user);  // Add User directly to the members list
        }
    }

    // Remove a member by the User object
    public void removeMember(User user) {
        if (user != null) {
            members.remove(user);  // Remove the User object directly
        }
    }


    // Schedule a study session
    public void scheduleStudySession(StudySession session) {
        if (session != null) {
            studySessions.add(session);
        }
    }

    // View the list of members in the group
    public List<User> viewMembers() {
        return new ArrayList<>(members);  // Return a copy of the members list
    }

    // Access the group chat
    public GroupChat accessGroupChat() {
        return groupChat;
    }

    // Getters for groupID, groupName, and description
    public String getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getDescription() {
        return description;
    }
}
