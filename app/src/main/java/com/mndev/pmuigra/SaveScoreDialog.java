package com.mndev.pmuigra;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

public class SaveScoreDialog extends DialogFragment implements DialogInterface.OnClickListener {
    public static String ARGUMENT_TOTAL_GAME_TIME;

    public interface Listener {
        void save(String name, String totalGameTime);
        void cancel();
    }

    private SaveScoreDialog.Listener listener;
    private String totalGameTime;

    public void setListener(SaveScoreDialog.Listener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        totalGameTime = getArguments().getString(ARGUMENT_TOTAL_GAME_TIME);
        builder.setView(inflater.inflate(R.layout.dialog_save_score, null))
                .setPositiveButton(R.string.polygon_save, this)
                .setNegativeButton(R.string.polygon_cancel, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case DialogInterface.BUTTON_POSITIVE:
                Dialog dialog = (Dialog)dialogInterface;
                EditText editText = (EditText)dialog.findViewById(R.id.dialog_username);
                if (listener != null) {
                    listener.save(editText.getText().toString(), totalGameTime);
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                if (listener != null) {
                    listener.cancel();
                }
                break;
        }
    }
}
