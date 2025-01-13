package com.example.madproject.ui.profile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.madproject.ImageHandler;
import com.example.madproject.R;
import com.example.madproject.data.model.EmergencyContact;
import com.example.madproject.data.model.User;
import com.example.madproject.data.repository.UserRepository;
import java.util.Calendar;
import java.util.Date;

public class EditProfileFragment extends Fragment {

    private UserRepository userRepository;
    private User currentUser;
    private ImageHandler imageHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        userRepository = new UserRepository(requireContext());
        currentUser = userRepository.getCurrentUser();

        ImageButton EditProfilePic = rootView.findViewById(R.id.BtnEditProfileIcon);
        ImageView ProfilePic = rootView.findViewById(R.id.IVProfilePic);
        imageHandler = new ImageHandler(this, ProfilePic);

        ImageHandler.loadImage(currentUser.getProfilePicURL(), ProfilePic);

        EditProfilePic.setOnClickListener(v -> {
            v.setScaleX(0.9f);
            v.setScaleY(0.9f);
            v.postDelayed(() -> {
                v.setScaleX(1f);
                v.setScaleY(1f);
                imageHandler.launchImagePickerWithUpdate();
            }, 100);
        });

        EditText ETUsername = rootView.findViewById(R.id.ETUsername);
        ETUsername.setText(currentUser.getUsername());

        ETUsername.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboardAndClearFocus(ETUsername);
                return true;
            }
            return false;
        });

        EditText ETEmail = rootView.findViewById(R.id.ETEmail);
        ETEmail.setText(currentUser.getEmail());

        EditText ETBirthday = rootView.findViewById(R.id.ETBirthday);
        Date birthday = currentUser.getBirthday();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthday);

        String formattedDate = String.format("%02d-%02d-%d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR));
        ETBirthday.setText(formattedDate);

        ETBirthday.setFocusable(false);
        ETBirthday.setCursorVisible(false);
        ETBirthday.setInputType(EditorInfo.TYPE_NULL);

        ETBirthday.setOnClickListener(v -> {
            Calendar birthdayCalendar = Calendar.getInstance();
            birthdayCalendar.setTime(currentUser.getBirthday());

            int year = birthdayCalendar.get(Calendar.YEAR);
            int month = birthdayCalendar.get(Calendar.MONTH); // Month is 0-indexed
            int day = birthdayCalendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String formattedDateChanged = String.format("%02d-%02d-%d", dayOfMonth, monthOfYear + 1, year1);
                        ETBirthday.setText(formattedDateChanged);
                    },
                    year, month, day);
            datePickerDialog.show();
        });


        EditText ETContact = rootView.findViewById(R.id.ETContact);
        ETContact.setText(currentUser.getContactInfo());

        ETContact.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboardAndClearFocus(ETContact);
                return true;
            }
            return false;
        });

        EditText ETEmergencyContact = rootView.findViewById(R.id.ETEmergencyContact);
        ETEmergencyContact.setText(currentUser.getEmergencyContact());

        ETEmergencyContact.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboardAndClearFocus(ETEmergencyContact);
                return true;
            }
            return false;
        });

        Button BtnSave = rootView.findViewById(R.id.BtnSave);
        BtnSave.setOnClickListener(v -> {
            saveUsernameInfo(ETUsername);
            saveContactInfo(ETContact);
            saveBirthdayInfo(ETBirthday);
            saveEmergencyContactInfo(ETEmergencyContact);

            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return rootView;
    }

    private void saveContactInfo(EditText editText) {
        String newContactInfo = editText.getText().toString().trim();
        if (!newContactInfo.equals(currentUser.getContactInfo())) {
            currentUser.setContactInfo(newContactInfo);
            userRepository.updateUserInFirestore(currentUser);
        }
    }

    private void saveUsernameInfo(EditText editText) {
        String newUsername = editText.getText().toString().trim();
        if (!newUsername.equals(currentUser.getUsername())) {
            currentUser.setUsername(newUsername);
            userRepository.updateUserInFirestore(currentUser);
        }
    }

    private void saveBirthdayInfo(EditText etBirthday) {
        String[] dateParts = etBirthday.getText().toString().split("-");
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]) - 1; // Month is 0-indexed
        int year = Integer.parseInt(dateParts[2]);

        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);
        Date updatedBirthday = selectedDate.getTime();

        if (!updatedBirthday.equals(currentUser.getBirthday())) {
            currentUser.setBirthday(updatedBirthday);
            userRepository.updateUserInFirestore(currentUser);
        }
    }

    private void saveEmergencyContactInfo(EditText editText) {
        String newContactInfo = editText.getText().toString().trim();
        if (!newContactInfo.equals(currentUser.getEmergencyContact())) {
           currentUser.setEmergencyContact(newContactInfo);
            userRepository.updateUserInFirestore(currentUser);
        }
    }

    private void hideKeyboardAndClearFocus(EditText editText) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.clearFocus();
    }

}


// ETUsername.setOnFocusChangeListener((v, hasFocus) -> {
//            if (!hasFocus) {
//                saveUsernameInfo(ETUsername);
//            }
//        });
//
//        ETUsername.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                saveUsernameInfo(ETUsername);
//                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(ETUsername.getWindowToken(), 0);
//                ETUsername.clearFocus();
//                return true;
//            }
//            return false;
//        });
//
//        ETContact.setOnFocusChangeListener((v, hasFocus) -> {
//            if (!hasFocus) {
//                saveContactInfo(ETContact);
//            }
//        });
//
//        ETContact.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                saveContactInfo(ETContact);
//
//                // Hide the keyboard
//                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(ETContact.getWindowToken(), 0);
//                ETContact.clearFocus();
//                return true;
//            }
//            return false;
//        });

//ETContact.setOnEditorActionListener((v, actionId, event) -> {
//        if (actionId == EditorInfo.IME_ACTION_DONE) {
//
//InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(ETContact.getWindowToken(), 0);
//        ETContact.clearFocus();
//                return true;
//                        }
//                        return false;
//                        });

// ETEmergencyContact.setOnFocusChangeListener((v, hasFocus) -> {
//        if (!hasFocus) {
//saveEmergencyContactInfo(ETEmergencyContact);
//            }
//                    });
//
//                    ETEmergencyContact.setOnEditorActionListener((v, actionId, event) -> {
//        if (actionId == EditorInfo.IME_ACTION_DONE) {
//saveEmergencyContactInfo(ETEmergencyContact);
//
//// Hide the keyboard
//InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(ETEmergencyContact.getWindowToken(), 0);
//        ETEmergencyContact.clearFocus();
//                return true;
//                        }
//                        return false;
//                        });