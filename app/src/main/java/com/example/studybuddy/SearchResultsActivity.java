package com.example.studybuddy;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SearchResultsActivity extends AppCompatActivity {

    private ListView searchResultsListView;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private ArrayList<String> fileNames;
    private Map<String, String> fileUrls;
    private String groupId; // Pass this dynamically if required
    private Button backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        searchResultsListView = findViewById(R.id.searchResultsListView);
        fileNames = new ArrayList<>();
        fileUrls = new HashMap<>();
//TODO:SAVE THE SOUL SOCIETY
        // Initialize Back Button
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SearchResultsActivity.this, StudyGroupActivity.class);
                intent.putExtra("username", getIntent().getStringExtra("username")); // Pass username
                startActivity(intent);
                finish();
            }
        });

        String searchTerm = getIntent().getStringExtra("searchTerm");
        groupId = getIntent().getStringExtra("GROUP_ID");


        databaseReference = FirebaseDatabase.getInstance().getReference("Resources").child(groupId);
        //storageReference = FirebaseStorage.getInstance().getReference("studyGroups/" + groupId);



        searchFiles(searchTerm);

        searchResultsListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            String selectedFile = fileNames.get(position);
            String url = fileUrls.get(selectedFile);

            if (url != null) {
                if (url.startsWith("http")) {
                    openUrlInBrowser(url);
                    downloadFile(selectedFile, url);
                } else {
                    downloadFile(selectedFile, url);
                }
            } else {
                Toast.makeText(this, "URL not found for selected file", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchFiles(String searchTerm) {
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DataSnapshot categorySnapshot : task.getResult().getChildren()) {
                    for (DataSnapshot fileSnapshot : categorySnapshot.getChildren()) {
                        String fileName = fileSnapshot.getKey();
                        String fileUrl = fileSnapshot.getValue(String.class);
                        if (fileName != null && fileName.toLowerCase().contains(searchTerm.toLowerCase())) {
                            fileNames.add(fileName);
                            fileUrls.put(fileName, fileUrl);
                        }
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileNames);
                searchResultsListView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Failed to search files", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openUrlInBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void downloadFile(String fileName, String url) {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(this, "/Downloads", fileName);
        downloadManager.enqueue(request);
        Toast.makeText(this, "Download started...", Toast.LENGTH_SHORT).show();
    }
}
