package com.example.madproject.ui.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Switch;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madproject.R;

public class AccessibilityFragment extends Fragment {

    private Switch locationSwitch, photoSwitch, cameraSwitch, fileAndFolderSwitch;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int PHOTO_PERMISSION_REQUEST_CODE = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    private ActivityResultLauncher<Intent> appSettingsLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_accessibility, container, false);

        // Initialize Switches
        photoSwitch = view.findViewById(R.id.Photo);
        locationSwitch = view.findViewById(R.id.Location);
        cameraSwitch = view.findViewById(R.id.Camera);

        // Initialize ActivityResultLauncher for app settings
        appSettingsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Update switch states when returning from app settings
                    photoSwitch.setChecked(checkPhotoPermission());
                    locationSwitch.setChecked(checkLocationPermission());
                    cameraSwitch.setChecked(checkCameraPermission());
                }
        );

        // Set initial states
        photoSwitch.setChecked(checkPhotoPermission());
        locationSwitch.setChecked(checkLocationPermission());
        cameraSwitch.setChecked(checkCameraPermission());

        // Photo Switch Listener
        photoSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!checkPhotoPermission()) {
                    requestPhotoPermission();
                } else {
                    Toast.makeText(getContext(), "Photo access already enabled", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (checkPhotoPermission()) {
                    openAppSettings();
                    photoSwitch.setChecked(checkPhotoPermission());
                }
            }
        });

        // Location Switch Listener
        locationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!checkLocationPermission()) {
                    requestLocationPermission();
                } else {
                    Toast.makeText(getContext(), "Location access already enabled", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (checkLocationPermission()) {
                    openAppSettings();
                    locationSwitch.setChecked(checkLocationPermission());
                }
            }
        });

        // Camera Switch Listener
        cameraSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    Toast.makeText(getContext(), "Camera access already enabled", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (checkCameraPermission()) {
                    openAppSettings();
                    cameraSwitch.setChecked(checkCameraPermission());
                }
            }
        });

        return view;

    }

    private boolean checkPhotoPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPhotoPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    PHOTO_PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PHOTO_PERMISSION_REQUEST_CODE);
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST_CODE);
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PHOTO_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Photo permission granted", Toast.LENGTH_SHORT).show();
                photoSwitch.setChecked(true);
            } else {
                Toast.makeText(getContext(), "Photo permission denied", Toast.LENGTH_SHORT).show();
                photoSwitch.setChecked(false);
            }
        } else if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Location permission granted", Toast.LENGTH_SHORT).show();
                locationSwitch.setChecked(true);
            } else {
                Toast.makeText(getContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
                locationSwitch.setChecked(false);
            }
        } else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Camera permission granted", Toast.LENGTH_SHORT).show();
                cameraSwitch.setChecked(true);
            } else {
                Toast.makeText(getContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
                cameraSwitch.setChecked(false);
            }
        }
    }

}