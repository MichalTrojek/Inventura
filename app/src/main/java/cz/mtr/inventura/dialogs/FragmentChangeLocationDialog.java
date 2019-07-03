package cz.mtr.inventura.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import cz.mtr.inventura.MainActivity;
import cz.mtr.inventura.R;
import cz.mtr.inventura.listView.Item;


public class FragmentChangeLocationDialog extends DialogFragment {
    private static final String TAG = FragmentChangeLocationDialog.class.getSimpleName();


    private Button changeButton, cancelButton;
    private EditText changeLocationEditText;
    private Item item;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_location_dialog, container, false);


        changeLocationEditText = view.findViewById(R.id.changeLocation);


        changeLocationEditText.addTextChangedListener(changeTextWatcher);


        changeButton = view.findViewById(R.id.changeButton);
        changeButton.setEnabled(false);
        cancelButton = view.findViewById(R.id.cancelButton);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(changeLocationEditText);
                dismiss();
            }
        });

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).changeLocation(changeLocationEditText.getText().toString().trim().toUpperCase());
                hideKeyboard(changeLocationEditText);
                dismiss();
            }
        });


        return view;
    }

    private TextWatcher changeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String locationInput = changeLocationEditText.getText().toString().trim();
            changeButton.setEnabled(!locationInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


}
