package com.example.studybuddy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

public class AddStudentToGroupActivity extends AppCompatActivity {

    private Button enterButton; // Button to trigger adding the member
    private DatabaseReference groupsRef; // Reference to the groups in the database
    private String groupId; // To hold the current group ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student_to_group_page);

        enterButton = findViewById(R.id.enterButton);

        // Get the groupId passed from the previous activity (if needed)
        groupId = getIntent().getStringExtra("GROUP_ID"); // Ensure to pass the group ID when starting this activity

        // Reference to the user's groups in Firebase
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        groupsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("Groups");

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace with the logic to get the selected username (you may implement a dialog or RecyclerView to choose users)
                String selectedUsername = "exampleUsername"; // Replace with the actual logic to get the selected username

                addMemberToGroup(selectedUsername);
            }
        });
    }

    private void addMemberToGroup(String username) {
        if (username.isEmpty()) {
            Toast.makeText(this, "Please select a valid username.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Assuming your structure has group members under each group ID
        groupsRef.child(groupId).child("members").child(username).setValue(true) // Use 'true' or any placeholder value
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddStudentToGroupActivity.this, "Member added successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Optionally, go back to the previous screen
                    } else {
                        Toast.makeText(AddStudentToGroupActivity.this, "Failed to add member.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
