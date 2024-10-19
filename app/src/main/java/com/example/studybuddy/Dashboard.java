package com.example.studybuddy;

import java.util.ArrayList;
import java.util.List;

public class Dashboard {


    public Dashboard() {
        this.user = null;
        this.myGroup = null;
    }

    public Dashboard(User user, ArrayList<Group> myGroup) {
        this.user = user;
        this.myGroup = myGroup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Group> getMyGroup() {
        return myGroup;
    }

    public void setMyGroup(ArrayList<Group> myGroup) {
        this.myGroup = myGroup;
    }

    public void showDashboard(){

    }

//    studyGroup is not real yet
//    public ArrayList<StudyGroup> displayGroup(){
//
//    }

//  studyGroup is not real yet
//    public ArrayList<StudySession> displayUpcomingSessions(){
//
//    }
//resource doesnt exist yet
//    public ArrayList<Resource> viewResource(){
//
//    }

    public void navigateToStudyGroup(String groupID){

    }

    public void navigateToCalendar(){

    }

    public void navigateToChat(String groupID){

    }






    private User user;
    private ArrayList<Group> myGroup;
//    StudySession has not been made yet
//    private List<StudySession> upcomingStudySession;


}
