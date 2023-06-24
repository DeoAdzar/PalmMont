package com.siska.palmmont;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sccomponents.widgets.ScGauge;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("sensor");// "data" iku diganti ref realtime db
    TextView displayValue,pumpTextIndicator,pumpText;
    ImageView pump;
    ScGauge inner, outer;
    boolean switchOn = false;
    int selectedButton = 0;
    RelativeLayout pumpIndicator;
    LinearLayout tempContainer,phContainer,moistContainer;
    ImageView tempIcon, phIcon, moistIcon;
    TextView tempText, phText, moistText;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
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
        inner = findViewById(R.id.gaugeInner);
        outer = findViewById(R.id.gaugeOuter);
        pumpIndicator = findViewById(R.id.pumpIndicator);
        pumpTextIndicator = findViewById(R.id.pumpTextIndicator);
        tempContainer = findViewById(R.id.tempContainer);
        phContainer = findViewById(R.id.phContainer);
        moistContainer = findViewById(R.id.moistContainer);
        tempIcon = findViewById(R.id.tempIcon);
        phIcon = findViewById(R.id.phIcon);
        moistIcon = findViewById(R.id.moistIcon);
        tempText = findViewById(R.id.tempText);
        phText = findViewById(R.id.phText);
        moistText = findViewById(R.id.moistText);
        pumpText = findViewById(R.id.pumpText);

        assert inner != null;
        assert outer != null;
        inner.setStrokesCap(Paint.Cap.ROUND);
        inner.setValue(100, 0, 100);
        outer.setStrokesCap(Paint.Cap.ROUND);

        displayValue = findViewById(R.id.displayValue);
        pump = findViewById(R.id.switchPump);
        getData();
        setButton();

        tempContainer.setOnClickListener(view -> {
            selectedButton = 0;
            setButton();
        });
        phContainer.setOnClickListener(view -> {
            selectedButton = 1;
            setButton();
        });
        moistContainer.setOnClickListener(view -> {
            selectedButton = 2;
            setButton();

        });
        pump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child("pump").setValue(!switchOn);
                switchOn = !switchOn;
                setPump();
            }
        });
    }

    private void setButton() {
        switch (selectedButton){
            case 1:
                setButtonUnselect(tempContainer,tempIcon,ContextCompat.getDrawable(getApplicationContext(),R.drawable.temp_unselect),tempText);
                setButtonSelect(phContainer,phIcon,ContextCompat.getDrawable(getApplicationContext(),R.drawable.ph_selected),phText);
                setButtonUnselect(moistContainer,moistIcon,ContextCompat.getDrawable(getApplicationContext(),R.drawable.moist_unselect),moistText);
                getData();
                break;
            case 2:
                setButtonSelect(moistContainer,moistIcon,ContextCompat.getDrawable(getApplicationContext(),R.drawable.moist_selected),moistText);
                setButtonUnselect(phContainer,phIcon,ContextCompat.getDrawable(getApplicationContext(),R.drawable.ph_unselect),phText);
                setButtonUnselect(tempContainer,tempIcon,ContextCompat.getDrawable(getApplicationContext(),R.drawable.temp_unselect),tempText);
                getData();
                break;
            default:
                setButtonSelect(tempContainer,tempIcon,ContextCompat.getDrawable(getApplicationContext(),R.drawable.temp_selected),tempText);
                setButtonUnselect(phContainer,phIcon,ContextCompat.getDrawable(getApplicationContext(),R.drawable.ph_unselect),phText);
                setButtonUnselect(moistContainer,moistIcon,ContextCompat.getDrawable(getApplicationContext(),R.drawable.moist_unselect),moistText);
                getData();
                break;
        }

    }

    @SuppressLint("SetTextI18n")
    private void setPump() {
        if (switchOn){
            pump.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.pump_on));
            pumpIndicator.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_purple_gradient_50));
            pumpTextIndicator.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            pumpText.setText("Click to turn off");

        }else{
            pump.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.pump_off));
            pumpIndicator.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_grey_soft_50));
            pumpTextIndicator.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
            pumpText.setText("Click to turn on");
        }

    }

    private void setButtonSelect(LinearLayout container, ImageView iconContainer, Drawable icon, TextView text){
        container.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.gradient_color));
        iconContainer.setImageDrawable(icon);
        text.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
    }
    private void setButtonUnselect(LinearLayout container, ImageView iconContainer, Drawable icon, TextView text){
        container.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.color.grey));
        iconContainer.setImageDrawable(icon);
        text.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
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
                    switch (selectedButton){
                        case 1:
                            displayValue.setText(data.getpH());
                            outer.setValue(Float.parseFloat(data.pH),0,100);
                            break;
                        case 2:
                            displayValue.setText(data.getMoist()+"%");
                            outer.setValue(Float.parseFloat(data.moist),0,100);
                            break;
                        default:
                            displayValue.setText(data.getTemp()+"\u2103");
                            outer.setValue(Float.parseFloat(data.temp),0,100);
                            break;
                    }
                    switchOn = data.pump;
                    setPump();
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