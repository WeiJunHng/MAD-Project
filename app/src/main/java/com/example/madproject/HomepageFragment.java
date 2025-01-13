package com.example.madproject;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.madproject.data.model.EmergencyContact;
import com.example.madproject.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomepageFragment extends Fragment {

    private View geminiButton; // Button for navigating to Gemini Chat
    private soup.neumorphism.NeumorphButton callButton; // NeumorphButton for accessing contacts
    private soup.neumorphism.NeumorphButton locationButton; // Button for navigating to Location Fragment
    private UserRepository userRepository; // Repository to access emergency contacts
    private SharedPreferences sharedPreferences;

    public HomepageFragment() {
        // Required empty public constructor
    }

    public static HomepageFragment newInstance(String param1, String param2) {
        HomepageFragment fragment = new HomepageFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize UserRepository
        userRepository = new UserRepository(requireContext());
        sharedPreferences = requireActivity().getSharedPreferences("userPreferences", MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the Gemini button
        geminiButton = view.findViewById(R.id.gemini_button);
//        geminiButton.setOnClickListener(v -> navigateToChatFragment());

        // Initialize the Neumorph Call button
        callButton = view.findViewById(R.id.button);
        callButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                showContactsDialog();
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.READ_CONTACTS}, 1);
            }
        });

        // Initialize the Location button
        locationButton = view.findViewById(R.id.location_button);
        locationButton.setOnClickListener(v -> navigateToLocationFragment());

        // Initialize the Emergency Button
        Button emergencyButton = view.findViewById(R.id.button3);
        emergencyButton.setOnClickListener(v -> handleEmergencyButtonClick());
    }

//    private void navigateToChatFragment() {
//        requireActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.FCVMain, new fragment_chat())
//                .addToBackStack(null)
//                .commit();
//    }

    private void navigateToLocationFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FCVMain, new location())
                .addToBackStack(null)
                .commit();
    }

    private void showContactsDialog() {
        ArrayList<String> contacts = new ArrayList<>();
        ArrayList<String> phoneNumbers = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = requireActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contacts.add(name + ": " + phoneNumber);
                    phoneNumbers.add(phoneNumber);
                }
            } else {
                Toast.makeText(requireContext(), "No contacts found", Toast.LENGTH_SHORT).show();
                return;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.searchbar, null);
        EditText searchBar = dialogView.findViewById(R.id.search_bar);
        ListView listView = dialogView.findViewById(R.id.contacts_list);

        ArrayList<String> filteredContacts = new ArrayList<>(contacts);
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, filteredContacts);
        listView.setAdapter(adapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filteredContacts.clear();
                for (String contact : contacts) {
                    if (contact.toLowerCase().contains(s.toString().toLowerCase())) {
                        filteredContacts.add(contact);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select a Contact");
        builder.setView(dialogView);
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
        builder.show();

        listView.setOnItemClickListener((parent, view, position, id) -> confirmCall(phoneNumbers.get(contacts.indexOf(filteredContacts.get(position)))));
    }

    private void confirmCall(String phoneNumber) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Call")
                .setMessage("Do you want to call " + phoneNumber + "?")
                .setPositiveButton("Yes", (dialog, which) -> makePhoneCall(phoneNumber))
                .setNegativeButton("No", null)
                .show();
    }

    private void makePhoneCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.CALL_PHONE}, 2);
        } else {
            try {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Failed to make the call: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleEmergencyButtonClick() {
        String userId =sharedPreferences.getString("userId",null); // Replace with actual user ID logic

        // Fetch emergency contact using UserRepository
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            EmergencyContact emergencyContact = userRepository.getEmergencyContactByUserId(userId);
            requireActivity().runOnUiThread(() -> {
                if (emergencyContact != null && emergencyContact.getContactInfo() != null) {
                    String phoneNumber = emergencyContact.getContactInfo();
                    dialEmergencyNumber(phoneNumber);
                } else {
                    Toast.makeText(getContext(), "Emergency contact not found", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


    private void dialEmergencyNumber(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        // Specify SIM slot (0 = SIM1, 1 = SIM2)
        callIntent.putExtra("com.android.phone.extra.slot", 0);
        callIntent.putExtra("simSlot", 0); // Alternate key for some devices
        callIntent.putExtra("com.android.phone.force.slot", true); // Some manufacturers

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, 2);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2) { // CALL_PHONE permission request code
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission granted. Please press the button again.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission denied to make calls", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
