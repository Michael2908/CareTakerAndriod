package com.example.caretaker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddBreakfast extends AppCompatActivity {
    EditText item1,item2,item3,item4,item5;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);
        item1 = findViewById(R.id.item1);
        item2 = findViewById(R.id.item2);
        item3 = findViewById(R.id.item3);
        item4 = findViewById(R.id.item4);
        item5 = findViewById(R.id.item5);
        btn = findViewById(R.id.add_button);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                String item01 = item1.getText().toString().trim();
                String item02 = item2.getText().toString().trim();
                String item03 = item3.getText().toString().trim();
                String item04 = item4.getText().toString().trim();
                String item05 = item5.getText().toString().trim();
                HashMap<String, Object> map = new HashMap<>();
                map.put("1", item01);
                map.put("2", item02);
                map.put("3", item03);
                map.put("4", item04);
                map.put("5", item05);
                FirebaseDatabase.getInstance().getReference().child("Accounts").child(fUser.getUid()).child("Food Menu").child("Breakfast").updateChildren(map);
                finish();


            }
        });
    }
}