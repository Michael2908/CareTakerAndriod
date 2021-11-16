package com.example.caretaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class PillSchedule extends AppCompatActivity {
    private ListView lv;
    private EditText eName,eHour,eMinute;
    private Button btn;
    private int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_schedule);
        final ArrayList<String> arr = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_item, arr);
        final HashMap<String, Object> map = new HashMap<>();
        lv = findViewById(R.id.pillList);
        eName = findViewById(R.id.pillName);
        eHour = findViewById(R.id.hour);
        eMinute = findViewById(R.id.minute);
        btn = findViewById(R.id.add);
        lv.setAdapter(adapter);
        DatabaseReference fdr = FirebaseDatabase.getInstance().getReference().child("Accounts").child(fUser.getUid()).child("Pill Schedule");
        fdr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 1;
                arr.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String counter = String.valueOf(count);
                    map.put(counter, snapshot.getValue().toString());
                    count += 1;
                    arr.add(snapshot.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PillSchedule.this, "Error! " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = map.size() + 1;
                String sCounter = String.valueOf(counter);
                String name = eName.getText().toString().trim();
                String hour = eHour.getText().toString().trim();
                String minute = eMinute.getText().toString().trim();
                int iHour,iMinute;
                if (TextUtils.isEmpty(name)) {
                    eName.setError("Name is Required");
                    return;
                }
                if (TextUtils.isEmpty(hour)) {
                    eHour.setError("Hour is Required");
                    return;}
                else {
                    iHour = Integer.parseInt(hour);
                    if (iHour > 24 || iHour < 0){
                    eHour.setError("Hour is Required to Be Between 0-24");
                    return;}
                }
                if (TextUtils.isEmpty(minute)) {
                    eMinute.setError("Minutes is Required");
                    return;}
                else{
                    iMinute = Integer.parseInt(minute);
                    if (iMinute > 59 || iMinute < 0){
                    eMinute.setError("Minutes is Required to Be Between 0-59");
                    return;}
                }
                if (minute.length() == 1){
                    minute = "0" + minute;
                }
                map.put(sCounter,name + " " + hour + ":" + minute);
                FirebaseDatabase.getInstance().getReference().child("Accounts").child(fUser.getUid()).child("Pill Schedule").updateChildren(map);
                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_HOUR,iHour);
                intent.putExtra(AlarmClock.EXTRA_MINUTES,iMinute);
                intent.putExtra(AlarmClock.EXTRA_MESSAGE,name);
                startActivity(intent);
                eName.setText(null);
                eHour.setText(null);
                eMinute.setText(null);
            }
        });
    }
}