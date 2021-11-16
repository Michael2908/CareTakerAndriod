package com.example.caretaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HealthCondition extends AppCompatActivity {
    Button btn;
    EditText bloodPressure,bloodSugar,heartRate,temperature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_condition);
        btn = findViewById(R.id.update);
        bloodPressure = findViewById(R.id.bloodPressure);
        bloodSugar = findViewById(R.id.bloodSugar);
        heartRate = findViewById(R.id.heartRate);
        temperature = findViewById(R.id.temperature);
        final ArrayList<String> arr = new ArrayList<>();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("Heart Rate", heartRate.getText().toString());
                map.put("Blood Pressure", bloodPressure.getText().toString());
                map.put("Blood Sugar", bloodSugar.getText().toString());
                map.put("Temperature", temperature.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("Accounts").child(fUser.getUid()).child("Condition").updateChildren(map);
            }
        });
        DatabaseReference fdr = FirebaseDatabase.getInstance().getReference().child("Accounts").child(fUser.getUid()).child("Condition");
        fdr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arr.clear();
                if(dataSnapshot.getValue() != null){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        arr.add(snapshot.getValue().toString());
                    }
                    bloodPressure.setText(arr.get(0));
                    bloodSugar.setText(arr.get(1));
                    heartRate.setText(arr.get(2));
                    temperature.setText(arr.get(3));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HealthCondition.this, "Error! " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}