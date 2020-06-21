package xyz.michaelzhao.postup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class NameDialog extends DialogFragment {

    View view;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_name, null);
        builder.setView(view)
                .setPositiveButton(R.string.submit, (dialog, which) -> moveOn());
        return builder.create();
    }

    protected void moveOn() {
        EditText editText = view.findViewById(R.id.addName);
        String name = editText.getText().toString();
        if (!name.isEmpty()) {
            Intent intent = new Intent(view.getContext(), EditPostActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        }
    }
}
