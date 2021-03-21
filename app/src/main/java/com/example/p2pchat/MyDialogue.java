package com.example.p2pchat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
public class MyDialogue extends AppCompatDialogFragment{

    public String selected;
    ChatThread.SendReceive sendReceive;

    public MyDialogue(ChatThread.SendReceive sendReceive){
        this.sendReceive = sendReceive;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final String[] backgs = getActivity().getResources().getStringArray(R.array.backgroundImages);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select a background");
        builder.setSingleChoiceItems(R.array.backgroundImages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selected = backgs[which];
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChatThread.changeBackground(selected);
                selected = "image###"+selected;
                sendReceive.write(selected.getBytes());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}

