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


public class FragmentChangeDialog extends DialogFragment {
    private static final String TAG = FragmentChangeDialog.class.getSimpleName();


    private Button changeButton, cancelButton;
    private EditText changeEanEditText, changeLocationEditText, changeAmountEditText;
    private Item item;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_dialog, container, false);


        changeEanEditText = view.findViewById(R.id.changeEan);
        changeLocationEditText = view.findViewById(R.id.changeLocation);
        changeAmountEditText = view.findViewById(R.id.changeAmount);

        item = ((MainActivity) getActivity()).getSelectedItem();
        changeEanEditText.setText(item.getEan());
        changeLocationEditText.setText(item.getLocation());
        changeAmountEditText.setText(item.getAmount() + "");

        changeEanEditText.addTextChangedListener(changeTextWatcher);
        changeLocationEditText.addTextChangedListener(changeTextWatcher);
        changeAmountEditText.addTextChangedListener(changeTextWatcher);


        changeButton = view.findViewById(R.id.changeButton);
        cancelButton = view.findViewById(R.id.cancelButton);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(changeEanEditText);
                hideKeyboard(changeLocationEditText);
                hideKeyboard(changeAmountEditText);
                ((MainActivity) getActivity()).getAdapter().notifyDataSetChanged();
                dismiss();
            }
        });

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = ((MainActivity) getActivity()).getItemPosition();
                ((MainActivity) getActivity()).getItems().get(position).setAmount(Integer.parseInt(changeAmountEditText.getText().toString()));
                ((MainActivity) getActivity()).getItems().get(position).setEan(changeEanEditText.getText().toString().trim());
                ((MainActivity) getActivity()).getItems().get(position).setLocation(changeLocationEditText.getText().toString().trim().toUpperCase());
                ((MainActivity) getActivity()).getAdapter().notifyItemChanged(position);
                hideKeyboard(changeEanEditText);
                hideKeyboard(changeLocationEditText);
                hideKeyboard(changeAmountEditText);
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
            String eanInput = changeEanEditText.getText().toString().trim();
            String locationInput = changeLocationEditText.getText().toString().trim();
            String amountInput = changeAmountEditText.getText().toString().trim();
            changeButton.setEnabled(!eanInput.isEmpty() && !locationInput.isEmpty() && !amountInput.isEmpty());
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
