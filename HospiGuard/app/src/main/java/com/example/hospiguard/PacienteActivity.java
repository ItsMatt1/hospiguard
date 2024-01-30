package com.example.hospiguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PacienteActivity extends AppCompatActivity implements SingleChoiseDialogFragment.SingleChoiceListener {

    private TextView tvDisplayChoise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paciente);

        tvDisplayChoise = findViewById(R.id.dados_paciente);

        Button btnSelectChoice = findViewById(R.id.select_paciente);
        btnSelectChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment singleChoiceDialog = new SingleChoiseDialogFragment();
                singleChoiceDialog.setCancelable(false);
                singleChoiceDialog.show(getSupportFragmentManager(), "Single Choise Dialog");
            }
        });

    }

    @Override
    public void OnpositiveButtonCkicked(String[] list, int position) {
        tvDisplayChoise.setText("Paciente: " +list[position]);
    }

    @Override
    public void OnNegativeButtomClicked() {
        tvDisplayChoise.setText("Paciente:");
    }
}