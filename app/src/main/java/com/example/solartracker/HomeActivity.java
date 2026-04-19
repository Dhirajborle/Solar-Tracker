package com.example.solartracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    TextView tvXAngle, tvYAngle, tvVoltage, tvCurrent, tvPower;
    ImageView btnRefresh, btnHome, btnProfile;

    DatabaseReference solarRef;
    ValueEventListener solarListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvXAngle = findViewById(R.id.tvXAngle_GIT);
        tvYAngle = findViewById(R.id.tvYAngle);
        tvVoltage = findViewById(R.id.tvVoltage);
        tvCurrent = findViewById(R.id.tvCurrent);
        tvPower = findViewById(R.id.tvPower);

        btnRefresh = findViewById(R.id.btnRefresh);
        btnHome = findViewById(R.id.btnHome);
        btnProfile = findViewById(R.id.btnProfile);

        solarRef = FirebaseDatabase.getInstance().getReference("solar");

        getData();

        btnRefresh.setOnClickListener(v ->
                Toast.makeText(this, "Live data is updating", Toast.LENGTH_SHORT).show()
        );

        btnHome.setOnClickListener(v ->
                Toast.makeText(this, "Already on Home", Toast.LENGTH_SHORT).show()
        );

        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class))
        );
    }

    private void getData() {
        solarListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    tvXAngle.setText("X Angle: --");
                    tvYAngle.setText("Y Angle: --");
                    tvVoltage.setText("Voltage: --");
                    tvCurrent.setText("Current: --");
                    tvPower.setText("Power: --");
                    return;
                }

                Integer h = snapshot.child("h_angle").getValue(Integer.class);
                Integer v = snapshot.child("v_angle").getValue(Integer.class);

                Double voltage = snapshot.child("voltage").getValue(Double.class);
                Double current = snapshot.child("current").getValue(Double.class);
                Double power = snapshot.child("power").getValue(Double.class);

                String mode = snapshot.child("mode").getValue(String.class);

                if (h != null) {
                    tvXAngle.setText("X Angle: " + h + "°");
                } else {
                    tvXAngle.setText("X Angle: --");
                }

                if (v != null) {
                    tvYAngle.setText("Y Angle: " + v + "°");
                } else {
                    tvYAngle.setText("Y Angle: --");
                }

                if (voltage != null) {
                    tvVoltage.setText(String.format(Locale.getDefault(), "Voltage: %.2f V", voltage));
                } else {
                    tvVoltage.setText("Voltage: --");
                }

                if (current != null) {
                    tvCurrent.setText(String.format(Locale.getDefault(), "Current: %.2f A", current));
                } else {
                    tvCurrent.setText("Current: --");
                }

                if (power != null) {
                    if (mode != null && !mode.isEmpty()) {
                        tvPower.setText(String.format(Locale.getDefault(), "Power: %.2f W | Mode: %s", power, mode));
                    } else {
                        tvPower.setText(String.format(Locale.getDefault(), "Power: %.2f W", power));
                    }
                } else {
                    if (mode != null && !mode.isEmpty()) {
                        tvPower.setText("Power: -- | Mode: " + mode);
                    } else {
                        tvPower.setText("Power: --");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        solarRef.addValueEventListener(solarListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (solarListener != null) {
            solarRef.removeEventListener(solarListener);
        }
    }
}