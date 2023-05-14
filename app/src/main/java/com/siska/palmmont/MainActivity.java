package com.siska.palmmont;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("data");// "data" iku diganti ref realtime db
    TextView displayMoisture,displayPh, displayTemp;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch pump;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayMoisture = findViewById(R.id.displayMoisture);
        displayPh = findViewById(R.id.displayPh);
        displayTemp = findViewById(R.id.displayTemp);
        pump = findViewById(R.id.switchPump);
        getData();
        pump.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                myRef.child("pump").setValue(b);
            }
        });
    }

    private void getData() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ObjectData data = dataSnapshot.getValue(ObjectData.class);
                assert data != null;
                displayMoisture.setText(data.getMoisture());
                displayTemp.setText(data.getTemp());
                displayPh.setText(data.getPh());
                pump.setChecked(data.isPump());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("error", String.valueOf(error.toException()));
            }
        });
    }
}