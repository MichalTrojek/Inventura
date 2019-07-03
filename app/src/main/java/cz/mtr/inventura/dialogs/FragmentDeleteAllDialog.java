package cz.mtr.inventura.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import cz.mtr.inventura.MainActivity;
import cz.mtr.inventura.R;


public class FragmentDeleteAllDialog extends DialogFragment {
    private static final String TAG = FragmentDeleteAllDialog.class.getSimpleName();


    private Button yesButton, noButton;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_all_dialog, container, false);

        yesButton = view.findViewById(R.id.yesButton);
        noButton = view.findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).deleteItems();
                dismiss();
            }
        });


        return view;
    }


}
