package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupPageActivity extends AppCompatActivity {

    private DatabaseReference userGroupsRef;
    private LinearLayout groupsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_page);

        Log.i("GroupPageActivity", "inside onCreate of GroupPageActivity");

        groupsLayout = findViewById(R.id.linearLayout);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        userGroupsRef = FirebaseDatabase.getInstance("https://studybuddy-eeec8-default-rtdb.firebaseio.com/")
                .getReference("users").child(username).child("groups");

        loadUserGroups();

        findViewById(R.id.button).setOnClickListener(v -> {
            Toast.makeText(this, "Add Group functionality not implemented yet", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserGroups() {
        userGroupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot groupSnapshot : snapshot.getChildren()) {
                    String groupId = groupSnapshot.getKey();
                    String groupName = groupSnapshot.child("groupName").getValue(String.class);

                    Log.i("GroupPageActivity loadUserGroups", "inside onCreate of GroupPageActivity");

                    if (groupName != null) {
                        LinearLayout groupLayout = new LinearLayout(GroupPageActivity.this);
                        groupLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        groupLayout.setOrientation(LinearLayout.HORIZONTAL);
                        groupLayout.setGravity(android.view.Gravity.CENTER);

                        TextView groupTextView = new TextView(GroupPageActivity.this);
                        groupTextView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                        groupTextView.setText(groupName);
                        groupTextView.setGravity(android.view.Gravity.CENTER);
                        groupLayout.addView(groupTextView);

                        ImageButton addMemberButton = new ImageButton(GroupPageActivity.this);
                        addMemberButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        addMemberButton.setImageResource(R.drawable.baseline_add_24);
                        addMemberButton.setOnClickListener(v -> {
                            Intent addMemberIntent = new Intent(GroupPageActivity.this, AddStudentToGroupActivity.class);
                            addMemberIntent.putExtra("GROUP_ID", groupId);
                            startActivity(addMemberIntent);
                        });

                        groupLayout.addView(addMemberButton);
                        groupsLayout.addView(groupLayout);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GroupPageActivity.this, "Failed to load groups: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
