package com.example.solartracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName, tvEmail, tvPhone, tvDeviceStatus;
    Button btnAddDevice, btnLogout;
    ImageView btnHome, btnRefresh, btnProfile;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Init Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Link UI
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvDeviceStatus = findViewById(R.id.tvDeviceStatus);

        btnAddDevice = findViewById(R.id.btnAddDevice);
        btnLogout = findViewById(R.id.btnLogout);

        btnHome = findViewById(R.id.btnHome);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnProfile = findViewById(R.id.btnProfile);

        loadUserData();
        checkDeviceStatus();

        // 🔄 Refresh button
        btnRefresh.setOnClickListener(v -> {
            loadUserData();
            checkDeviceStatus();
            Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
        });

        // 🏠 Go Home
        btnHome.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class))
        );

        // ➕ Add Device (you will create this later)
        btnAddDevice.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, AddDeviceActivity.class))
        );

        // 🚪 Logout
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });
    }

    // 👤 Load User Data
    private void loadUserData() {

        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {

                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");
                        String phone = documentSnapshot.getString("phone");

                        tvName.setText("Name: " + name);
                        tvEmail.setText("Email: " + email);
                        tvPhone.setText("Phone: " + phone);
                    }
                });
    }

    // 🔗 Check if device is added
    private void checkDeviceStatus() {

        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users")
                .document(userId)
                .collection("devices")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (queryDocumentSnapshots.isEmpty()) {
                        tvDeviceStatus.setText("No Device Connected ❌");
                    } else {
                        tvDeviceStatus.setText("Device Connected ✅");
                    }
                });
    }
}