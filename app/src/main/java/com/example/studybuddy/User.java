package com.example.studybuddy;

import java.util.ArrayList;

public class User {
    public User() {
        this.email = "";
        this.password = "";
        this.profileInfo = null;
    }

    public User(String email, String password, ArrayList<String> profileInfo) {
        this.email = email;
        this.password = password;
        this.profileInfo = profileInfo;
    }

    public ArrayList<String> getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(ArrayList<String> profileInfo) {
        this.profileInfo = profileInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public void register(String email, String password, String profileInfo){

    }

    public void login(String email, String password){

    }
//    study group has yet to be made
//    StudyGroup createGroup(String groupName){
//
//    }

    public void joinGroup( String groupID){

    }
//    study group has yet to be made
//    List<StudyGroup> viewGroups(){
//
//    }

    public void viewDashboard(){

    }

//    study group yet to be made
//    void addMemberToGroup(StudyGroup group, User newMember){
//
//    }


    private String email;
    private String password;
    private ArrayList<String> profileInfo;






}
