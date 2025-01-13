package com.example.madproject.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.madproject.ImageHandler;
import com.example.madproject.R;
import com.example.madproject.data.model.User;
import com.example.madproject.data.repository.UserRepository;

public class ProfilePageFragment extends Fragment {

    private UserRepository userRepository;
    private User currentUser;
    private ImageHandler imageHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile_page, container, false);

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


        // ListView reference
        ListView listView = rootView.findViewById(R.id.AccountSettingsList);

        // List items
        String[] settingsItems = {
                "Change Password",
                "Accessibility",
                "Allow Notification",
                "Get Support",
                "Sign Out",
                "Delete Account"
        };

        // Set custom adapter
        SettingsAdapter adapter = new SettingsAdapter(settingsItems);
        listView.setAdapter(adapter);

        Button btnEditProfile = rootView.findViewById(R.id.BtnEditProfile); // Use rootView directly

        TextView TVProfileUsername = rootView.findViewById(R.id.TVProfileUsername);
        TVProfileUsername.setText(currentUser.getUsername());

        TextView TVProfileUserID = rootView.findViewById(R.id.TVProfileUserID);
        TVProfileUserID.setText("@" + currentUser.getId());

                btnEditProfile.setOnClickListener(v -> {
            v.setScaleX(0.9f);
            v.setScaleY(0.9f);
            v.postDelayed(() -> {
                v.setScaleX(1f);
                v.setScaleY(1f);
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.FCVMain, new EditProfileFragment());
                transaction.addToBackStack(null); // Add the transaction to the back stack for navigation
                transaction.commit();
            }, 100);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            switch (settingsItems[position]) {

                case "Change Password":
                    ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog();
                    changePasswordDialog.show(getParentFragmentManager(), "changePasswordDialog");
                    break;

                case "Allow Notification":
                    FragmentTransaction transaction1 = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction1.replace(R.id.FCVMain, new AllowNotificationFragment());
                    transaction1.addToBackStack(null); // Add the transaction to the back stack for navigation
                    transaction1.commit();
                    break;

                case "Get Support":
                    FragmentTransaction transaction2 = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction2.replace(R.id.FCVMain, new GetSupportFragment());
                    transaction2.addToBackStack(null);
                    transaction2.commit();
                    break;

                case "Accessibility":
                    FragmentTransaction transaction3 = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction3.replace(R.id.FCVMain, new AccessibilityFragment());
                    transaction3.addToBackStack(null); // Add the transaction to the back stack for navigation
                    transaction3.commit();
                    break;

                case "Sign Out":
                    SignOutDialog signOutDialog = new SignOutDialog();
                    signOutDialog.show(getParentFragmentManager(), "SignOutDialog");
                    break;

                case "Delete Account":
                    DeleteAccountDialog deleteAccountdialog = new DeleteAccountDialog();
                    deleteAccountdialog.show(getParentFragmentManager(), "DeleteAccountDialog");
                    break;
            }
        });

        return rootView;
    }
}
