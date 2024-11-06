package com.example.studybuddy;

<<<<<<< HEAD
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResourcesActivity {
=======
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.util.Log;
import android.database.Cursor;
import android.provider.OpenableColumns;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.auth.FirebaseUser;

public class ResourcesActivity extends AppCompatActivity {
    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String groupId = "studyGroup123"; // Replace with dynamic groupId
    private String category;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resource);

        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResourcesActivity.this, "Signed in anonymously", Toast.LENGTH_SHORT).show();
                        Log.i("FirebaseAuth", "Anonymous sign-in successful");
                    } else {
                        Toast.makeText(ResourcesActivity.this, "Anonymous sign-in failed", Toast.LENGTH_SHORT).show();
                        Log.i("FirebaseAuth", "Anonymous sign-in FAILED");
                    }
                });

        storage = FirebaseStorage.getInstance();

        storageReference = storage.getReference();
        //storageReference = storage.getReferenceFromUrl("gs://studybuddy-eeec8.firebasestorage.app/o/");
        Log.i("Print","In OnCreate Initial Storage REF IS: " + storageReference);
        Log.i("FirebaseStorage", "Initial bucket path: " + storageReference.getBucket());

        //storage = FirebaseStorage.getInstance("gs://studybuddy-eeec8.firebasestorage.app/o/");
       // storageReference = storage.getReference();
        ////storageReference = storage.getReferenceFromUrl("gs://studybuddy-eeec8.firebasestorage.app/o/");
        Log.i("Print","In OnCreate Initial Storage REF IS: " + storageReference);
        Log.i("FirebaseStorage", "Initial bucket path: " + storageReference.getBucket());

        databaseReference = FirebaseDatabase.getInstance().getReference("Resources");

        Button uploadLectureNotesButton = findViewById(R.id.uploadLectureNotesButton);
        Button uploadPracticeExamsButton = findViewById(R.id.uploadPracticeExamsButton);
        Button uploadProjectHelpButton = findViewById(R.id.uploadProjectHelpButton);

        Button viewLectureNotesButton = findViewById(R.id.viewLectureNotesButton);
        Button viewPracticeExamsButton = findViewById(R.id.viewPracticeExamsButton);
        Button viewProjectHelpButton = findViewById(R.id.viewProjectHelpButton);

        searchBar = findViewById(R.id.searchBar);



        uploadLectureNotesButton.setOnClickListener(view -> {
            category = "LectureNotes";
            selectFile();
        });
        uploadPracticeExamsButton.setOnClickListener(view -> {
            category = "PracticeExams";
            selectFile();
        });
        uploadProjectHelpButton.setOnClickListener(view -> {
            category = "ProjectHelp";
            selectFile();
        });

        viewLectureNotesButton.setOnClickListener(view -> openFileList("LectureNotes"));
        viewPracticeExamsButton.setOnClickListener(view -> openFileList("PracticeExams"));
        viewProjectHelpButton.setOnClickListener(view -> openFileList("ProjectHelp"));

        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            String searchTerm = searchBar.getText().toString();
            Intent intent = new Intent(ResourcesActivity.this, SearchResultsActivity.class);
            intent.putExtra("searchTerm", searchTerm);
            startActivity(intent);
            return true;
        });

    }

    private void openFileList(String category) {
        Intent intent = new Intent(ResourcesActivity.this, FileListActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("category", category);
        startActivity(intent);
    }



    // Use ActivityResultLauncher to handle file selection
    private final ActivityResultLauncher<Intent> fileChooserLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    filePath = result.getData().getData();
                    Log.i("Print","line 83 chosen filepath filePath IS: " + filePath);
                    uploadFile();
                } else {
                    Toast.makeText(ResourcesActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
                }
            });

    // Launch file chooser
    private void selectFile() {
        Intent intent = new Intent();
        intent.setType("*/*");  // Allow all file types
        intent.setAction(Intent.ACTION_GET_CONTENT);
        fileChooserLauncher.launch(Intent.createChooser(intent, "Select File"));
    }

    private void uploadFile() {
        if (filePath != null) {//TEMPORARY DATABASE REFERENCE
            //StorageReference ref = storageReference.child("studyGroups/" + groupId + "/" + category + "/" + System.currentTimeMillis());
            //StorageReference ref = storageReference.child("studyGroups/group1/"  + category + "/" + System.currentTimeMillis());

            //temp:
            StorageReference ref = storageReference.child("studyGroups/studyGroup123/"  + category + "/" + System.currentTimeMillis());
            Log.i("Print","Storage REF IS: " + ref);


            Log.i("Print","Line 108 FilePath IS: " + filePath);

            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                        String uploadId = databaseReference.push().getKey();
                        Log.i("Print","Upload ID IS: " + uploadId);
                        databaseReference.child(groupId).child(category).child(uploadId).setValue(uri.toString());
                        Toast.makeText(ResourcesActivity.this, "File Uploaded!", Toast.LENGTH_SHORT).show();
                    }))
                    .addOnFailureListener(e -> Toast.makeText(ResourcesActivity.this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_LONG).show());





            Log.i("Print","Database REF IS: " + databaseReference);
            Log.i("Print","Database REF IS: " + databaseReference);

        } else {
            Toast.makeText(this, "File path is null", Toast.LENGTH_SHORT).show();
        }
    }
>>>>>>> refs/heads/JonathanWork
}
