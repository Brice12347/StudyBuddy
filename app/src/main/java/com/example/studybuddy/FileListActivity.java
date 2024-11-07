package com.example.studybuddy;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileListActivity extends AppCompatActivity {

    private ListView fileListView;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ArrayList<String> fileNames;
    private Map<String, String> fileUrls; // Maps file name to URL
    private String groupId;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        fileListView = findViewById(R.id.fileListView);
        fileNames = new ArrayList<>();
        fileUrls = new HashMap<>();

        // Get groupId and category from intent
        groupId = getIntent().getStringExtra("groupId");
        category = getIntent().getStringExtra("category");

        databaseReference = FirebaseDatabase.getInstance().getReference("Resources")
                .child(groupId).child(category);
        storageReference = FirebaseStorage.getInstance().getReference();

        fetchFiles();

        fileListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
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

    private void fetchFiles() {
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String fileName = snapshot.getKey();
                    String fileUrl = snapshot.getValue(String.class);
                    fileNames.add(fileName);
                    fileUrls.put(fileName, fileUrl);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileNames);
                fileListView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Failed to fetch files", Toast.LENGTH_SHORT).show();
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
