package com.example.madproject.ui.signup;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.madproject.data.model.User;
import com.example.madproject.databinding.FragmentSignUpInfoBinding;
import com.example.madproject.ui.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import soup.neumorphism.NeumorphButton;

public class SignUpInfoFragment extends Fragment {

    private AutoCompleteTextView ACTVGender;
    private TextInputEditText ETBirthday, ETContact;
    private NeumorphButton BtnContinue, BtnBack;

    private FragmentSignUpInfoBinding binding;
    private SignUpViewModel signUpViewModel;
    private User user;

    private final String[] genderList = {"Male", "Female", "Other"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignUpInfoBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(requireActivity());
        signUpViewModel = new ViewModelProvider(requireActivity(),factory).get(SignUpViewModel.class);

        View root = binding.getRoot();

        ACTVGender = binding.ACTVGender;
        ETBirthday = binding.ETBirthday;
        ETContact = binding.ETContact;
        BtnContinue = binding.BtnContinue;
        BtnBack = binding.BtnBack;

        user = signUpViewModel.getUserLiveData().getValue();

        signUpViewModel.resetUserLiveData();

        signUpViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if(user != null){
                Toast.makeText(getContext(),"Successfully signed up",Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        });

        ACTVGender.setInputType(InputType.TYPE_NULL);
        ACTVGender.setAdapter(new ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, genderList));

        ETBirthday.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        ETBirthday.setText(String.format("%02d-%02d-%d",dayOfMonth,monthOfYear+1,year1));
                    },
                    year, month, day);

            datePickerDialog.show();
        });

        BtnContinue.setOnClickListener(view -> signUp());

        BtnBack.setOnClickListener(view -> requireActivity().getSupportFragmentManager().popBackStack());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void signUp() {
        String gender = ACTVGender.getText().toString();
        String birthday = ETBirthday.getText().toString();
        String contact = ETContact.getText().toString();

        if(gender.isBlank()||birthday.isBlank()||contact.isBlank()) {
            Toast.makeText(requireContext(), "All the fields must be filled in!", Toast.LENGTH_SHORT).show();
            return;
        }

        signUpViewModel.signUpUser_2(user, gender, birthday, contact);
    }
}