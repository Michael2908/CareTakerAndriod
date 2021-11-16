package com.example.caretaker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    EditText mFullName, mPhone,mPassword,mEmail,mEmergencyPhone;
    Button mAccept;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFullName = findViewById(R.id.fullName);
        mPhone = findViewById(R.id.phoneNumber);
        mPassword = findViewById(R.id.password);
        mEmail = findViewById(R.id.email);
        mAccept = findViewById(R.id.button);
        mEmergencyPhone = findViewById(R.id.emergencyContact);

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainMenu.class));
            finish();
        }

        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String phone = mPhone.getText().toString().trim();
                final String name = mFullName.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    mFullName.setError("Name is Required");
                    return;
                }
                if(TextUtils.isEmpty(phone)){
                    mPhone.setError("Phone Number is Required");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Password Must Be At Least 6 Characters");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String emergencyPhone = mEmergencyPhone.getText().toString().trim();
                        if(TextUtils.isEmpty(emergencyPhone)){
                            emergencyPhone = "101";
                        }
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("Email", email);
                        map.put("Name", name);
                        map.put("Phone", phone);
                        map.put("Emergency Contact", emergencyPhone);
                        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseDatabase.getInstance().getReference().child("Accounts").child(fUser.getUid()).updateChildren(map);
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainMenu.class));
                        } else {
                            Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }


                    }
                });

            }
        });
    }
}
