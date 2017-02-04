package com.mndev.pmuigra;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

public class SavePolygonDialog extends DialogFragment implements DialogInterface.OnClickListener {

    public interface Listener {
        void save(String name);
        void cancel();
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_save_polygon, null))
                .setPositiveButton(R.string.polygon_save, this)
                .setNegativeButton(R.string.polygon_cancel, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case DialogInterface.BUTTON_POSITIVE:
                Dialog dialog = (Dialog)dialogInterface;
                EditText editText = (EditText)dialog.findViewById(R.id.dialog_polygon_name);
                if (listener != null) {
                    listener.save(editText.getText().toString());
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
