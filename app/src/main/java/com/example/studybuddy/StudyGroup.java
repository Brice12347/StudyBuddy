package com.example.studybuddy;


public class StudyGroup(){
    private String groupID;
    private String groupName;
    private String description;
    private List<Member> Members;
    private List<StudySession> studySessions;
    private GroupChat groupchat;

    public StudyGroup(String groupID, String groupName, String description){
        this.groupID = groupID;
        this.groupName = groupName;
        this.members = new ArrayList<>();
        this.description = description;
        this.studySessions = new ArrayList<>();
        this.groupChat = new GroupChat(groupID);
    }

    public void addMember(User user){
        if(user != null){
            Member newMember = new Member(user);
            members.add(newMember);
        }
    }

    public void removeMember(String userID) {
        members.removeIf(member -> member.getUser().getUserID().equals(userID));
    }

    public void scheduleStudySession(StudySession session) {
        if (session != null) {
            studySessions.add(session);
        }
    }

    public List<Member> viewMembers() {
        return new ArrayList<>(members);  // Return a copy of the members list
    }

    public GroupChat accessGroupChat() {
        return groupChat;
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

}