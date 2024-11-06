package com.example.studybuddy;

import java.util.ArrayList;
import java.util.List;

public class Course {

    public Course(String courseId, String courseName, String description, ArrayList<User> enrolledStudents) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.enrolledStudents = enrolledStudents;
    }

    public Course(String courseId, String courseName, String description) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
    }

    public Course() {
        this.courseId = "";
        this.courseName = "";
        this.description = "";
        this.enrolledStudents = null;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<User> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(ArrayList<User> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public void printStudents(){
        for (User u: this.enrolledStudents){
            System.out.println(u.username);
        }
    }

    public void addToEnrolledStudents(User student){
        this.enrolledStudents.add(student);
    }

    private String courseId;
    private String courseName;
    private String description;
    private ArrayList<User> enrolledStudents = new ArrayList<>();

    public void enrollStudent(User student){

    }

    public String getCourseInfo(){
        return null;
    }
    public ArrayList<User>tEnrolledStudents(){
        return null;
    }

}
