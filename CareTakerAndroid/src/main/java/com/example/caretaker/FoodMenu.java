package com.example.caretaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.usage.NetworkStats;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.List;

public class FoodMenu extends AppCompatActivity {
    ListView blv, llv, dlv;
    Button bbtn,dbtn,lbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);
        blv = findViewById(R.id.breakfast_list);
        llv = findViewById(R.id.lunch_list);
        dlv = findViewById(R.id.dinner_list);
        bbtn = findViewById(R.id.breakfast_button);
        dbtn = findViewById(R.id.dinner_button);
        lbtn =findViewById(R.id.lunch_button);
        final ArrayList<String> arr = new ArrayList<>();
        final ArrayList<String> arr2 = new ArrayList<>();
        final ArrayList<String> arr3 = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_item, arr);
        final ArrayAdapter adapter2 = new ArrayAdapter<String>(this,R.layout.list_item, arr2);
        final ArrayAdapter adapter3 = new ArrayAdapter<String>(this,R.layout.list_item, arr3);

        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference fdr;


        bbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddBreakfast.class));
            }
        });
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddLunch.class));
            }
        });
        dbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddDinner.class));
            }
        });

        blv.setAdapter(adapter);
        llv.setAdapter(adapter2);
        dlv.setAdapter(adapter3);

        fdr = FirebaseDatabase.getInstance().getReference().child("Accounts").child(fUser.getUid()).child("Food Menu").child("Breakfast");
        fdr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arr.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    arr.add(snapshot.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FoodMenu.this, "Error! " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        fdr = FirebaseDatabase.getInstance().getReference().child("Accounts").child(fUser.getUid()).child("Food Menu").child("Lunch");
        fdr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arr2.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    arr2.add(snapshot.getValue().toString());
                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FoodMenu.this, "Error! " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        fdr = FirebaseDatabase.getInstance().getReference().child("Accounts").child(fUser.getUid()).child("Food Menu").child("Dinner");
        fdr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arr3.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    arr3.add(snapshot.getValue().toString());
                }
                adapter3.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FoodMenu.this, "Error! " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}