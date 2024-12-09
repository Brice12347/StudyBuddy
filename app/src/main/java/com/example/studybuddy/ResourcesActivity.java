package com.example.studybuddy;

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
    private String groupId;// = "studyGroup123"; // Replace with dynamic groupId
    private String category;
    private EditText searchBar;
    private String courseName;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resource);
//TODO:WE HAVE TO SAVE THE SOUL SOCIETY



        Intent init_intent = getIntent();
//
        courseName = init_intent.getStringExtra("COURSE_NAME");
        groupId = init_intent.getStringExtra("GROUP_ID");
        Log.i("DATA", "Course Name is: " + courseName);
        Log.i("DATA", "Group_ID is: " + groupId);

        if (courseName == null || groupId == null) {
            Toast.makeText(this, "Missing course name or group ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


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
        backButton = findViewById(R.id.backButton);

        searchBar = findViewById(R.id.searchBar);

        // Ensure the necessary folders exist
        initializeFolders();



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
            intent.putExtra("GROUP_ID", groupId);
            intent.putExtra("category", category);

            startActivity(intent);
            return true;
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ResourcesActivity.this, StudyGroupActivity.class);
                intent.putExtra("username", getIntent().getStringExtra("username"));
                intent.putExtra("COURSE_NAME",courseName);
                intent.putExtra("GROUP_ID",groupId);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initializeFolders() {
        String[] categories = {"LectureNotes", "PracticeExams", "ProjectHelp"};
        for (String cat : categories) {
            StorageReference folderRef = storageReference.child("studyGroups/" + groupId + "/" + cat);
            folderRef.listAll().addOnSuccessListener(listResult -> {
                if (listResult.getItems().isEmpty()) {
                    folderRef.child("placeholder.txt").putBytes("Placeholder file".getBytes())
                            .addOnSuccessListener(taskSnapshot -> Log.i("FirebaseStorage", "Initialized folder: " + cat))
                            .addOnFailureListener(e -> Log.e("FirebaseStorage", "Failed to initialize folder: " + cat, e));
                }
            }).addOnFailureListener(e -> Log.e("FirebaseStorage", "Error checking folder existence: " + cat, e));
        }
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
        if (filePath != null) {
            String fileName = getFileName(filePath);
            String sanitizedFileName = sanitizeFileName(fileName); // Sanitize the filename

            StorageReference ref = storageReference.child("studyGroups/" + groupId + "/" + category + "/" + fileName);

            Log.i("Print", "Storage REF IS: " + ref);
            Log.i("Print", "Line 178 FilePath IS: " + filePath);

            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Store the file URL with the sanitized file name as the key
                        Log.i("Print", "File Name IS: " + fileName);
                        databaseReference.child(groupId).child(category).child(sanitizedFileName).setValue(uri.toString());
                        Toast.makeText(ResourcesActivity.this, "File Uploaded!", Toast.LENGTH_SHORT).show();
                    }))
                    .addOnFailureListener(e -> Toast.makeText(ResourcesActivity.this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_LONG).show());

            Log.i("Print", "Database REF IS: " + databaseReference);

        } else {
            Toast.makeText(this, "File path is null", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to sanitize the file name for Firebase Database
    public String sanitizeFileName(String fileName) {
        return fileName.replace(".", "_")
                .replace("#", "_")
                .replace("$", "_")
                .replace("[", "_")
                .replace("]", "_");
    }



    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }


}


