package com.example.madproject.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.madproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilePage.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilePageFragment newInstance(String param1, String param2) {
        ProfilePageFragment fragment = new ProfilePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfilePageFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile_page, container, false);

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
//                    FragmentTransaction transaction1 = requireActivity().getSupportFragmentManager().beginTransaction();
//                    transaction1.replace(R.id.FCVMain, new AllowNotificationFragment());
//                    transaction1.addToBackStack(null); // Add the transaction to the back stack for navigation
//                    transaction1.commit();
                    Navigation.findNavController(requireActivity(), R.id.FCVMain).navigate(R.id.allowNotificationFragment);
                    break;

                case "Get Support":
//                    FragmentTransaction transaction2 = requireActivity().getSupportFragmentManager().beginTransaction();
//                    transaction2.replace(R.id.FCVMain, new GetSupportFragment());
//                    transaction2.addToBackStack(null);
//                    transaction2.commit();
                    Navigation.findNavController(requireActivity(), R.id.FCVMain).navigate(R.id.getSupportFragment);
                    break;

                case "Accessibility":
//                    FragmentTransaction transaction3 = requireActivity().getSupportFragmentManager().beginTransaction();
//                    transaction3.replace(R.id.FCVMain, new AccessibilityFragment());
//                    transaction3.addToBackStack(null); // Add the transaction to the back stack for navigation
//                    transaction3.commit();
                    Navigation.findNavController(requireActivity(), R.id.FCVMain).navigate(R.id.accessibilityFragment);
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
