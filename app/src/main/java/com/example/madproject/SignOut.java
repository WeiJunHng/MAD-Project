package com.example.madproject;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignOut#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignOut extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignOut() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignOut.
     */
    // TODO: Rename and change types and number of parameters
    public static SignOut newInstance(String param1, String param2) {
        SignOut fragment = new SignOut();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_out, container, false);

        Button btnConfirm = view.findViewById(R.id.BtnConfirmSignOut);
        Button btnCancel = view.findViewById(R.id.BtnCancelSignOut);
        ImageButton btnClose = view.findViewById(R.id.BtnCloseIcon);

        btnConfirm.setOnClickListener(v -> {
            v.setScaleX(0.9f);
            v.setScaleY(0.9f);
            v.postDelayed(() -> {
                v.setScaleX(1f);
                v.setScaleY(1f);
                Toast.makeText(getContext(), "Signed Out", Toast.LENGTH_SHORT).show();
                dismiss();
            }, 100);
        });

        btnCancel.setOnClickListener(v -> {
            v.setScaleX(0.9f);
            v.setScaleY(0.9f);
            v.postDelayed(() -> {
                v.setScaleX(1f);
                v.setScaleY(1f);
                dismiss();
            }, 100);
        });

        btnClose.setOnClickListener(v -> {
            v.setScaleX(0.9f);
            v.setScaleY(0.9f);
            v.postDelayed(() -> {
                v.setScaleX(1f);
                v.setScaleY(1f);
                dismiss();
            }, 100);
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set the dialog width and height
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

}