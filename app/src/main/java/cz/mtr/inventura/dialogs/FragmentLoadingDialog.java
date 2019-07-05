package cz.mtr.inventura.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import cz.mtr.inventura.R;


public class FragmentLoadingDialog extends DialogFragment {
    private static final String TAG = FragmentLoadingDialog.class.getSimpleName();


    private ProgressBar progress;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading_dialog, container, false);
        progress = view.findViewById(R.id.progressBar);
        return view;
    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return new Dialog(getActivity(), getTheme()) {
//
//        };
//    }


}
