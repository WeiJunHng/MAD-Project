package com.example.madproject.ui.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.madproject.R;
import com.example.madproject.databinding.FragmentAdminProfileMenuBinding;
import com.example.madproject.ui.login.ForgotPasswordVerifyFragment;

public class AdminProfileMenu extends Fragment {

    private FragmentAdminProfileMenuBinding binding;
    private Button BtnManageContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminProfileMenuBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        BtnManageContent = binding.BtnManageContent;

        BtnManageContent.setOnClickListener(view -> switchManageContentFragment());

        return root;
    }

    private void switchManageContentFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.AdminFCV, new ManageContentFragment())
                .addToBackStack(null)
                .commit();
    }
}