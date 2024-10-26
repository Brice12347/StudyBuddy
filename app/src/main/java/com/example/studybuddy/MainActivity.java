package com.example.studybuddy;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextInputLayout signupUsername, signupConfirmPassword, signupPassword;
    Button signupButton,loginButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//
//

//
        signupUsername = findViewById(R.id.registrationUsernameInput);
        signupPassword = findViewById(R.id.registrationPassInput);
        signupConfirmPassword = findViewById(R.id.registrationConfirmPassInput);
        signupButton = findViewById(R.id.registerButton);
        loginButton =findViewById(R.id.goToSignInPage);
//
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");


                String username = signupUsername.getEditText().getText().toString();
                String password = signupPassword.getEditText().getText().toString();

                reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Username already exists
                            Toast.makeText(MainActivity.this, "Username already exists. Please try a different one.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Username is available, proceed with signup
                            HelperClass helperClass = new HelperClass(username, password);
                            reference.child(username).setValue(helperClass);
                            Toast.makeText(MainActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle possible errors with Firebase operation
                        Toast.makeText(MainActivity.this, "Failed to check username. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
//                HelperClass helperClass = new HelperClass(username, password);
//                reference.child(username).setValue(helperClass);
//                Toast.makeText(MainActivity.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });




    }
}