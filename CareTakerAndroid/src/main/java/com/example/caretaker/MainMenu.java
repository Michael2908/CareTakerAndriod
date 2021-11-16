package com.example.caretaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {
    private ImageView pillSchedule, healthCondition,emergency,foodMenu;
    private TextView userName;
    private static String num;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        pillSchedule = findViewById(R.id.pills);
        healthCondition = findViewById(R.id.condition);
        emergency = findViewById(R.id.emergency);
        foodMenu = findViewById(R.id.menus);
        userName = findViewById(R.id.userName);
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Accounts").child(fUser.getUid()).child("Name");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String name = dataSnapshot.getValue(String.class);
            userName.setText("Hello " + name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainMenu.this, "Error! " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        pillSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent((getApplicationContext()), PillSchedule.class));
            }
        });

        healthCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent((getApplicationContext()), HealthCondition.class));
            }
        });

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall();
            }
        });

        foodMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent((getApplicationContext()), FoodMenu.class));
            }
        });

    }

    public void setEmergencyContact(View view){
        startActivity(new Intent(getApplicationContext(), SetEmergencyCall.class));
    }
    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
    private void makePhoneCall() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Accounts").child(fUser.getUid());
        ref.child("Emergency Contact").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    num = dataSnapshot.getValue().toString();
                    Log.d("phone num", num);
                    if (num.trim().length() > 0){
                        if (ContextCompat.checkSelfPermission(MainMenu.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(MainMenu.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                        }
                        else {
                            String dial = "tel:" + num;
                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }
        }else{
            Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
        }

    }
}