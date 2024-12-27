package com.example.madproject.ui.signup;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madproject.R;
import com.example.madproject.databinding.FragmentSignUpMainBinding;
import com.example.madproject.ui.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import soup.neumorphism.NeumorphButton;

public class SignUpMainFragment extends Fragment {

    TextInputEditText ETFirstName,ETLastName,ETUsername,ETEmail,ETPassword,ETConfirmPassword;
    CheckBox CBoxGuidelines,CBoxTerms;
    TextView TVGuidelines,TVTerms;
    NeumorphButton BtnSignUp,BtnCancel;

    private FragmentSignUpMainBinding binding;
    private SignUpViewModel signUpViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignUpMainBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(requireActivity());
        signUpViewModel = new ViewModelProvider(requireActivity(),factory).get(SignUpViewModel.class);

        View root = binding.getRoot();

        ETFirstName = binding.ETFirstName;
        ETLastName = binding.ETLastName;
        ETUsername = binding.ETUsername;
        ETEmail = binding.ETEmail;
        ETPassword = binding.ETPassword;
        ETConfirmPassword = binding.ETConfirmPassword;

        CBoxGuidelines = binding.CBoxGuidelines;
        CBoxTerms = binding.CBoxTerms;

        TVGuidelines = binding.TVGuidelines;
        TVTerms = binding.TVTerms;

        BtnSignUp = binding.BtnSignUp;
        BtnCancel = binding.BtnCancel;

        signUpViewModel.resetErrorMsgLiveData();
        signUpViewModel.resetUserLiveData();

        signUpViewModel.getErrorMsgLiveData().observe(getViewLifecycleOwner(), errorMsg -> {
            if(errorMsg != null) {
                Toast.makeText(getContext(),errorMsg,Toast.LENGTH_SHORT).show();
            }
        });
        signUpViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if(user != null){
                switchDetailsFragment();
            }
        });

        signUpViewModel.getGuidelinesAgreeLiveData().observe(getViewLifecycleOwner(), guidelinesAgree -> {
            if(guidelinesAgree != null) {
                CBoxGuidelines.setChecked(guidelinesAgree);
            }
        });
        signUpViewModel.getTermsAgreeLiveData().observe(getViewLifecycleOwner(), termsAgree -> {
            if(termsAgree != null) {
                CBoxTerms.setChecked(termsAgree);
            }
        });

        TVGuidelines.setOnClickListener(v -> {
            GuidelinesFragment popUpFragment = new GuidelinesFragment();
            popUpFragment.show(getParentFragmentManager(), "Community Guidelines");
        });

        TVTerms.setOnClickListener(v -> {
            TermsFragment popUpFragment = new TermsFragment();
            popUpFragment.show(getParentFragmentManager(), "Terms of Service");
        });

        CBoxGuidelines.setOnClickListener(v -> signUpViewModel.setGuidelinesAgreeLiveData(CBoxGuidelines.isChecked()));
        CBoxTerms.setOnClickListener(v -> signUpViewModel.setTermsAgreeLiveData(CBoxTerms.isChecked()));

        BtnSignUp.setOnClickListener(view -> signUp());

        BtnCancel.setOnClickListener(view -> requireActivity().finish());

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void signUp(){
        String firstName = ETFirstName.getText().toString();
        String lastName = ETLastName.getText().toString();
        String username = ETUsername.getText().toString();
        String email = ETEmail.getText().toString();
        String password = ETPassword.getText().toString();
        String confirmPassword = ETConfirmPassword.getText().toString();

        boolean guidelinesChecked = CBoxGuidelines.isChecked();
        boolean termsChecked = CBoxTerms.isChecked();

        signUpViewModel.signUpUser_1(firstName,lastName,username,email,password,confirmPassword,guidelinesChecked,termsChecked);

//        if(firstName.isBlank()||lastName.isBlank()||username.isBlank()||email.isBlank()||password.isBlank()||confirmPassword.isBlank()){
//            Toast.makeText(getContext(),"All the fields must be filled in!",Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(!isEmailValid(email)){
//            Toast.makeText(getContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(!password.equals(confirmPassword)){
//            Toast.makeText(getContext(),"Check your password again",Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(!guidelinesChecked){
//            CBoxGuidelines.requestFocus();
//            Toast.makeText(getContext(),"Read and agree the Community Guidelines to continue",Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(!termsChecked){
//            CBoxTerms.requestFocus();
//            Toast.makeText(getContext(),"Read and agree the Terms of Service to continue",Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Executor.executeTask(() -> {
//
//            boolean emailExist = userDAO.emailExists(email);
//            boolean usernameExist = userDAO.usernameExists(username);
//
//            if(emailExist){
//                requireActivity().runOnUiThread(() -> Toast.makeText(getContext(),"This email address has been signed up",Toast.LENGTH_SHORT).show());
//                return;
//            }
//
//            if(usernameExist){
//                requireActivity().runOnUiThread(() -> Toast.makeText(getContext(),"Username has been used.\nPlease try other",Toast.LENGTH_SHORT).show());
//                return;
//            }
//
//            signUpViewModel.signUpUser(firstName,lastName,username,email,password,confirmPassword,guidelinesChecked,termsChecked);
//
////            String userId = createUserId();
////            User user = new User(userId,firstName,lastName,username,email,password,null,"Male",20,"20-9-2004",null,null);
////            try{
////                firestoreManager.executeAction(FirestoreManager.Action.INSERT,"user", user, getContext());
////            } catch (Exception e) {
////                Log.e(TAG, "Error executing action: " + e.getMessage());
////            }
//
////            switchDetailsFragment();
//
//            // Redirect to login page after successful signup
////            requireActivity().runOnUiThread(() -> {
////                Toast.makeText(getContext(),"Successfully signed up",Toast.LENGTH_SHORT).show();
////                requireActivity().finish();
////            });
//        });

    }

    private void switchDetailsFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.SignUpFCV, new SignUpInfoFragment())
                .addToBackStack(null)
                .commit();
    }
}