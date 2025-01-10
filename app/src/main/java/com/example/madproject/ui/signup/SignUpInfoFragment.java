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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.madproject.ImageHandler;
import com.example.madproject.ImageViewModel;
import com.example.madproject.data.model.User;
import com.example.madproject.databinding.FragmentSignUpInfoBinding;
import com.example.madproject.ui.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import soup.neumorphism.NeumorphButton;

public class SignUpInfoFragment extends Fragment {

    private ImageView IVProfilePic;
    private AutoCompleteTextView ACTVGender;
    private TextInputEditText ETBirthday, ETContact;
    private NeumorphButton BtnChangePic, BtnContinue, BtnBack;

    private FragmentSignUpInfoBinding binding;
    private SignUpViewModel signUpViewModel;
//    private ImageViewModel imageViewModel;
    private ImageHandler imageHandler;
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
//        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        View root = binding.getRoot();

        IVProfilePic = binding.IVProfilePic;
        ACTVGender = binding.ACTVGender;
        ETBirthday = binding.ETBirthday;
        ETContact = binding.ETContact;
        BtnChangePic = binding.BtnChangePic;
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
//        imageViewModel.getImageUriLiveData().observe(getViewLifecycleOwner(), uri -> {
//            if (uri != null) {
//                IVProfilePic.setImageURI(uri);
//            }
//        });

        imageHandler = new ImageHandler(this,IVProfilePic);

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

//        IVProfilePic.post(() -> {
//            int width = IVProfilePic.getWidth();
//            int height = IVProfilePic.getHeight();
//            Log.d("ImageViewDimensions", "Width: " + width + ", Height: " + height);
//        });

        BtnChangePic.setOnClickListener(view -> choosePic());

        BtnContinue.setOnClickListener(view -> signUp());

        BtnBack.setOnClickListener(view -> requireActivity().getSupportFragmentManager().popBackStack());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void choosePic() {
//        ImagePickerUtil.requestImage(this);
//        ImageHandler imageHandler = new ImageHandler();
//        ImageViewModel imageViewModel = this.imageViewModel;
//        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.SignUpFCV, imageHandler);
//
//        // Check if the fragment is already in the back stack to avoid adding it again
//        if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {
//            transaction.addToBackStack(null);
//        }
//
//        // Commit the transaction
//        transaction.commitNow();
//
//        // Perform other fragment setup if needed
//        if (imageHandler.isAdded()) {
//            imageHandler.requestImage(IVProfilePic, imageViewModel);
//        } else {
//            Log.w("ImageHandler", "Fragment is not attached yet.");
//        }
        imageHandler.launchImagePicker();
    }

    private void signUp() {
        String gender = ACTVGender.getText().toString();
        String birthday = ETBirthday.getText().toString();
        String contact = ETContact.getText().toString();

        if(gender.isBlank()||birthday.isBlank()||contact.isBlank()) {
            Toast.makeText(requireContext(), "All the fields must be filled in!", Toast.LENGTH_SHORT).show();
            return;
        }

//        Bitmap imageBitmap = null;
//        Drawable drawable = IVProfilePic.getDrawable();
//        if(drawable != null && !drawable.equals(ImageHandlerNew.getNotAvailableDrawable(requireContext()))) {
//            imageBitmap = ((BitmapDrawable) drawable).getBitmap();
//        }

        String encodedImage = ImageHandler.encodeImage(IVProfilePic);

        signUpViewModel.signUpUser_2(requireContext(), user, gender, birthday, contact, encodedImage);
    }
}