package com.siska.palmmont;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("data");// "data" iku diganti ref realtime db
    TextView displayMoisture,displayPh, displayTemp;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch pump;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));
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
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ObjectData data = dataSnapshot.getValue(ObjectData.class);
                if (data!=null){
                    displayMoisture.setText(data.getMoisture());
                    displayTemp.setText(data.getTemp()+"\u2103");
                    displayPh.setText(data.getPh());
                    pump.setChecked(data.isPump());
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("error", String.valueOf(error.toException()));
            }
        });
    }
}