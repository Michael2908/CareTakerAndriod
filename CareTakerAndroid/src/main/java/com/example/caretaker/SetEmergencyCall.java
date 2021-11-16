package com.example.caretaker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SetEmergencyCall extends AppCompatActivity {
    EditText number;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_emergency_call);
        btn = findViewById(R.id.accept);
        number = findViewById(R.id.number);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eNumber = number.getText().toString().trim();
                if (TextUtils.isEmpty(eNumber)) {
                    number.setError("Number is Required");
                    return;
                }
                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase.getInstance().getReference().child("Accounts").child(fUser.getUid()).child("Emergency Contact").setValue(eNumber);
                finish();
            }
        });

    }
}