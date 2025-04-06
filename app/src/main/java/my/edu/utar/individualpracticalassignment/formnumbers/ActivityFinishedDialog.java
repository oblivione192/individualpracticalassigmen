package my.edu.utar.individualpracticalassignment.formnumbers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import my.edu.utar.individualpracticalassignment.MainActivity;

public class ActivityFinishedDialog extends DialogFragment {
    public interface OnDialogListener{
        void onDialogAccept();
        void onDialogReject();
    }

    private OnDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context){
         super.onAttach(context);
         try{
             listener = (OnDialogListener) context;
         }
         catch(ClassCastException err){
             throw new ClassCastException(context.toString() + " must implement OnDialogInteractionListener");
         }
    }

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Well Done!");
        builder.setMessage("You have completed all the exercises. Hoped you learnt that a\n" +
                           "big number can be made of smaller numbers.\n"+
                            "Would you want to restart the activity?"
                );

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener!= null) {
                    listener.onDialogAccept();
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener!=null) {
                    listener.onDialogReject();
                }
            }
        });

        return builder.create();
    }
}
