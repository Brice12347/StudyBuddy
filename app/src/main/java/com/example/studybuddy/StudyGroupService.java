package com.example.studybuddy;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudyGroupService {
    private DatabaseReference coursesRef;

    public StudyGroupService() {
        coursesRef = FirebaseDatabase.getInstance().getReference("Courses");
    }

    // Create a new study group in Firebase under a specific course
    public void createStudyGroup(StudyGroup studyGroup) {
        DatabaseReference groupRef = coursesRef.child(studyGroup.getCourseID())
                .child("Groups")
                .child(studyGroup.getGroupID());
        groupRef.child("groupID").setValue(studyGroup.getGroupID());
        groupRef.child("groupName").setValue(studyGroup.getGroupName());
        groupRef.child("description").setValue(studyGroup.getDescription());
    }

    // Add a member to a specific group in Firebase
    public void addMemberToGroup(String courseID, String groupID, User user) {
        DatabaseReference memberRef = coursesRef.child(courseID)
                .child("Groups")
                .child(groupID)
                .child("Members")
                .child(user.getUserID());
        memberRef.setValue(user);
    }
}
