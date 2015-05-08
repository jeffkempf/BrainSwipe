package edu.jeffkempf.brainswipe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by jeffkempf on 4/29/15.
 */
public class DialogFrag extends DialogFragment {
    private String message;

    public DialogFrag() {
        //fragments should only have default constructors
    }

    public void setMessage(String m) {
        message = m;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // need anything here?
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

