package com.connectify.connectify.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class FirebaseService {
    public FirebaseService() throws IOException {
        initFirebase();
    }

    public void initFirebase() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://connectify-kma-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .build();

        FirebaseApp.initializeApp(options);
    }

    public DatabaseReference getDatabaseIns (String path) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference(path);
    }
}
