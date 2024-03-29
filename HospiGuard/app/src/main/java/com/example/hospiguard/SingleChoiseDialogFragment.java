package com.example.hospiguard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SingleChoiseDialogFragment extends DialogFragment {

    int position = 0; //posição default
    public interface SingleChoiceListener{
        void OnpositiveButtonCkicked(String[] list, int position);
        void OnNegativeButtomClicked();
    }
    SingleChoiceListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mListener=(SingleChoiceListener) context;
        }catch (Exception e){
            throw  new ClassCastException(getActivity().toString()+"SingleChoiceListener must implemented");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] list = getActivity().getResources().getStringArray(R.array.escolha_pacientes);

        builder.setTitle("Escolha o paciente desejado")
                .setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener(){
                @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                    position = i;
                }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                    mListener.OnpositiveButtonCkicked(list, position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mListener.OnNegativeButtomClicked();
                    }
                });
              return builder.create();
    }
}
