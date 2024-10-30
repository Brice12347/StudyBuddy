package com.example.studybuddy;

import android.util.Log;
import com.example.studybuddy.StudyGroup;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudyGroupRepository {
    private FirebaseFirestore db;

    public StudyGroupRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void addStudyGroup(StudyGroup studyGroup) {
        DocumentReference studyGroupRef = db.collection("StudyGroups").document(studyGroup.groupID);

        studyGroupRef.set(studyGroup)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Study group added"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error adding study group", e));
    }
}