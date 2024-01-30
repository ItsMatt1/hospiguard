package com.example.hospiguard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PatologyCheckboxActivity extends AppCompatActivity {
    private Button save;
    private ArrayList<String> patology;
    private CheckBox checkbox1, checkbox2, checkbox3, checkbox4, checkbox5, checkbox6, checkbox7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patology_checkbox);

        patology = new ArrayList<>();

        save = findViewById(R.id.btn_save);
        checkbox1 = findViewById(R.id.checkbox1);
        checkbox2 = findViewById(R.id.checkbox2);
        checkbox3 = findViewById(R.id.checkbox3);
        checkbox4 = findViewById(R.id.checkbox4);
        checkbox5 = findViewById(R.id.checkbox5);
        checkbox6 = findViewById(R.id.checkbox6);
        checkbox7 = findViewById(R.id.checkbox7);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox[] checkboxes = {checkbox1, checkbox2, checkbox3, checkbox4, checkbox5, checkbox6, checkbox7};

                for (CheckBox checkbox : checkboxes) {
                    if (checkbox.isChecked()) {
                        patology.add(checkbox.getText().toString());
                    }
                }
                Intent telaSensores = new Intent( PatologyCheckboxActivity.this, SensorSelectActivity.class);
                telaSensores.putStringArrayListExtra("selected_patologies", patology);
                startActivity(telaSensores);
            }
        });
    }
}